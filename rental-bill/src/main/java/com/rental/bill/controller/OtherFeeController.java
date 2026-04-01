package com.rental.bill.controller;

import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 其他费用控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/other-fee")
@RequiredArgsConstructor
@Tag(name = "其他费用管理", description = "自定义收费设置、费用记录接口")
public class OtherFeeController {

    /**
     * 添加其他费用
     *
     * @param tenantId  租客 ID
     * @param feeType   费用类型
     * @param amount    金额
     * @param billMonth 账单月份
     * @return 费用 ID
     */
    @PostMapping
    @Operation(summary = "添加其他费用")
    public Result<Long> addOtherFee(
            @RequestParam Long tenantId,
            @RequestParam String feeType,
            @RequestParam BigDecimal amount,
            @RequestParam String billMonth) {
        log.info("添加其他费用：tenantId={}, feeType={}, amount={}", tenantId, feeType, amount);
        // TODO: 实现业务逻辑
        return Result.success(1L);
    }

    /**
     * 查询费用记录
     *
     * @param tenantId  租客 ID
     * @param billMonth 账单月份
     * @return 费用列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询费用记录")
    public Result<Object> getFeeList(
            @RequestParam Long tenantId,
            @RequestParam(required = false) String billMonth) {
        log.info("查询费用记录：tenantId={}, month={}", tenantId, billMonth);
        // TODO: 实现业务逻辑
        return Result.success(null);
    }
}
