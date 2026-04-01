package com.rental.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.user.dto.TenantDTO;
import com.rental.user.entity.Tenant;
import com.rental.user.entity.User;
import com.rental.user.mapper.TenantMapper;
import com.rental.user.mapper.UserMapper;
import com.rental.user.service.TenantService;
import com.rental.user.vo.TenantVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租客服务实现类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final PropertyMapper propertyMapper;
    private final UserMapper userMapper;

    /**
     * 在住状态
     */
    private static final Integer STATUS_LIVING = 0;

    /**
     * 已退租状态
     */
    private static final Integer STATUS_CHECKED_OUT = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkIn(TenantDTO dto) {
        log.info("入住登记：name={}, propertyId={}", dto.getName(), dto.getPropertyId());

        // 1. 检查房源是否存在
        Property property = propertyMapper.selectById(dto.getPropertyId());
        if (property == null || property.getDeleted() == 1) {
            throw new BusinessException(404, "房源不存在");
        }

        // 2. 检查房源是否已出租
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getPropertyId, dto.getPropertyId())
                .eq(Tenant::getStatus, STATUS_LIVING);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(400, "房源已出租");
        }

        // 3. 检查用户是否存在
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 4. 创建租客记录
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(dto, tenant);
        tenant.setStatus(STATUS_LIVING);
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());

        baseMapper.insert(tenant);

        // 5. 更新房源状态为已出租
        property.setStatus(1);
        propertyMapper.updateById(property);

        log.info("入住登记成功：tenantId={}", tenant.getId());
        return tenant.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long tenantId, LocalDate checkOutDate) {
        log.info("退租办理：tenantId={}, checkOutDate={}", tenantId, checkOutDate);

        Tenant tenant = baseMapper.selectById(tenantId);
        if (tenant == null) {
            throw new BusinessException(404, "租客不存在");
        }

        if (tenant.getStatus() == STATUS_CHECKED_OUT) {
            throw new BusinessException(400, "租客已退租");
        }

        // 更新租客状态
        tenant.setStatus(STATUS_CHECKED_OUT);
        tenant.setCheckOutDate(checkOutDate);
        tenant.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(tenant);

        // 更新房源状态为未出租
        Property property = propertyMapper.selectById(tenant.getPropertyId());
        if (property != null) {
            property.setStatus(0);
            propertyMapper.updateById(property);
        }

        log.info("退租办理成功：tenantId={}", tenantId);
    }

    @Override
    public Page<TenantVO> getTenantList(Long landlordId, Integer status, Integer pageNum, Integer pageSize) {
        log.info("查询租客列表：landlordId={}, status={}", landlordId, status);

        Page<Tenant> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(Tenant::getStatus, status);
        }
        queryWrapper.orderByDesc(Tenant::getCreateTime);

        Page<Tenant> tenantPage = baseMapper.selectPage(page, queryWrapper);

        // 转换为 VO
        List<TenantVO> voList = tenantPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<TenantVO> voPage = new Page<>(tenantPage.getCurrent(), tenantPage.getSize(), tenantPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public TenantVO getTenantDetail(Long tenantId) {
        log.info("查询租客详情：tenantId={}", tenantId);

        Tenant tenant = baseMapper.selectById(tenantId);
        if (tenant == null) {
            throw new BusinessException(404, "租客不存在");
        }

        return convertToVO(tenant);
    }

    /**
     * 转换为 VO
     *
     * @param tenant 租客实体
     * @return 租客 VO
     */
    private TenantVO convertToVO(Tenant tenant) {
        TenantVO vo = new TenantVO();
        BeanUtils.copyProperties(tenant, vo);

        // 计算入住天数
        if (tenant.getStatus() == STATUS_CHECKED_OUT && tenant.getCheckOutDate() != null) {
            long days = ChronoUnit.DAYS.between(tenant.getCheckInDate(), tenant.getCheckOutDate());
            vo.setStayDays(days);
        } else if (tenant.getCheckInDate() != null) {
            long days = ChronoUnit.DAYS.between(tenant.getCheckInDate(), LocalDate.now());
            vo.setStayDays(days);
        }

        return vo;
    }
}
