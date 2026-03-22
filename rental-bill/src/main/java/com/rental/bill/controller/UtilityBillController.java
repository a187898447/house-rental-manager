package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.service.UtilityBillService;
import com.rental.bill.vo.UtilityBillVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/utility")
@RequiredArgsConstructor
@Tag(name = "水电账单", description = "水电费管理相关接口")
public class UtilityBillController {

    private final UtilityBillService utilityBillService;

    @GetMapping("/{id}")
    @Operation(summary = "水电账单详情")
    public Result<UtilityBillVO> getDetail(@PathVariable Long id) {
        return Result.success(utilityBillService.getDetail(id));
    }

    @GetMapping("/tenant")
    @Operation(summary = "租客水电账单列表")
    public Result<Page<UtilityBillVO>> getTenantBills(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(utilityBillService.getTenantBills(tenantId, status, page, size));
    }

    @GetMapping("/owner")
    @Operation(summary = "房东水电账单列表")
    public Result<Page<UtilityBillVO>> getOwnerBills(
            Authentication authentication,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = (Long) authentication.getPrincipal();
        return Result.success(utilityBillService.getOwnerBills(ownerId, status, page, size));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "支付水电账单")
    public Result<Boolean> pay(@PathVariable Long id) {
        return Result.success(utilityBillService.pay(id));
    }
}
