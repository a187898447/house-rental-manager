package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.UtilityBill;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水电账单Mapper
 */
@Mapper
public interface UtilityBillMapper extends BaseMapper<UtilityBill> {
}
