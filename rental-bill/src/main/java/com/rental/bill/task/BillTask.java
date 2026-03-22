package com.rental.bill.task;

import com.rental.bill.entity.RentRecord;
import com.rental.bill.entity.Tenant;
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
                
                // TODO: 从合同表获取租金金额，这里简化处理
                // 实际应查询contract表获取rentAmount
                
                log.info("为租客 {} 生成 {} 月账单", tenant.getId(), currentMonth);
                
                // TODO: 发送通知给租客
                // if (notifyClient != null) {
                //     notifyClient.notifyBillCreated(tenant.getUserId(), propertyName, amount, currentMonth);
                // }
                
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
                
                // TODO: 发送逾期通知
                log.info("账单 {} 已逾期", bill.getId());
                
            } catch (Exception e) {
                log.error("更新逾期状态失败: billId={}, error={}", bill.getId(), e.getMessage());
            }
        }
        
        log.info("逾期账单检查完成，共处理 {} 条", overdueBills.size());
    }

    /**
     * 每周发送租金提醒
     * 规则：对已逾期和即将到期的租客发送提醒
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void sendPaymentReminder() {
        log.info("开始发送租金提醒...");
        
        // 查找已逾期和即将逾期(3天内)的账单
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(3);
        
        List<RentRecord> billsToRemind = rentRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentRecord>()
                        .in(RentRecord::getStatus, 0, 2) // 待支付或已逾期
                        .le(RentRecord::getPayDate, warningDate)
        );
        
        for (RentRecord bill : billsToRemind) {
            try {
                // 调用催租提醒接口
                bill.setRemindCount(bill.getRemindCount() + 1);
                rentRecordMapper.updateById(bill);
                
                // TODO: 发送微信模板消息
                log.info("发送租金提醒: billId={}, remindCount={}", bill.getId(), bill.getRemindCount());
                
            } catch (Exception e) {
                log.error("发送提醒失败: billId={}, error={}", bill.getId(), e.getMessage());
            }
        }
        
        log.info("租金提醒发送完成，共处理 {} 条", billsToRemind.size());
    }
}
