package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.entity.Appointment;
import com.rental.bill.entity.Tenant;
import com.rental.bill.mapper.AppointmentMapper;
import com.rental.bill.mapper.TenantMapper;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.TenantVO;
import com.rental.common.exception.BusinessException;
import com.rental.common.notify.NotifyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 租客服务实现 - 包含预约和租客管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantMapper tenantMapper;
    private final AppointmentMapper appointmentMapper;

    @Autowired(required = false)
    private NotifyClient notifyClient;

    private static final List<Integer> CHECKOUT_ALLOWED_STATUSES = Arrays.asList(1);

    // ==================== 预约看房 ====================

    @Override
    @Transactional
    public Long createAppointment(AppointmentCreateDTO dto) {
        Appointment appointment = new Appointment();
        appointment.setPropertyId(dto.getPropertyId());
        appointment.setTenantName(dto.getTenantName());
        appointment.setTenantPhone(dto.getTenantPhone());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setRemark(dto.getRemark());
        appointment.setStatus(0); // 待确认
        
        appointmentMapper.insert(appointment);
        
        // TODO: 获取房东ID后发送通知
        // if (notifyClient != null) {
        //     notifyClient.notifyAppointmentCreated(ownerId, propertyName, tenantName, phone);
        // }
        
        log.info("预约创建成功: id={}, propertyId={}, tenant={}", appointment.getId(), dto.getPropertyId(), dto.getTenantName());
        return appointment.getId();
    }

    @Override
    public Page<AppointmentVO> getMyAppointments(String phone, Integer page, Integer size) {
        Page<Appointment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getTenantPhone, phone)
                .orderByDesc(Appointment::getCreatedAt);
        
        Page<Appointment> result = appointmentMapper.selectPage(pageParam, wrapper);
        
        Page<AppointmentVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertAppointmentToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<AppointmentVO> getOwnerAppointments(Long ownerId, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<Appointment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<Appointment>()
                .inSql(Appointment::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .orderByDesc(Appointment::getCreatedAt);
        
        Page<Appointment> result = appointmentMapper.selectPage(pageParam, wrapper);
        
        Page<AppointmentVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertAppointmentToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean updateAppointmentStatus(Long id, Integer status) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw new BusinessException("预约不存在");
        }
        
        appointment.setStatus(status);
        appointment.setUpdatedAt(LocalDateTime.now());
        
        // 状态变更通知租客
        // TODO: 调用通知服务
        
        return appointmentMapper.updateById(appointment) > 0;
    }

    // ==================== 租客管理 ====================

    @Override
    @Transactional
    public Long checkIn(TenantCreateDTO dto) {
        Tenant tenant = new Tenant();
        tenant.setUserId(dto.getUserId());
        tenant.setPropertyId(dto.getPropertyId());
        tenant.setName(dto.getName());
        tenant.setPhone(dto.getPhone());
        tenant.setIdCard(dto.getIdCard());
        tenant.setLeaseStartDate(dto.getLeaseStartDate());
        tenant.setLeaseEndDate(dto.getLeaseEndDate());
        tenant.setEmergencyContact(dto.getEmergencyContact());
        tenant.setEmergencyPhone(dto.getEmergencyPhone());
        tenant.setRemark(dto.getRemark());
        tenant.setStatus(0); // 待入住
        
        tenantMapper.insert(tenant);
        log.info("租客入住登记成功: id={}, propertyId={}, name={}", tenant.getId(), dto.getPropertyId(), dto.getName());
        return tenant.getId();
    }

    @Override
    public TenantVO getDetail(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            log.warn("租客不存在: id={}", id);
            throw new BusinessException("租客不存在");
        }
        return convertToVO(tenant);
    }

    @Override
    public Page<TenantVO> getPropertyTenants(Long propertyId, Integer status, Integer page, Integer size) {
        Page<Tenant> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<Tenant>()
                .eq(Tenant::getPropertyId, propertyId)
                .eq(status != null, Tenant::getStatus, status)
                .orderByDesc(Tenant::getCreatedAt);
        
        Page<Tenant> result = tenantMapper.selectPage(pageParam, wrapper);
        
        Page<TenantVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<TenantVO> getOwnerTenants(Long ownerId, Integer status, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<Tenant> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<Tenant>()
                .inSql(Tenant::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .eq(status != null, Tenant::getStatus, status)
                .orderByDesc(Tenant::getCreatedAt);
        
        Page<Tenant> result = tenantMapper.selectPage(pageParam, wrapper);
        
        Page<TenantVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean checkOut(Long id, String remark) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            log.warn("租客不存在: id={}", id);
            throw new BusinessException("租客不存在");
        }
        if (!CHECKOUT_ALLOWED_STATUSES.contains(tenant.getStatus())) {
            log.warn("租客状态不正确，无法退租: id={}, status={}", id, tenant.getStatus());
            throw new BusinessException("只有已入住的租客可以办理退租");
        }
        
        tenant.setStatus(2); // 已退租
        if (remark != null) {
            tenant.setRemark(remark);
        }
        
        boolean result = tenantMapper.updateById(tenant) > 0;
        log.info("租客退租成功: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            log.warn("租客不存在: id={}", id);
            throw new BusinessException("租客不存在");
        }
        
        boolean result = tenantMapper.deleteById(id) > 0;
        log.info("租客删除成功: id={}", id);
        return result;
    }

    // ==================== 转换方法 ====================

    private AppointmentVO convertAppointmentToVO(Appointment appointment) {
        AppointmentVO vo = new AppointmentVO();
        vo.setId(appointment.getId());
        vo.setPropertyId(appointment.getPropertyId());
        vo.setTenantName(appointment.getTenantName());
        vo.setTenantPhone(appointment.getTenantPhone());
        vo.setAppointmentDate(appointment.getAppointmentDate());
        vo.setStatus(appointment.getStatus());
        vo.setStatusName(getAppointmentStatusName(appointment.getStatus()));
        vo.setRemark(appointment.getRemark());
        vo.setCreatedAt(appointment.getCreatedAt());
        return vo;
    }

    private TenantVO convertToVO(Tenant tenant) {
        TenantVO vo = new TenantVO();
        vo.setId(tenant.getId());
        vo.setUserId(tenant.getUserId());
        vo.setPropertyId(tenant.getPropertyId());
        vo.setName(tenant.getName());
        vo.setPhone(tenant.getPhone());
        vo.setIdCard(tenant.getIdCard());
        vo.setLeaseStartDate(tenant.getLeaseStartDate());
        vo.setLeaseEndDate(tenant.getLeaseEndDate());
        vo.setEmergencyContact(tenant.getEmergencyContact());
        vo.setEmergencyPhone(tenant.getEmergencyPhone());
        vo.setStatus(tenant.getStatus());
        vo.setStatusName(getStatusName(tenant.getStatus()));
        vo.setRemark(tenant.getRemark());
        vo.setCreatedAt(tenant.getCreatedAt());
        vo.setUpdatedAt(tenant.getUpdatedAt());
        return vo;
    }

    private String getAppointmentStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待确认";
            case 1 -> "已确认";
            case 2 -> "已取消";
            case 3 -> "已完成";
            default -> "未知";
        };
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待入住";
            case 1 -> "已入住";
            case 2 -> "已退租";
            default -> "未知";
        };
    }
}
