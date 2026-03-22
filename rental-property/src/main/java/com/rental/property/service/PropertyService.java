package com.rental.property.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.property.dto.PropertyCreateDTO;
import com.rental.property.dto.PropertyQueryDTO;
import com.rental.property.dto.PropertyUpdateDTO;
import com.rental.property.entity.Property;
import com.rental.property.vo.PropertyVO;

/**
 * 房源服务接口
 */
public interface PropertyService extends IService<Property> {

    /**
     * 分页查询房源列表
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    Page<PropertyVO> queryPage(PropertyQueryDTO queryDTO);

    /**
     * 根据ID查询房源详情
     * @param id 房源ID
     * @return 房源详情
     */
    PropertyVO getDetail(Long id);

    /**
     * 创建房源
     * @param dto 创建参数
     * @return 房源ID
     */
    Long create(PropertyCreateDTO dto);

    /**
     * 更新房源
     * @param id 房源ID
     * @param dto 更新参数
     * @return 是否成功
     */
    boolean updateProperty(Long id, PropertyUpdateDTO dto);

    /**
     * 删除房源
     * @param id 房源ID
     * @return 是否成功
     */
    boolean deleteProperty(Long id);

    /**
     * 更新水电费配置
     * @param id 房源ID
     * @param dto 水电费配置
     * @return 是否成功
     */
    boolean updateUtilityConfig(Long id, com.rental.property.dto.UtilityConfigDTO dto);
}
