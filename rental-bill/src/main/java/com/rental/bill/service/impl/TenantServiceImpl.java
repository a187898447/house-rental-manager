package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.entity.Tenant;
import com.rental.bill.mapper.TenantMapper;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.TenantVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 租客服务实现
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantMapper tenantMapper;

    private static final List<Integer> CHECKOUT_ALLOWED_STATUSES = Arrays.asList(1);

    @Override
    @Transactional
    public Long checkIn(TenantCreateDTO dto) {
        Tenant tenant = new Tenant();
        tenant.setUserId(dto.getUserId());
        tenant.setPropertyId(dto.getPropertyId());
        tenant.setName(dto.getName());
        tenant.setPhone(dto.getPhone());
        tenant.setIdCard(dto.getIdCard());
        tenant.setLeaseStartDate(dto.getLeaseStartDate());
        tenant.setLeaseEndDate(dto.getLeaseEndDate());
        tenant.setEmergencyContact(dto.getEmergencyContact());
        tenant.setEmergencyPhone(dto.getEmergencyPhone());
        tenant.setRemark(dto.getRemark());
        tenant.setStatus(0); // 待入住
        
        tenantMapper.insert(tenant);
        return tenant.getId();
    }

    @Override
    public TenantVO getDetail(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租客不存在");
        }
        return convertToVO(tenant);
    }

    @Override
    public Page<TenantVO> getPropertyTenants(Long propertyId, Integer status, Integer page, Integer size) {
        Page<Tenant> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<Tenant>()
                .eq(Tenant::getPropertyId, propertyId)
                .eq(status != null, Tenant::getStatus, status)
                .orderByDesc(Tenant::getCreatedAt);
        
        Page<Tenant> result = tenantMapper.selectPage(pageParam, wrapper);
        
        Page<TenantVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<TenantVO> getOwnerTenants(Long ownerId, Integer status, Integer page, Integer size) {
        // TODO: 需要关联property表查询ownerId
        Page<Tenant> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<Tenant>()
                .eq(status != null, Tenant::getStatus, status)
                .orderByDesc(Tenant::getCreatedAt);
        
        Page<Tenant> result = tenantMapper.selectPage(pageParam, wrapper);
        
        Page<TenantVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean checkOut(Long id, String remark) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租客不存在");
        }
        if (!CHECKOUT_ALLOWED_STATUSES.contains(tenant.getStatus())) {
            throw new BusinessException("只有已入住的租客可以办理退租");
        }
        
        tenant.setStatus(2); // 已退租
        if (remark != null) {
            tenant.setRemark(remark);
        }
        
        return tenantMapper.updateById(tenant) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租客不存在");
        }
        if (tenant.getStatus() == 1) {
            throw new BusinessException("已入住的租客无法删除");
        }
        
        return tenantMapper.deleteById(id) > 0;
    }

    private TenantVO convertToVO(Tenant tenant) {
        TenantVO vo = new TenantVO();
        vo.setId(tenant.getId());
        vo.setUserId(tenant.getUserId());
        vo.setPropertyId(tenant.getPropertyId());
        vo.setName(tenant.getName());
        vo.setPhone(tenant.getPhone());
        vo.setIdCard(tenant.getIdCard());
        vo.setLeaseStartDate(tenant.getLeaseStartDate());
        vo.setLeaseEndDate(tenant.getLeaseEndDate());
        vo.setEmergencyContact(tenant.getEmergencyContact());
        vo.setEmergencyPhone(tenant.getEmergencyPhone());
        vo.setStatus(tenant.getStatus());
        vo.setStatusName(getStatusName(tenant.getStatus()));
        vo.setRemark(tenant.getRemark());
        vo.setCreatedAt(tenant.getCreatedAt());
        vo.setUpdatedAt(tenant.getUpdatedAt());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待入住";
            case 1 -> "已入住";
            case 2 -> "已退租";
            default -> "未知";
        };
    }
}
