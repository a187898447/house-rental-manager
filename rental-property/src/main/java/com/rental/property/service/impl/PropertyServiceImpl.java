package com.rental.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.property.dto.PropertyCreateDTO;
import com.rental.property.dto.PropertyQueryDTO;
import com.rental.property.dto.PropertyUpdateDTO;
import com.rental.property.entity.Building;
import com.rental.property.entity.Property;
import com.rental.property.mapper.BuildingMapper;
import com.rental.property.mapper.PropertyMapper;
import com.rental.property.service.PropertyService;
import com.rental.property.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 房源服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService {

    private final BuildingMapper buildingMapper;

    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();
    static {
        STATUS_MAP.put(0, "未出租");
        STATUS_MAP.put(1, "已出租");
    }

    @Override
    public Page<PropertyVO> queryPage(PropertyQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<Property> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Property::getOwnerId, queryDTO.getOwnerId())
               .eq(queryDTO.getBuildingId() != null, Property::getBuildingId, queryDTO.getBuildingId())
               .eq(queryDTO.getStatus() != null, Property::getStatus, queryDTO.getStatus())
               .like(StringUtils.hasText(queryDTO.getKeyword()), Property::getRoomNumber, queryDTO.getKeyword())
               .isNull(Property::getDeletedAt)
               .orderByDesc(Property::getCreatedAt);

        // 分页查询
        Page<Property> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        Page<Property> resultPage = this.page(page, wrapper);

        // 转换为VO
        return convertPage(resultPage);
    }

    @Override
    public PropertyVO getDetail(Long id) {
        Property property = this.getById(id);
        if (property == null || property.getDeletedAt() != null) {
            throw new BusinessException("房源不存在");
        }

        PropertyVO vo = new PropertyVO();
        BeanUtils.copyProperties(property, vo);
        vo.setStatusName(STATUS_MAP.getOrDefault(property.getStatus(), "未知"));

        // 查询楼栋名称
        if (property.getBuildingId() != null) {
            Building building = buildingMapper.selectById(property.getBuildingId());
            if (building != null) {
                vo.setBuildingName(building.getName());
            }
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(PropertyCreateDTO dto) {
        // 检查房源是否重复（同一房东下的相同楼栋+房号）
        LambdaQueryWrapper<Property> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Property::getOwnerId, dto.getOwnerId())
               .eq(Property::getBuildingId, dto.getBuildingId())
               .eq(Property::getRoomNumber, dto.getRoomNumber())
               .isNull(Property::getDeletedAt);
        
        Property existProperty = this.getOne(wrapper);
        if (existProperty != null) {
            throw new BusinessException("该房源已存在");
        }

        // 创建房源
        Property property = new Property();
        BeanUtils.copyProperties(dto, property);
        property.setStatus(0); // 默认未出租
        
        this.save(property);
        log.info("创建房源成功: id={}, ownerId={}, roomNumber={}", property.getId(), property.getOwnerId(), property.getRoomNumber());
        
        return property.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProperty(Long id, PropertyUpdateDTO dto) {
        Property property = this.getById(id);
        if (property == null || property.getDeletedAt() != null) {
            throw new BusinessException("房源不存在");
        }

        BeanUtils.copyProperties(dto, property);
        boolean result = this.updateById(property);
        
        log.info("更新房源成功: id={}", id);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProperty(Long id) {
        Property property = this.getById(id);
        if (property == null || property.getDeletedAt() != null) {
            throw new BusinessException("房源不存在");
        }

        // 逻辑删除
        property.setDeletedAt(java.time.LocalDateTime.now());
        boolean result = this.updateById(property);
        
        log.info("删除房源成功: id={}", id);
        return result;
    }

    /**
     * 分页结果转换
     */
    private Page<PropertyVO> convertPage(Page<Property> page) {
        Page<PropertyVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        
        for (Property property : page.getRecords()) {
            PropertyVO vo = new PropertyVO();
            BeanUtils.copyProperties(property, vo);
            vo.setStatusName(STATUS_MAP.getOrDefault(property.getStatus(), "未知"));
            
            // 查询楼栋名称
            if (property.getBuildingId() != null) {
                Building building = buildingMapper.selectById(property.getBuildingId());
                if (building != null) {
                    vo.setBuildingName(building.getName());
                }
            }
            
            voPage.getRecords().add(vo);
        }
        
        return voPage;
    }
}
