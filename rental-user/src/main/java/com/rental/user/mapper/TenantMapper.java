package com.rental.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.user.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租客 Mapper 接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 查询租客列表（按房源 ID）
     *
     * @param propertyId 房源 ID
     * @param status     状态（可选）
     * @return 租客列表
     */
    List<Tenant> selectByPropertyId(@Param("propertyId") Long propertyId, @Param("status") Integer status);

    /**
     * 查询租客列表（按房东 ID）
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @return 租客列表
     */
    List<Tenant> selectByLandlordId(@Param("landlordId") Long landlordId, @Param("status") Integer status);
}
