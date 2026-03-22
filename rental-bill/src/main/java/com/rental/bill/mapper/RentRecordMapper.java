package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.RentRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租金账单Mapper
 */
@Mapper
public interface RentRecordMapper extends BaseMapper<RentRecord> {
}
