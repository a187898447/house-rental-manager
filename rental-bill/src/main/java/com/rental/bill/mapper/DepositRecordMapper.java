package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.DepositRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 押金记录 Mapper 接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Mapper
public interface DepositRecordMapper extends BaseMapper<DepositRecord> {
}
