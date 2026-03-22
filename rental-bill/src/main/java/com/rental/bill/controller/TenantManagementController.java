package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.service.TenantService;
import com.rental.bill.vo.TenantVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 租客管理控制器
 */
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
@Tag(name = "租客管理", description = "租客入住、退租相关接口")
public class TenantController {

    private final TenantService tenantService;

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
        Long ownerId = (Long) authentication.getPrincipal();
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
