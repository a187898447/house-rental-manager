package com.rental.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.bill.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约看房Mapper
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
