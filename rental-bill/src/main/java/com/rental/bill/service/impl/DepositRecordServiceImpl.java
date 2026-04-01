package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.bill.entity.DepositRecord;
import com.rental.bill.mapper.DepositRecordMapper;
import com.rental.bill.service.DepositRecordService;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.user.entity.Tenant;
import com.rental.user.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 押金记录服务实现类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepositRecordServiceImpl extends ServiceImpl<DepositRecordMapper, DepositRecord> implements DepositRecordService {

    private final TenantMapper tenantMapper;
    private final PropertyMapper propertyMapper;

    /**
     * 未支付状态
     */
    private static final Integer STATUS_UNPAID = 0;

    /**
     * 已支付状态
     */
    private static final Integer STATUS_PAID = 1;

    /**
     * 已退还状态
     */
    private static final Integer STATUS_REFUNDED = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long collectDeposit(Long tenantId, Long propertyId, BigDecimal amount) {
        log.info("收取押金：tenantId={}, propertyId={}, amount={}", tenantId, propertyId, amount);

        // 1. 检查租客是否存在
        Tenant tenant = tenantMapper.selectById(tenantId);
        if (tenant == null) {
            throw new RuntimeException("租客不存在");
        }

        // 2. 检查房源是否存在
        Property property = propertyMapper.selectById(propertyId);
        if (property == null || property.getDeleted() == 1) {
            throw new RuntimeException("房源不存在");
        }

        // 3. 检查是否已有押金记录
        LambdaQueryWrapper<DepositRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepositRecord::getTenantId, tenantId)
                .in(DepositRecord::getStatus, STATUS_UNPAID, STATUS_PAID);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("该租客已有押金记录");
        }

        // 4. 创建押金记录
        DepositRecord record = new DepositRecord();
        record.setTenantId(tenantId);
        record.setPropertyId(propertyId);
        record.setLandlordId(property.getCreateBy());
        record.setAmount(amount);
        record.setPaidDate(LocalDate.now());
        record.setStatus(STATUS_PAID);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        baseMapper.insert(record);
        log.info("押金收取成功：recordId={}", record.getId());
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundDeposit(Long recordId, LocalDate refundDate) {
        log.info("退还押金：recordId={}, refundDate={}", recordId, refundDate);

        DepositRecord record = baseMapper.selectById(recordId);
        if (record == null) {
            throw new RuntimeException("押金记录不存在");
        }

        if (record.getStatus() == STATUS_REFUNDED) {
            throw new RuntimeException("押金已退还");
        }

        record.setStatus(STATUS_REFUNDED);
        record.setRefundDate(refundDate);
        record.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(record);

        log.info("押金退还成功：recordId={}", recordId);
    }

    @Override
    public DepositRecord getByTenantId(Long tenantId) {
        LambdaQueryWrapper<DepositRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepositRecord::getTenantId, tenantId)
                .in(DepositRecord::getStatus, STATUS_PAID, STATUS_REFUNDED)
                .orderByDesc(DepositRecord::getCreateTime)
                .last("LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }
}
