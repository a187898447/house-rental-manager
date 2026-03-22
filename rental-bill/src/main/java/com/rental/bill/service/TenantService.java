package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.PropertyPublicVO;

/**
 * 租客服务接口
 */
public interface TenantService {

    /**
     * 公开房源列表（无需登录）
     */
    Page<PropertyPublicVO> getPublicPropertyList(Integer page, Integer size);

    /**
     * 公开房源详情（无需登录）
     */
    PropertyPublicVO getPublicPropertyDetail(Long id);

    /**
     * 预约看房
     */
    Long createAppointment(AppointmentCreateDTO dto);

    /**
     * 我的预约列表（租客）
     */
    Page<AppointmentVO> getMyAppointments(String tenantPhone, Integer page, Integer size);

    /**
     * 收到的预约列表（房东）
     */
    Page<AppointmentVO> getOwnerAppointments(Long ownerId, Integer page, Integer size);

    /**
     * 更新预约状态
     */
    boolean updateAppointmentStatus(Long id, Integer status);
}
