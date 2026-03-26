package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.Receipt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {
}