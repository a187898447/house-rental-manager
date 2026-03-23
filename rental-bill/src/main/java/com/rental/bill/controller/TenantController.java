package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.AppointmentCreateDTO;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.AppointmentVO;
import com.rental.bill.vo.PropertyPublicVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 租客端控制器
 */
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
@Tag(name = "租客端", description = "房源浏览、预约看房相关接口")
public class TenantController {

    private final TenantService tenantService;

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
        var result = tenantService.getMyAppointments(phone, page, size);
        return Result.success(result);
    }

    @GetMapping("/appointment/owner")
    @Operation(summary = "收到的预约列表（房东）")
    public Result<Page<AppointmentVO>> getOwnerAppointments(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = (Long) authentication.getPrincipal();
        var result = tenantService.getOwnerAppointments(ownerId, page, size);
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
}
