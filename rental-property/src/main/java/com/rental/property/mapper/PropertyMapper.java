package com.rental.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.property.entity.Property;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyMapper extends BaseMapper<Property> {
}
