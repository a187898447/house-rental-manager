package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.TenantVO;

/**
 * 租客服务接口 - 包含预约、租客管理
 */
public interface TenantService {
    
    // ==================== 预约看房 ====================
    
    /**
     * 创建预约 - 自动通知房东
     */
    Long createAppointment(AppointmentCreateDTO dto);
    
    /**
     * 租客查询自己的预约
     */
    Page<AppointmentVO> getMyAppointments(String phone, Integer page, Integer size);
    
    /**
     * 房东查询收到的预约
     */
    Page<AppointmentVO> getOwnerAppointments(Long ownerId, Integer page, Integer size);
    
    /**
     * 更新预约状态
     */
    boolean updateAppointmentStatus(Long id, Integer status);
    
    // ==================== 租客管理 ====================
    
    /**
     * 入住登记
     */
    Long checkIn(TenantCreateDTO dto);
    
    /**
     * 租客详情
     */
    TenantVO getDetail(Long id);
    
    /**
     * 房源租客列表
     */
    Page<TenantVO> getPropertyTenants(Long propertyId, Integer status, Integer page, Integer size);
    
    /**
     * 房东所有租客
     */
    Page<TenantVO> getOwnerTenants(Long ownerId, Integer status, Integer page, Integer size);
    
    /**
     * 退租办理
     */
    boolean checkOut(Long id, String remark);
    
    /**
     * 删除租客
     */
    boolean delete(Long id);
}
