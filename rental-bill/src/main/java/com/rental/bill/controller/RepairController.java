package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RepairCreateDTO;
import com.rental.bill.service.RepairService;
import com.rental.bill.vo.RepairVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 报修控制器
 */
@RestController
@RequestMapping("/repair")
@RequiredArgsConstructor
@Tag(name = "报修管理", description = "报修提交、处理相关接口")
public class RepairController {

    private final RepairService repairService;

    @PostMapping
    @Operation(summary = "提交报修")
    public Result<Long> create(@RequestBody RepairCreateDTO dto) {
        Long id = repairService.create(dto);
        return Result.success("报修提交成功", id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "报修详情")
    @Parameter(name = "id", description = "报修ID")
    public Result<RepairVO> getDetail(@PathVariable Long id) {
        RepairVO vo = repairService.getDetail(id);
        return Result.success(vo);
    }

    @GetMapping("/tenant")
    @Operation(summary = "租客报修列表")
    public Result<Page<RepairVO>> getTenantRepairs(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<RepairVO> result = repairService.getTenantRepairs(tenantId, status, page, size);
        return Result.success(result);
    }

    @GetMapping("/owner")
    @Operation(summary = "房东报修列表")
    public Result<Page<RepairVO>> getOwnerRepairs(
            Authentication authentication,
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = (Long) authentication.getPrincipal();
        Page<RepairVO> result = repairService.getOwnerRepairs(ownerId, propertyId, status, page, size);
        return Result.success(result);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "开始处理")
    @Parameter(name = "id", description = "报修ID")
    public Result<Boolean> startProcess(@PathVariable Long id, Authentication authentication) {
        Long handlerId = (Long) authentication.getPrincipal();
        boolean result = repairService.startProcess(id, handlerId);
        return Result.success("已开始处理", result);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "完成处理")
    @Parameter(name = "id", description = "报修ID")
    public Result<Boolean> complete(@PathVariable Long id, @RequestParam(required = false) String remark) {
        boolean result = repairService.complete(id, remark);
        return Result.success("处理完成", result);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消报修")
    @Parameter(name = "id", description = "报修ID")
    public Result<Boolean> cancel(@PathVariable Long id) {
        boolean result = repairService.cancel(id);
        return Result.success("已取消", result);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新报修状态")
    @Parameter(name = "id", description = "报修ID")
    public Result<Boolean> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remark) {
        // 将字符串状态转换为整数: processing=1, completed=2, cancelled=3
        Integer statusCode = switch (status) {
            case "processing" -> 1;
            case "completed" -> 2;
            case "cancelled" -> 3;
            default -> 0;
        };
        boolean result = repairService.updateStatus(id, statusCode, remark);
        return Result.success("状态更新成功", result);
    }
}
