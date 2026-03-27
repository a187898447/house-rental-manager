package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.entity.Deposit;
import com.rental.bill.mapper.DepositMapper;
import com.rental.bill.service.DepositService;
import com.rental.bill.vo.DepositVO;
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
        log.info("押金创建成功: id={}, tenantId={}, amount={}", deposit.getId(), tenantId, amount);
        return deposit.getId();
    }

    @Override
    @Transactional
    public Long createDeposit(Long propertyId, Long tenantId, BigDecimal amount) {
        Deposit deposit = new Deposit();
        deposit.setTenantId(tenantId);
        deposit.setPropertyId(propertyId);
        deposit.setAmount(amount);
        deposit.setStatus(0); // 待缴纳
        depositMapper.insert(deposit);
        log.info("押金创建成功(简化): id={}, propertyId={}, tenantId={}, amount={}", 
                deposit.getId(), propertyId, tenantId, amount);
        return deposit.getId();
    }

    @Override
    public DepositVO getDetail(Long id) {
        Deposit deposit = depositMapper.selectById(id);
        if (deposit == null) {
            log.warn("押金记录不存在: id={}", id);
            throw new BusinessException("押金记录不存在");
        }
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
        // 通过 property 表关联过滤 ownerId
        Page<Deposit> p = new Page<>(page, size);
        
        // 使用子查询：通过 property 表获取该房东的房源ID列表
        LambdaQueryWrapper<Deposit> w = new LambdaQueryWrapper<Deposit>()
                .inSql(Deposit::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
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
        if (deposit == null) {
            log.warn("押金记录不存在: id={}", id);
            throw new BusinessException("押金记录不存在");
        }
        if (deposit.getStatus() != 0) {
            log.warn("押金状态不正确: id={}, status={}", id, deposit.getStatus());
            throw new BusinessException("状态不正确");
        }
        deposit.setStatus(1); // 已缴纳
        deposit.setPayDate(LocalDate.now());
        deposit.setUpdatedAt(LocalDateTime.now());
        boolean result = depositMapper.updateById(deposit) > 0;
        log.info("押金缴纳成功: id={}, payMethod={}", id, payMethod);
        return result;
    }

    @Override
    @Transactional
    public boolean refund(Long id, BigDecimal refundAmount) {
        Deposit deposit = depositMapper.selectById(id);
        if (deposit == null) {
            log.warn("押金记录不存在: id={}", id);
            throw new BusinessException("押金记录不存在");
        }
        if (deposit.getStatus() != 1) {
            log.warn("押金状态不正确，无法退还: id={}, status={}", id, deposit.getStatus());
            throw new BusinessException("只有已缴纳的押金可退还");
        }
        deposit.setStatus(3); // 已退还
        deposit.setRefundAmount(refundAmount);
        deposit.setRefundDate(LocalDate.now());
        deposit.setUpdatedAt(LocalDateTime.now());
        boolean result = depositMapper.updateById(deposit) > 0;
        log.info("押金退还成功: id={}, refundAmount={}", id, refundAmount);
        return result;
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
    
    @Override
    public BigDecimal getTotalDeposits(Long ownerId) {
        // 查询该房东所有已缴纳的押金总额
        BigDecimal total = depositMapper.selectSumByOwnerAndStatus(ownerId, 1);
        return total != null ? total : BigDecimal.ZERO;
    }
}
