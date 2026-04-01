package com.rental.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.property.entity.Property;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房源 Mapper 接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Mapper
public interface PropertyMapper extends BaseMapper<Property> {

    /**
     * 查询房源列表（按房东 ID）
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @return 房源列表
     */
    List<Property> selectByLandlordId(@Param("landlordId") Long landlordId, @Param("status") Integer status);
}
