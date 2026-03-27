package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.RentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 租金账单Mapper
 */
@Mapper
public interface RentRecordMapper extends BaseMapper<RentRecord> {
    
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_rent_record WHERE owner_id = #{ownerId}")
    BigDecimal selectSumByOwner(@Param("ownerId") Long ownerId);
    
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_rent_record WHERE owner_id = #{ownerId} AND status = #{status}")
    BigDecimal selectSumByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") Integer status);
}
