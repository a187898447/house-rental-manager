package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.ContractCreateDTO;
import com.rental.bill.dto.ContractUpdateDTO;
import com.rental.bill.service.ContractService;
import com.rental.bill.vo.ContractVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 合同控制器
 */
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@Tag(name = "合同管理", description = "合同创建、签署、查询相关接口")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @Operation(summary = "创建合同")
    public Result<Long> create(@RequestBody ContractCreateDTO dto) {
        Long id = contractService.create(dto);
        return Result.success("合同创建成功", id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "合同详情")
    @Parameter(name = "id", description = "合同ID")
    public Result<ContractVO> getDetail(@PathVariable Long id) {
        ContractVO vo = contractService.getDetail(id);
        return Result.success(vo);
    }

    @GetMapping("/owner/list")
    @Operation(summary = "房东合同列表")
    public Result<Page<ContractVO>> getOwnerContracts(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = Long.valueOf(userId);
        Page<ContractVO> result = contractService.getOwnerContracts(ownerId, page, size);
        return Result.success(result);
    }

    @GetMapping("/tenant/list")
    @Operation(summary = "租客合同列表")
    public Result<Page<ContractVO>> getTenantContracts(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ContractVO> result = contractService.getTenantContracts(tenantId, page, size);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新合同")
    @Parameter(name = "id", description = "合同ID")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody ContractUpdateDTO dto) {
        boolean result = contractService.update(id, dto);
        return Result.success("合同更新成功", result);
    }

    @PostMapping("/{id}/sign")
    @Operation(summary = "签署合同")
    @Parameter(name = "id", description = "合同ID")
    public Result<Boolean> sign(@PathVariable Long id, @RequestParam String signUrl) {
        boolean result = contractService.sign(id, signUrl);
        return Result.success("合同签署成功", result);
    }

    @GetMapping("/{id}/sign-url")
    @Operation(summary = "获取合同签署URL")
    @Parameter(name = "id", description = "合同ID")
    public Result<String> getSignUrl(@PathVariable Long id) {
        String url = contractService.getSignUrl(id);
        return Result.success(url);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新合同状态")
    @Parameter(name = "id", description = "合同ID")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = contractService.updateStatus(id, status);
        return Result.success("状态更新成功", result);
    }

    @PostMapping("/{id}/terminate")
    @Operation(summary = "解除合同")
    @Parameter(name = "id", description = "合同ID")
    public Result<Boolean> terminate(@PathVariable Long id) {
        boolean result = contractService.terminate(id);
        return Result.success("合同解除成功", result);
    }
}
