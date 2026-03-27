package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.Deposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 押金Mapper
 */
@Mapper
public interface DepositMapper extends BaseMapper<Deposit> {
    
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_deposit WHERE property_id IN " +
            "(SELECT id FROM t_property WHERE owner_id = #{ownerId}) AND status = #{status}")
    BigDecimal selectSumByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") Integer status);
}
