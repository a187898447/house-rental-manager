package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.ContractCreateDTO;
import com.rental.bill.dto.ContractUpdateDTO;
import com.rental.bill.entity.Contract;
import com.rental.bill.mapper.ContractMapper;
import com.rental.bill.service.ContractService;
import com.rental.bill.vo.ContractVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 合同服务实现
 */
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractMapper contractMapper;

    private static final List<Integer> VALID_STATUSES = Arrays.asList(0, 1, 2, 3, 4);
    private static final List<Integer> TERMINABLE_STATUSES = Arrays.asList(1, 2);

    @Override
    @Transactional
    public Long create(ContractCreateDTO dto) {
        Contract contract = new Contract();
        contract.setTenantId(dto.getTenantId());
        contract.setPropertyId(dto.getPropertyId());
        contract.setRentAmount(dto.getRentAmount());
        contract.setDepositAmount(dto.getDepositAmount());
        contract.setWaterFee(dto.getWaterFee());
        contract.setElectricityFee(dto.getElectricityFee());
        contract.setOtherFees(dto.getOtherFees());
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setStatus(0); // 待签署
        
        // TODO: 获取ownerId from property
        contract.setOwnerId(1L);
        
        contractMapper.insert(contract);
        return contract.getId();
    }

    @Override
    public ContractVO getDetail(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        return convertToVO(contract);
    }

    @Override
    public Page<ContractVO> getOwnerContracts(Long ownerId, Integer page, Integer size) {
        Page<Contract> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                .eq(Contract::getOwnerId, ownerId)
                .orderByDesc(Contract::getCreatedAt);
        
        Page<Contract> result = contractMapper.selectPage(pageParam, wrapper);
        
        Page<ContractVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<ContractVO> getTenantContracts(Long tenantId, Integer page, Integer size) {
        Page<Contract> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                .eq(Contract::getTenantId, tenantId)
                .orderByDesc(Contract::getCreatedAt);
        
        Page<Contract> result = contractMapper.selectPage(pageParam, wrapper);
        
        Page<ContractVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean update(Long id, ContractUpdateDTO dto) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        
        if (dto.getRentAmount() != null) {
            contract.setRentAmount(dto.getRentAmount());
        }
        if (dto.getDepositAmount() != null) {
            contract.setDepositAmount(dto.getDepositAmount());
        }
        if (dto.getWaterFee() != null) {
            contract.setWaterFee(dto.getWaterFee());
        }
        if (dto.getElectricityFee() != null) {
            contract.setElectricityFee(dto.getElectricityFee());
        }
        if (dto.getOtherFees() != null) {
            contract.setOtherFees(dto.getOtherFees());
        }
        if (dto.getStartDate() != null) {
            contract.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            contract.setEndDate(dto.getEndDate());
        }
        
        return contractMapper.updateById(contract) > 0;
    }

    @Override
    @Transactional
    public boolean sign(Long id, String signUrl) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        if (contract.getStatus() != 0) {
            throw new BusinessException("合同状态不正确，无法签署");
        }
        
        contract.setSignUrl(signUrl);
        contract.setStatus(1); // 已签署
        
        return contractMapper.updateById(contract) > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new BusinessException("无效的合同状态");
        }
        
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        
        contract.setStatus(status);
        
        // 如果状态变为已生效，检查是否到期
        if (status == 2 && contract.getEndDate() != null) {
            // TODO: 定时任务检查到期
        }
        
        return contractMapper.updateById(contract) > 0;
    }

    @Override
    @Transactional
    public boolean terminate(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        if (!TERMINABLE_STATUSES.contains(contract.getStatus())) {
            throw new BusinessException("当前状态无法解除合同");
        }
        
        contract.setStatus(4); // 已解除
        return contractMapper.updateById(contract) > 0;
    }

    private ContractVO convertToVO(Contract contract) {
        ContractVO vo = new ContractVO();
        vo.setId(contract.getId());
        vo.setTenantId(contract.getTenantId());
        vo.setPropertyId(contract.getPropertyId());
        vo.setOwnerId(contract.getOwnerId());
        vo.setRentAmount(contract.getRentAmount());
        vo.setDepositAmount(contract.getDepositAmount());
        vo.setWaterFee(contract.getWaterFee());
        vo.setElectricityFee(contract.getElectricityFee());
        vo.setOtherFees(contract.getOtherFees());
        vo.setStartDate(contract.getStartDate());
        vo.setEndDate(contract.getEndDate());
        vo.setSignUrl(contract.getSignUrl());
        vo.setStatus(contract.getStatus());
        vo.setStatusName(getStatusName(contract.getStatus()));
        vo.setCreatedAt(contract.getCreatedAt());
        vo.setUpdatedAt(contract.getUpdatedAt());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待签署";
            case 1 -> "已签署";
            case 2 -> "已生效";
            case 3 -> "已到期";
            case 4 -> "已解除";
            default -> "未知";
        };
    }
}
