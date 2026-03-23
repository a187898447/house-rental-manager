package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租客Mapper
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
