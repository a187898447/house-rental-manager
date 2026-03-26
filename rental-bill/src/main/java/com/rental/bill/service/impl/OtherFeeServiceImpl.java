package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.OtherFeeCreateDTO;
import com.rental.bill.entity.OtherFee;
import com.rental.bill.mapper.OtherFeeMapper;
import com.rental.bill.service.OtherFeeService;
import com.rental.bill.vo.OtherFeeVO;
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
public class OtherFeeServiceImpl implements OtherFeeService {

    private final OtherFeeMapper otherFeeMapper;

    @Override
    @Transactional
    public Long create(OtherFeeCreateDTO dto) {
        OtherFee fee = new OtherFee();
        fee.setTenantId(dto.getTenantId());
        fee.setPropertyId(dto.getPropertyId());
        fee.setFeeType(dto.getFeeType());
        fee.setAmount(dto.getAmount());
        fee.setBillMonth(dto.getBillMonth());
        fee.setRemark(dto.getRemark());
        fee.setStatus(0); // 待支付
        otherFeeMapper.insert(fee);
        log.info("其他费用创建成功: id={}, tenantId={}, amount={}", fee.getId(), dto.getTenantId(), dto.getAmount());
        return fee.getId();
    }

    @Override
    public OtherFeeVO getDetail(Long id) {
        OtherFee fee = otherFeeMapper.selectById(id);
        if (fee == null) {
            log.warn("费用记录不存在: id={}", id);
            throw new BusinessException("费用记录不存在");
        }
        return convertToVO(fee);
    }

    @Override
    public Page<OtherFeeVO> getTenantFees(Long tenantId, Integer status, Integer page, Integer size) {
        Page<OtherFee> p = new Page<>(page, size);
        LambdaQueryWrapper<OtherFee> w = new LambdaQueryWrapper<OtherFee>()
                .eq(OtherFee::getTenantId, tenantId)
                .eq(status != null, OtherFee::getStatus, status)
                .orderByDesc(OtherFee::getCreatedAt);
        Page<OtherFee> result = otherFeeMapper.selectPage(p, w);
        Page<OtherFeeVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public Page<OtherFeeVO> getOwnerFees(Long ownerId, Integer status, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<OtherFee> p = new Page<>(page, size);
        LambdaQueryWrapper<OtherFee> w = new LambdaQueryWrapper<OtherFee>()
                .inSql(OtherFee::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .eq(status != null, OtherFee::getStatus, status)
                .orderByDesc(OtherFee::getCreatedAt);
        Page<OtherFee> result = otherFeeMapper.selectPage(p, w);
        Page<OtherFeeVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    @Transactional
    public boolean pay(Long id) {
        OtherFee fee = otherFeeMapper.selectById(id);
        if (fee == null) {
            log.warn("费用记录不存在: id={}", id);
            throw new BusinessException("费用记录不存在");
        }
        if (fee.getStatus() != 0) {
            log.warn("费用状态不正确: id={}, status={}", id, fee.getStatus());
            throw new BusinessException("状态不正确");
        }
        fee.setStatus(1); // 已支付
        fee.setPayDate(LocalDate.now());
        fee.setUpdatedAt(LocalDateTime.now());
        boolean result = otherFeeMapper.updateById(fee) > 0;
        log.info("其他费用支付成功: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        OtherFee fee = otherFeeMapper.selectById(id);
        if (fee == null) {
            log.warn("费用记录不存在: id={}", id);
            throw new BusinessException("费用记录不存在");
        }
        if (fee.getStatus() == 1) {
            log.warn("已支付的费用不能删除: id={}", id);
            throw new BusinessException("已支付的费用不能删除");
        }
        boolean result = otherFeeMapper.deleteById(id) > 0;
        log.info("其他费用删除成功: id={}", id);
        return result;
    }

    private OtherFeeVO convertToVO(OtherFee f) {
        OtherFeeVO vo = new OtherFeeVO();
        vo.setId(f.getId());
        vo.setTenantId(f.getTenantId());
        vo.setPropertyId(f.getPropertyId());
        vo.setFeeType(f.getFeeType());
        vo.setFeeTypeName(getFeeTypeName(f.getFeeType()));
        vo.setAmount(f.getAmount());
        vo.setBillMonth(f.getBillMonth());
        vo.setStatus(f.getStatus());
        vo.setStatusName(getStatusName(f.getStatus()));
        vo.setPayDate(f.getPayDate());
        vo.setRemark(f.getRemark());
        return vo;
    }

    private String getFeeTypeName(String feeType) {
        return switch (feeType) {
            case "parking" -> "停车费";
            case "network" -> "网络费";
            case "furniture" -> "家具维修";
            case "cleaning" -> "清洁费";
            case "other" -> "其他";
            default -> "未知";
        };
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            default -> "未知";
        };
    }
}
