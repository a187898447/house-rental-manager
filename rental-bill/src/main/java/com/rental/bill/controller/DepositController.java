package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.service.DepositService;
import com.rental.bill.vo.DepositVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/deposit")
@RequiredArgsConstructor
@Tag(name = "押金管理", description = "押金缴纳、退还相关接口")
public class DepositController {

    private final DepositService depositService;

    @GetMapping("/{id}")
    @Operation(summary = "押金详情")
    public Result<DepositVO> getDetail(@PathVariable Long id) {
        return Result.success(depositService.getDetail(id));
    }

    @GetMapping("/tenant")
    @Operation(summary = "租客押金列表")
    public Result<Page<DepositVO>> getTenantDeposits(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(depositService.getTenantDeposits(tenantId, status, page, size));
    }

    @GetMapping("/owner")
    @Operation(summary = "房东押金列表")
    public Result<Page<DepositVO>> getOwnerDeposits(
            HttpServletRequest request,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = Long.valueOf(request.getHeader("X-User-Id"));
        return Result.success(depositService.getOwnerDeposits(ownerId, status, page, size));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "缴纳押金")
    public Result<Boolean> pay(@PathVariable Long id, @RequestParam String payMethod) {
        return Result.success(depositService.pay(id, payMethod));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "退还押金")
    public Result<Boolean> refund(@PathVariable Long id, @RequestParam BigDecimal refundAmount) {
        return Result.success(depositService.refund(id, refundAmount));
    }
}
