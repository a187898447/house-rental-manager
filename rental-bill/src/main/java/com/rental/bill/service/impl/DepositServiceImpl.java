package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.entity.Deposit;
import com.rental.bill.mapper.DepositMapper;
import com.rental.bill.service.DepositService;
import com.rental.bill.vo.DepositVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositMapper depositMapper;

    @Override
    @Transactional
    public Long create(Long tenantId, Long propertyId, Long contractId, BigDecimal amount) {
        Deposit deposit = new Deposit();
        deposit.setTenantId(tenantId);
        deposit.setPropertyId(propertyId);
        deposit.setContractId(contractId);
        deposit.setAmount(amount);
        deposit.setStatus(0); // 待缴纳
        depositMapper.insert(deposit);
        return deposit.getId();
    }

    @Override
    public DepositVO getDetail(Long id) {
        Deposit deposit = depositMapper.selectById(id);
        if (deposit == null) throw new BusinessException("押金记录不存在");
        return convertToVO(deposit);
    }

    @Override
    public Page<DepositVO> getTenantDeposits(Long tenantId, Integer status, Integer page, Integer size) {
        Page<Deposit> p = new Page<>(page, size);
        LambdaQueryWrapper<Deposit> w = new LambdaQueryWrapper<Deposit>()
                .eq(Deposit::getTenantId, tenantId)
                .eq(status != null, Deposit::getStatus, status)
                .orderByDesc(Deposit::getCreatedAt);
        Page<Deposit> result = depositMapper.selectPage(p, w);
        Page<DepositVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public Page<DepositVO> getOwnerDeposits(Long ownerId, Integer status, Integer page, Integer size) {
        // TODO: 关联property表过滤ownerId
        Page<Deposit> p = new Page<>(page, size);
        LambdaQueryWrapper<Deposit> w = new LambdaQueryWrapper<Deposit>()
                .eq(status != null, Deposit::getStatus, status)
                .orderByDesc(Deposit::getCreatedAt);
        Page<Deposit> result = depositMapper.selectPage(p, w);
        Page<DepositVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    @Transactional
    public boolean pay(Long id, String payMethod) {
        Deposit deposit = depositMapper.selectById(id);
        if (deposit == null) throw new BusinessException("押金记录不存在");
        if (deposit.getStatus() != 0) throw new BusinessException("状态不正确");
        deposit.setStatus(1); // 已缴纳
        deposit.setPayDate(LocalDateTime.now());
        return depositMapper.updateById(deposit) > 0;
    }

    @Override
    @Transactional
    public boolean refund(Long id, BigDecimal refundAmount) {
        Deposit deposit = depositMapper.selectById(id);
        if (deposit == null) throw new BusinessException("押金记录不存在");
        if (deposit.getStatus() != 1) throw new BusinessException("只有已缴纳的押金可退还");
        deposit.setStatus(3); // 已退还
        deposit.setRefundAmount(refundAmount);
        deposit.setRefundDate(LocalDateTime.now());
        return depositMapper.updateById(deposit) > 0;
    }

    private DepositVO convertToVO(Deposit d) {
        DepositVO vo = new DepositVO();
        vo.setId(d.getId());
        vo.setTenantId(d.getTenantId());
        vo.setPropertyId(d.getPropertyId());
        vo.setContractId(d.getContractId());
        vo.setAmount(d.getAmount());
        vo.setStatus(d.getStatus());
        vo.setStatusName(getStatusName(d.getStatus()));
        vo.setPayDate(d.getPayDate());
        vo.setRefundAmount(d.getRefundAmount());
        vo.setRefundDate(d.getRefundDate());
        vo.setRemark(d.getRemark());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待缴纳";
            case 1 -> "已缴纳";
            case 2 -> "待退还";
            case 3 -> "已退还";
            case 4 -> "已扣除";
            default -> "未知";
        };
    }
}
