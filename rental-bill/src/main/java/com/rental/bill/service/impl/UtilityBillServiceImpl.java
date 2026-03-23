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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        bill.setElectricityReading(dto.getElectricityReading());
        bill.setWaterAmount(dto.getWaterAmount());
        bill.setElectricityAmount(dto.getElectricityAmount());
        bill.setRemark(dto.getRemark());
        bill.setStatus(0); // 待支付
        
        utilityBillMapper.insert(bill);
        return bill.getId();
    }

    @Override
    public UtilityBillVO getDetail(Long id) {
        UtilityBill bill = utilityBillMapper.selectById(id);
        if (bill == null) throw new BusinessException("水电账单不存在");
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
        Page<UtilityBill> p = new Page<>(page, size);
        LambdaQueryWrapper<UtilityBill> w = new LambdaQueryWrapper<UtilityBill>()
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
        if (bill == null) throw new BusinessException("水电账单不存在");
        if (bill.getStatus() != 0) throw new BusinessException("状态不正确");
        bill.setStatus(1); // 已支付
        bill.setPayDate(LocalDateTime.now());
        return utilityBillMapper.updateById(bill) > 0;
    }

    private UtilityBillVO convertToVO(UtilityBill b) {
        UtilityBillVO vo = new UtilityBillVO();
        vo.setId(b.getId());
        vo.setTenantId(b.getTenantId());
        vo.setPropertyId(b.getPropertyId());
        vo.setBillMonth(b.getBillMonth());
        vo.setWaterAmount(b.getWaterAmount());
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
