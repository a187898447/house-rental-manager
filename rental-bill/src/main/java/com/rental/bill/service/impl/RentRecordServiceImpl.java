package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordCreateDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.service.RentRecordService;
import com.rental.bill.vo.RentRecordVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租金账单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentRecordServiceImpl implements RentRecordService {

    private final RentRecordMapper rentRecordMapper;

    private static final List<Integer> PAYABLE_STATUSES = Arrays.asList(0, 2);

    @Override
    @Transactional
    public Long create(RentRecordCreateDTO dto) {
        RentRecord record = new RentRecord();
        record.setTenantId(dto.getTenantId());
        record.setPropertyId(dto.getPropertyId());
        record.setAmount(dto.getAmount());
        record.setDays(dto.getDays());
        record.setDailyRate(dto.getDailyRate());
        record.setPayMonth(dto.getPayMonth());
        record.setRemark(dto.getRemark());
        record.setStatus(0); // 待支付
        record.setRemindCount(0);
        
        rentRecordMapper.insert(record);
        log.info("租金账单创建成功: id={}, tenantId={}, amount={}", record.getId(), dto.getTenantId(), dto.getAmount());
        return record.getId();
    }

    @Override
    public RentRecordVO getDetail(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        return convertToVO(record);
    }

    @Override
    public Page<RentRecordVO> getOwnerBills(Long ownerId, Long propertyId, Integer status, String month, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<RentRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<RentRecord>()
                .inSql(RentRecord::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .eq(propertyId != null, RentRecord::getPropertyId, propertyId)
                .eq(status != null, RentRecord::getStatus, status)
                .eq(month != null, RentRecord::getPayMonth, month)
                .orderByDesc(RentRecord::getCreatedAt);
        
        Page<RentRecord> result = rentRecordMapper.selectPage(pageParam, wrapper);
        
        Page<RentRecordVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<RentRecordVO> getTenantBills(Long tenantId, Integer status, Integer page, Integer size) {
        Page<RentRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<RentRecord>()
                .eq(RentRecord::getTenantId, tenantId)
                .eq(status != null, RentRecord::getStatus, status)
                .orderByDesc(RentRecord::getCreatedAt);
        
        Page<RentRecord> result = rentRecordMapper.selectPage(pageParam, wrapper);
        
        Page<RentRecordVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean markPaid(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        if (!PAYABLE_STATUSES.contains(record.getStatus())) {
            log.warn("账单状态不正确: id={}, status={}", id, record.getStatus());
            throw new BusinessException("账单状态不正确");
        }
        
        record.setStatus(1); // 已支付
        record.setPayDate(LocalDate.now());
        
        boolean result = rentRecordMapper.updateById(record) > 0;
        log.info("租金账单支付成功: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean sendReminder(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        
        // 催租提醒逻辑：D+0 → D+2 → D+3
        LocalDate today = LocalDate.now();
        LocalDate payDate = record.getPayDate();
        if (payDate == null) {
            throw new BusinessException("账单支付日期未设置");
        }
        
        long overdueDays = ChronoUnit.DAYS.between(payDate, today);
        int currentStatus = record.getRemindStatus() != null ? record.getRemindStatus() : 0;
        
        // 根据逾期天数判断提醒阶段
        if (overdueDays <= 0) {
            // D+0: 首次提醒（到期日当天）
            if (currentStatus < 1) {
                record.setRemindStatus(1);
                log.info("账单 {} 处于到期日，发送 D+0 提醒", id);
            }
        } else if (overdueDays <= 2) {
            // D+2: 逾期2天内
            if (currentStatus < 2) {
                record.setRemindStatus(2);
                log.info("账单 {} 逾期 {} 天，发送 D+2 提醒", id, overdueDays);
            }
        } else {
            // D+3: 逾期3天及以上
            if (currentStatus < 3) {
                record.setRemindStatus(3);
                log.info("账单 {} 逾期 {} 天，发送 D+3 提醒（租客+房东）", id, overdueDays);
                // TODO: 通知房东（等通知服务对接后实现）
            }
        }
        
        record.setRemindCount(record.getRemindCount() + 1);
        record.setLastRemindDate(today);
        
        log.info("租金账单催缴: id={}, remindCount={}, remindStatus={}, overdueDays={}", 
                id, record.getRemindCount(), record.getRemindStatus(), overdueDays);
        
        return rentRecordMapper.updateById(record) > 0;
    }

    @Override
    public List<Long> getBillsNeedReminder() {
        // 查找所有待支付和已逾期的账单，且提醒状态未完成的
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<RentRecord>()
                .in(RentRecord::getStatus, 0, 2) // 待支付或已逾期
                .lt(RentRecord::getRemindStatus, 3); // remindStatus < 3 (还未完成 D+3 提醒)
        
        List<RentRecord> records = rentRecordMapper.selectList(wrapper);
        log.info("需要催租提醒的账单数量: {}", records.size());
        
        return records.stream().map(RentRecord::getId).toList();
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        if (record.getStatus() != 0) {
            log.warn("账单状态不正确，无法取消: id={}, status={}", id, record.getStatus());
            throw new BusinessException("只有待支付的账单可以取消");
        }
        
        record.setStatus(3); // 已取消
        
        boolean result = rentRecordMapper.updateById(record) > 0;
        log.info("租金账单取消成功: id={}", id);
        return result;
    }

    private RentRecordVO convertToVO(RentRecord record) {
        RentRecordVO vo = new RentRecordVO();
        vo.setId(record.getId());
        vo.setTenantId(record.getTenantId());
        vo.setPropertyId(record.getPropertyId());
        vo.setAmount(record.getAmount());
        vo.setDays(record.getDays());
        vo.setDailyRate(record.getDailyRate());
        vo.setPayMonth(record.getPayMonth());
        vo.setPayDate(record.getPayDate());
        vo.setStatus(record.getStatus());
        vo.setStatusName(getStatusName(record.getStatus()));
        vo.setRemindCount(record.getRemindCount());
        vo.setLastRemindDate(record.getLastRemindDate());
        vo.setRemindStatus(record.getRemindStatus());
        vo.setRemindStatusName(getRemindStatusName(record.getRemindStatus()));
        vo.setRemark(record.getRemark());
        vo.setCreatedAt(record.getCreatedAt());
        vo.setUpdatedAt(record.getUpdatedAt());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已逾期";
            case 3 -> "已取消";
            default -> "未知";
        };
    }
    
    private String getRemindStatusName(Integer status) {
        if (status == null) return "未提醒";
        return switch (status) {
            case 0 -> "未提醒";
            case 1 -> "D+0已提醒";
            case 2 -> "D+2已提醒";
            case 3 -> "D+3已提醒";
            default -> "未知";
        };
    }
    
    @Override
    public Map<String, Object> getOwnerStats(Long ownerId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计总金额
        BigDecimal totalAmount = rentRecordMapper.selectSumByOwner(ownerId);
        stats.put("totalAmount", totalAmount != null ? totalAmount : BigDecimal.ZERO);
        
        // 统计已支付金额
        BigDecimal paidAmount = rentRecordMapper.selectSumByOwnerAndStatus(ownerId, 1);
        stats.put("paidAmount", paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        // 统计待支付金额
        BigDecimal pendingAmount = rentRecordMapper.selectSumByOwnerAndStatus(ownerId, 0);
        stats.put("pendingAmount", pendingAmount != null ? pendingAmount : BigDecimal.ZERO);
        
        // 统计待支付数量
        long pendingCount = rentRecordMapper.selectCount(
                new LambdaQueryWrapper<RentRecord>()
                        .eq(RentRecord::getOwnerId, ownerId)
                        .in(RentRecord::getStatus, 0, 2));
        stats.put("pendingCount", pendingCount);
        
        log.info("获取房东租金统计: ownerId={}, stats={}", ownerId, stats);
        return stats;
    }
}
