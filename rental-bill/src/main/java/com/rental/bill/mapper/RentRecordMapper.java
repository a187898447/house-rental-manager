package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.RentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 租金记录 Mapper 接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Mapper
public interface RentRecordMapper extends BaseMapper<RentRecord> {

    /**
     * 查询待收租列表
     *
     * @param landlordId 房东 ID
     * @param dueDate    截止日期
     * @return 租金记录列表
     */
    List<RentRecord> selectUnpaidByLandlord(@Param("landlordId") Long landlordId, @Param("dueDate") LocalDate dueDate);

    /**
     * 查询逾期租金记录
     *
     * @param landlordId 房东 ID
     * @param currentDate 当前日期
     * @return 租金记录列表
     */
    List<RentRecord> selectOverdueByLandlord(@Param("landlordId") Long landlordId, @Param("currentDate") LocalDate currentDate);
}
