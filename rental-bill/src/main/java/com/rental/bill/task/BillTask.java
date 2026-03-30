package com.rental.bill.task;

import com.rental.bill.entity.Contract;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.entity.Tenant;
import com.rental.bill.mapper.ContractMapper;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.mapper.TenantMapper;
import com.rental.common.notify.NotifyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 账单定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BillTask {

    private final TenantMapper tenantMapper;
    private final RentRecordMapper rentRecordMapper;
    private final ContractMapper contractMapper;

    @Autowired(required = false)
    private NotifyClient notifyClient;

    /**
     * 每月1日自动生成租金账单
     * 规则：针对所有"已入住"的租客，按合同生成月度账单
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateMonthlyBills() {
        log.info("开始生成月度租金账单...");
        
        // 查询所有已入住的租客
        List<Tenant> tenants = tenantMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Tenant>()
                        .eq(Tenant::getStatus, 1) // 已入住
        );
        
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        for (Tenant tenant : tenants) {
            try {
                // 检查当月是否已生成账单
                Long existingCount = rentRecordMapper.selectCount(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentRecord>()
                                .eq(RentRecord::getTenantId, tenant.getId())
                                .eq(RentRecord::getPayMonth, currentMonth)
                );
                
                if (existingCount > 0) {
                    log.info("租客 {} 的 {} 月账单已存在，跳过", tenant.getId(), currentMonth);
                    continue;
                }
                
                // 从合同表获取租金金额
                Contract contract = contractMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Contract>()
                        .eq(Contract::getTenantId, tenant.getId())
                        .eq(Contract::getStatus, 2) // 已生效
                        .orderByDesc(Contract::getCreatedAt)
                        .last("LIMIT 1")
                );
                
                if (contract == null) {
                    log.warn("租客 {} 无有效合同，跳过", tenant.getId());
                    continue;
                }
                
                // 创建租金账单
                RentRecord record = new RentRecord();
                record.setTenantId(tenant.getId());
                record.setPropertyId(tenant.getPropertyId());
                record.setAmount(contract.getRentAmount());
                record.setPayMonth(currentMonth);
                record.setStatus(0); // 待支付
                record.setRemindCount(0);
                
                // 设置支付日期为当月最后一天
                LocalDate payDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                record.setPayDate(payDate);
                
                rentRecordMapper.insert(record);
                
                log.info("为租客 {} 生成 {} 月账单: amount={}", tenant.getId(), currentMonth, contract.getRentAmount());
                
                // 记录日志（通知功能暂缓）
                log.info("租客 {} 账单生成完成，待支付金额: {}", tenant.getId(), contract.getRentAmount());
                
            } catch (Exception e) {
                log.error("生成账单失败: tenantId={}, error={}", tenant.getId(), e.getMessage());
            }
        }
        
        log.info("月度租金账单生成完成");
    }

    /**
     * 每天检查逾期账单并更新状态
     * 规则：账单状态为"待支付"且超过付款日期的，标记为"已逾期"
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkOverdueBills() {
        log.info("开始检查逾期账单...");
        
        LocalDate today = LocalDate.now();
        
        // 查询所有待支付且已过期的账单
        List<RentRecord> overdueBills = rentRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentRecord>()
                        .eq(RentRecord::getStatus, 0) // 待支付
                        .lt(RentRecord::getPayDate, today) // 已过支付日期
        );
        
        for (RentRecord bill : overdueBills) {
            try {
                bill.setStatus(2); // 已逾期
                rentRecordMapper.updateById(bill);
                
                log.info("账单 {} 已逾期，状态已更新", bill.getId());
                
            } catch (Exception e) {
                log.error("更新逾期状态失败: billId={}, error={}", bill.getId(), e.getMessage());
            }
        }
        
        log.info("逾期账单检查完成，共处理 {} 条", overdueBills.size());
    }

    /**
     * 催租提醒定时任务 - D+0/D+2/D+3 三轮提醒
     * 规则：
     * - D+0: 租金到期日当天，租客收到第1次提醒
     * - D+2: 租金逾期第2天，租客收到第2次提醒
     * - D+3: 租金逾期第3天，租客收到第3次提醒 + 房东收到逾期通知
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void sendPaymentReminder() {
        log.info("开始发送租金催租提醒（D+0/D+2/D+3）...");
        
        LocalDate today = LocalDate.now();
        
        // 查询所有待支付或已逾期的账单
        List<RentRecord> bills = rentRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentRecord>()
                        .in(RentRecord::getStatus, 0, 2) // 待支付或已逾期
                        .le(RentRecord::getPayDate, today) // 到期日 <= 今天
        );
        
        int tenantReminderCount = 0;
        int landlordNotificationCount = 0;
        
        for (RentRecord bill : bills) {
            try {
                LocalDate payDate = bill.getPayDate();
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(payDate, today);
                int currentRemindCount = bill.getRemindCount() != null ? bill.getRemindCount() : 0;
                
                // D+0: 当天首次提醒
                if (daysOverdue == 0 && currentRemindCount == 0) {
                    bill.setRemindCount(1);
                    rentRecordMapper.updateById(bill);
                    // 发送通知给租客
                    notifyTenant(bill.getTenantId(), 1);
                    tenantReminderCount++;
                    log.info("账单 {} D+0 第1次提醒已发送", bill.getId());
                }
                // D+2: 逾期第2天，第二次提醒
                else if (daysOverdue >= 2 && currentRemindCount == 1) {
                    bill.setRemindCount(2);
                    rentRecordMapper.updateById(bill);
                    // 发送通知给租客
                    notifyTenant(bill.getTenantId(), 2);
                    tenantReminderCount++;
                    log.info("账单 {} D+2 第2次提醒已发送", bill.getId());
                }
                // D+3: 逾期第3天，第三次提醒 + 房东通知
                else if (daysOverdue >= 3 && currentRemindCount == 2) {
                    bill.setRemindCount(3);
                    rentRecordMapper.updateById(bill);
                    // 发送通知给租客
                    notifyTenant(bill.getTenantId(), 3);
                    tenantReminderCount++;
                    // 发送通知给房东
                    notifyLandlord(bill);
                    landlordNotificationCount++;
                    log.info("账单 {} D+3 第3次提醒+房东通知已发送", bill.getId());
                }
                
            } catch (Exception e) {
                log.error("处理提醒失败: billId={}, error={}", bill.getId(), e.getMessage());
            }
        }
        
        log.info("催租提醒发送完成: 租客提醒{}条, 房东通知{}条", tenantReminderCount, landlordNotificationCount);
    }
    
    /**
     * 通知租客
     */
    private void notifyTenant(Long tenantId, int round) {
        log.info("发送第{}次提醒给租客: tenantId={}", round, tenantId);
        // TODO: 调用通知服务发送消息
    }
    
    /**
     * 通知房东
     */
    private void notifyLandlord(RentRecord bill) {
        log.info("发送逾期通知给房东: billId={}", bill.getId());
        // TODO: 调用通知服务发送消息给房东
    }
}
