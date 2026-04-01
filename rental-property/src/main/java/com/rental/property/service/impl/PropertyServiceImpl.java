package com.rental.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.property.dto.PropertyDTO;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.property.service.PropertyService;
import com.rental.property.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 房源服务实现类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService {

    /**
     * 未删除状态
     */
    private static final Integer NOT_DELETED = 0;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addProperty(PropertyDTO dto, Long landlordId) {
        log.info("添加房源：name={}, landlordId={}", dto.getName(), landlordId);

        // 检查房源名称是否重复
        LambdaQueryWrapper<Property> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Property::getName, dto.getName())
                .eq(Property::getCreateBy, landlordId)
                .eq(Property::getDeleted, NOT_DELETED);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(400, "房源名称已存在");
        }

        Property property = new Property();
        BeanUtils.copyProperties(dto, property);
        property.setStatus(0); // 0-未出租
        property.setDeleted(0); // 0-未删除
        property.setCreateBy(landlordId);
        property.setCreateTime(LocalDateTime.now());
        property.setUpdateTime(LocalDateTime.now());

        baseMapper.insert(property);
        log.info("房源添加成功：propertyId={}", property.getId());
        return property.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProperty(PropertyDTO dto) {
        log.info("编辑房源：propertyId={}", dto.getId());

        Property property = baseMapper.selectById(dto.getId());
        if (property == null || property.getDeleted() == 1) {
            throw new BusinessException(404, "房源不存在");
        }

        BeanUtils.copyProperties(dto, property, "id", "createBy", "createTime", "deleted");
        property.setUpdateTime(LocalDateTime.now());

        baseMapper.updateById(property);
        log.info("房源编辑成功：propertyId={}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProperty(Long propertyId) {
        log.info("删除房源：propertyId={}", propertyId);

        Property property = baseMapper.selectById(propertyId);
        if (property == null) {
            throw new BusinessException(404, "房源不存在");
        }

        // 逻辑删除
        property.setDeleted(1);
        property.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(property);

        log.info("房源删除成功：propertyId={}", propertyId);
    }

    @Override
    public Page<PropertyVO> getPropertyList(Long landlordId, Integer status, Integer pageNum, Integer pageSize) {
        log.info("查询房源列表：landlordId={}, status={}", landlordId, status);

        Page<Property> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Property> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Property::getCreateBy, landlordId)
                .eq(Property::getDeleted, NOT_DELETED);
        if (status != null) {
            queryWrapper.eq(Property::getStatus, status);
        }
        queryWrapper.orderByDesc(Property::getCreateTime);

        Page<Property> propertyPage = baseMapper.selectPage(page, queryWrapper);

        // 转换为 VO
        List<PropertyVO> voList = propertyPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<PropertyVO> voPage = new Page<>(propertyPage.getCurrent(), propertyPage.getSize(), propertyPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public PropertyVO getPropertyDetail(Long propertyId) {
        log.info("查询房源详情：propertyId={}", propertyId);

        Property property = baseMapper.selectById(propertyId);
        if (property == null || property.getDeleted() == 1) {
            throw new BusinessException(404, "房源不存在");
        }

        return convertToVO(property);
    }

    /**
     * 转换为 VO
     *
     * @param property 房源实体
     * @return 房源 VO
     */
    private PropertyVO convertToVO(Property property) {
        PropertyVO vo = new PropertyVO();
        BeanUtils.copyProperties(property, vo);
        return vo;
    }
}
