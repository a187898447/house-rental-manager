package com.rental.property.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.property.dto.PropertyDTO;
import com.rental.property.entity.Property;
import com.rental.property.vo.PropertyVO;

import java.util.List;

/**
 * 房源服务接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
public interface PropertyService extends IService<Property> {

    /**
     * 添加房源
     *
     * @param dto        房源信息
     * @param landlordId 房东 ID
     * @return 房源 ID
     */
    Long addProperty(PropertyDTO dto, Long landlordId);

    /**
     * 编辑房源
     *
     * @param dto 房源信息
     */
    void updateProperty(PropertyDTO dto);

    /**
     * 删除房源（逻辑删除）
     *
     * @param propertyId 房源 ID
     */
    void deleteProperty(Long propertyId);

    /**
     * 查询房源列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 房源列表
     */
    Page<PropertyVO> getPropertyList(Long landlordId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 查询房源详情
     *
     * @param propertyId 房源 ID
     * @return 房源信息
     */
    PropertyVO getPropertyDetail(Long propertyId);
}
