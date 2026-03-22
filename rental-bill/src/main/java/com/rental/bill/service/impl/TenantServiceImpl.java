package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.entity.Appointment;
import com.rental.bill.entity.Property;
import com.rental.bill.entity.Building;
import com.rental.bill.mapper.AppointmentMapper;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.PropertyPublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 租客服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements TenantService {

    // 需要注入PropertyMapper，但目前在不同模块，这里简化处理
    // 实际项目中需要通过Feign调用property-service

    private static final Map<Integer, String> APPOINTMENT_STATUS_MAP = new HashMap<>();
    static {
        APPOINTMENT_STATUS_MAP.put(0, "待确认");
        APPOINTMENT_STATUS_MAP.put(1, "已确认");
        APPOINTMENT_STATUS_MAP.put(2, "已取消");
        APPOINTMENT_STATUS_MAP.put(3, "已完成");
    }

    @Override
    public Page<PropertyPublicVO> getPublicPropertyList(Integer page, Integer size) {
        // 简化实现：返回空列表
        // 实际需要调用property-service获取公开房源
        Page<PropertyPublicVO> result = new Page<>(page, size);
        log.info("获取公开房源列表: page={}, size={}", page, size);
        return result;
    }

    @Override
    public PropertyPublicVO getPublicPropertyDetail(Long id) {
        // 简化实现
        // 实际需要调用property-service获取房源详情
        log.info("获取公开房源详情: id={}", id);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAppointment(AppointmentCreateDTO dto) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(dto, appointment);
        appointment.setStatus(0); // 待确认
        
        this.save(appointment);
        log.info("创建预约看房: propertyId={}, tenantPhone={}", dto.getPropertyId(), dto.getTenantPhone());
        
        return appointment.getId();
    }

    @Override
    public Page<AppointmentVO> getMyAppointments(String tenantPhone, Integer page, Integer size) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getTenantPhone, tenantPhone)
               .isNull(Appointment::getDeletedAt)
               .orderByDesc(Appointment::getCreatedAt);
        
        Page<Appointment> pageResult = new Page<>(page, size);
        Page<Appointment> result = this.page(pageResult, wrapper);
        
        return convertPage(result);
    }

    @Override
    public Page<AppointmentVO> getOwnerAppointments(Long ownerId, Integer page, Integer size) {
        // 需要关联查询property表的ownerId
        // 简化实现
        Page<Appointment> pageResult = new Page<>(page, size);
        Page<Appointment> result = this.page(pageResult);
        
        return convertPage(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAppointmentStatus(Long id, Integer status) {
        Appointment appointment = this.getById(id);
        if (appointment == null) {
            return false;
        }
        
        appointment.setStatus(status);
        boolean result = this.updateById(appointment);
        log.info("更新预约状态: id={}, status={}", id, status);
        
        return result;
    }

    private Page<AppointmentVO> convertPage(Page<Appointment> page) {
        Page<AppointmentVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        
        for (Appointment appointment : page.getRecords()) {
            AppointmentVO vo = new AppointmentVO();
            BeanUtils.copyProperties(appointment, vo);
            vo.setStatusName(APPOINTMENT_STATUS_MAP.getOrDefault(appointment.getStatus(), "未知"));
            voPage.getRecords().add(vo);
        }
        
        return voPage;
    }
}
