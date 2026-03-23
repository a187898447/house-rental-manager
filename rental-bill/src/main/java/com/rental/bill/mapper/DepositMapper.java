package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.Deposit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 押金Mapper
 */
@Mapper
public interface DepositMapper extends BaseMapper<Deposit> {
}
