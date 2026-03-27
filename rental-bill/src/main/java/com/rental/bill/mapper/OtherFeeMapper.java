package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.OtherFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 其他费用Mapper
 */
@Mapper
public interface OtherFeeMapper extends BaseMapper<OtherFee> {
    
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_other_fee WHERE property_id IN " +
            "(SELECT id FROM t_property WHERE owner_id = #{ownerId}) AND status = 1")
    BigDecimal selectSumByOwner(@Param("ownerId") Long ownerId);
}
