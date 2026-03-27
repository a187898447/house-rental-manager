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
    
    @Select("SELECT COALESCE(SUM(d.amount), 0) FROM deposit d " +
            "INNER JOIN property p ON d.property_id = p.id " +
            "WHERE p.owner_id = #{ownerId} AND d.status = #{status}")
    BigDecimal selectSumByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") Integer status);
}
