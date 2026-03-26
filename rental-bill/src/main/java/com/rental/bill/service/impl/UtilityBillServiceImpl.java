package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.UtilityBillCreateDTO;
import com.rental.bill.entity.UtilityBill;
import com.rental.bill.mapper.UtilityBillMapper;
import com.rental.bill.service.UtilityBillService;
import com.rental.bill.vo.UtilityBillVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityBillServiceImpl implements UtilityBillService {

    private final UtilityBillMapper utilityBillMapper;

    @Override
    @Transactional
    public Long create(UtilityBillCreateDTO dto) {
        UtilityBill bill = new UtilityBill();
        bill.setTenantId(dto.getTenantId());
        bill.setPropertyId(dto.getPropertyId());
        bill.setBillMonth(dto.getBillMonth());
        bill.setWaterReading(dto.getWaterReading());
        bill.setWaterReadingCurrent(dto.getWaterReadingCurrent());
        bill.setWaterAmount(dto.getWaterAmount());
        bill.setElectricityReading(dto.getElectricityReading());
        bill.setElectricityReadingCurrent(dto.getElectricityReadingCurrent());
        bill.setElectricityAmount(dto.getElectricityAmount());
        bill.setSource(dto.getSource() != null ? dto.getSource() : 0);
        bill.setStatus(0);
        utilityBillMapper.insert(bill);
        log.info("水电账单创建成功: id={}, tenantId={}, month={}", bill.getId(), dto.getTenantId(), dto.getBillMonth());
        return bill.getId();
    }

    @Override
    public UtilityBillVO getDetail(Long id) {
        UtilityBill bill = utilityBillMapper.selectById(id);
        if (bill == null) {
            log.warn("水电账单不存在: id={}", id);
            throw new BusinessException("水电账单不存在");
        }
        return convertToVO(bill);
    }

    @Override
    public Page<UtilityBillVO> getTenantBills(Long tenantId, Integer status, Integer page, Integer size) {
        Page<UtilityBill> p = new Page<>(page, size);
        LambdaQueryWrapper<UtilityBill> w = new LambdaQueryWrapper<UtilityBill>()
                .eq(UtilityBill::getTenantId, tenantId)
                .eq(status != null, UtilityBill::getStatus, status)
                .orderByDesc(UtilityBill::getCreatedAt);
        Page<UtilityBill> result = utilityBillMapper.selectPage(p, w);
        Page<UtilityBillVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public Page<UtilityBillVO> getOwnerBills(Long ownerId, Integer status, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<UtilityBill> p = new Page<>(page, size);
        LambdaQueryWrapper<UtilityBill> w = new LambdaQueryWrapper<UtilityBill>()
                .inSql(UtilityBill::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .eq(status != null, UtilityBill::getStatus, status)
                .orderByDesc(UtilityBill::getCreatedAt);
        Page<UtilityBill> result = utilityBillMapper.selectPage(p, w);
        Page<UtilityBillVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    @Transactional
    public boolean pay(Long id) {
        UtilityBill bill = utilityBillMapper.selectById(id);
        if (bill == null) {
            log.warn("水电账单不存在: id={}", id);
            throw new BusinessException("水电账单不存在");
        }
        if (bill.getStatus() != 0) {
            log.warn("水电账单状态不正确: id={}, status={}", id, bill.getStatus());
            throw new BusinessException("状态不正确");
        }
        bill.setStatus(1); // 已支付
        bill.setPayDate(LocalDate.now());
        bill.setUpdatedAt(LocalDateTime.now());
        boolean result = utilityBillMapper.updateById(bill) > 0;
        log.info("水电账单支付成功: id={}", id);
        return result;
    }

    private UtilityBillVO convertToVO(UtilityBill b) {
        UtilityBillVO vo = new UtilityBillVO();
        vo.setId(b.getId());
        vo.setTenantId(b.getTenantId());
        vo.setPropertyId(b.getPropertyId());
        vo.setBillMonth(b.getBillMonth());
        vo.setWaterReading(b.getWaterReading());
        vo.setWaterReadingCurrent(b.getWaterReadingCurrent());
        vo.setWaterAmount(b.getWaterAmount());
        vo.setElectricityReading(b.getElectricityReading());
        vo.setElectricityReadingCurrent(b.getElectricityReadingCurrent());
        vo.setElectricityAmount(b.getElectricityAmount());
        vo.setStatus(b.getStatus());
        vo.setStatusName(getStatusName(b.getStatus()));
        vo.setPayDate(b.getPayDate());
        vo.setRemark(b.getRemark());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已逾期";
            default -> "未知";
        };
    }
}
