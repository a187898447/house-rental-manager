package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.TenantVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 租客服务控制器 - 包含预约和租客管理
 */
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
@Tag(name = "租客服务", description = "预约看房、租客管理相关接口")
public class TenantServiceController {

    private final TenantService tenantService;

    // ==================== 预约看房 ====================

    @PostMapping("/appointment")
    @Operation(summary = "预约看房")
    public Result<Long> createAppointment(@RequestBody AppointmentCreateDTO dto) {
        Long id = tenantService.createAppointment(dto);
        return Result.success("预约成功", id);
    }

    @GetMapping("/appointment/my")
    @Operation(summary = "我的预约列表（租客）")
    public Result<Page<AppointmentVO>> getMyAppointments(
            @RequestParam String phone,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AppointmentVO> result = tenantService.getMyAppointments(phone, page, size);
        return Result.success(result);
    }

    @GetMapping("/appointment/owner")
    @Operation(summary = "收到的预约列表（房东）")
    public Result<Page<AppointmentVO>> getOwnerAppointments(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = 1L; // TODO: 临时处理，正式环境要用 JWT
        Page<AppointmentVO> result = tenantService.getOwnerAppointments(ownerId, page, size);
        return Result.success(result);
    }

    @PutMapping("/appointment/{id}/status")
    @Operation(summary = "更新预约状态")
    @Parameter(name = "id", description = "预约ID")
    public Result<Boolean> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        boolean result = tenantService.updateAppointmentStatus(id, status);
        return Result.success("状态更新成功", result);
    }

    // ==================== 租客管理 ====================

    @PostMapping("/checkin")
    @Operation(summary = "入住登记")
    public Result<Long> checkIn(@RequestBody TenantCreateDTO dto) {
        Long id = tenantService.checkIn(dto);
        return Result.success("入住登记成功", id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "租客详情")
    @Parameter(name = "id", description = "租客ID")
    public Result<TenantVO> getDetail(@PathVariable Long id) {
        TenantVO vo = tenantService.getDetail(id);
        return Result.success(vo);
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "房源租客列表")
    @Parameter(name = "propertyId", description = "房源ID")
    public Result<Page<TenantVO>> getPropertyTenants(
            @PathVariable Long propertyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<TenantVO> result = tenantService.getPropertyTenants(propertyId, status, page, size);
        return Result.success(result);
    }

    @GetMapping("/owner/list")
    @Operation(summary = "房东所有租客")
    public Result<Page<TenantVO>> getOwnerTenants(
            Authentication authentication,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = 1L; // TODO: 临时处理，正式环境要用 JWT
        Page<TenantVO> result = tenantService.getOwnerTenants(ownerId, status, page, size);
        return Result.success(result);
    }

    @PostMapping("/{id}/checkout")
    @Operation(summary = "退租办理")
    @Parameter(name = "id", description = "租客ID")
    public Result<Boolean> checkOut(@PathVariable Long id, @RequestParam(required = false) String remark) {
        boolean result = tenantService.checkOut(id, remark);
        return Result.success("退租办理成功", result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除租客")
    @Parameter(name = "id", description = "租客ID")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = tenantService.delete(id);
        return Result.success("删除成功", result);
    }
}
