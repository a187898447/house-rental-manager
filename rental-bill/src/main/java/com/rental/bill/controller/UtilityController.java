package com.rental.bill.controller;

import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 水电费控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/utility")
@RequiredArgsConstructor
@Tag(name = "水电管理", description = "水电单价配置、抄表录入、账单生成接口")
public class UtilityController {

    /**
     * 配置水电单价
     *
     * @param propertyId 房源 ID
     * @param type       类型：1-水费，2-电费
     * @param unitPrice  单价
     * @return 成功结果
     */
    @PostMapping("/price")
    @Operation(summary = "配置水电单价")
    public Result<Void> setUtilityPrice(
            @RequestParam Long propertyId,
            @RequestParam Integer type,
            @RequestParam BigDecimal unitPrice) {
        log.info("配置水电单价：propertyId={}, type={}, unitPrice={}", propertyId, type, unitPrice);
        // TODO: 实现业务逻辑
        return Result.success();
    }

    /**
     * 抄表录入
     *
     * @param tenantId       租客 ID
     * @param type           类型：1-水费，2-电费
     * @param currentReading 本期读数
     * @param billMonth      账单月份
     * @return 成功结果
     */
    @PostMapping("/reading")
    @Operation(summary = "抄表录入")
    public Result<Void> submitReading(
            @RequestParam Long tenantId,
            @RequestParam Integer type,
            @RequestParam BigDecimal currentReading,
            @RequestParam String billMonth) {
        log.info("抄表录入：tenantId={}, type={}, reading={}", tenantId, type, currentReading);
        // TODO: 实现业务逻辑
        return Result.success();
    }

    /**
     * 生成水电账单
     *
     * @param tenantId  租客 ID
     * @param type      类型：1-水费，2-电费
     * @param billMonth 账单月份
     * @return 成功结果
     */
    @PostMapping("/bill")
    @Operation(summary = "生成水电账单")
    public Result<Void> generateBill(
            @RequestParam Long tenantId,
            @RequestParam Integer type,
            @RequestParam String billMonth) {
        log.info("生成水电账单：tenantId={}, type={}, month={}", tenantId, type, billMonth);
        // TODO: 实现业务逻辑
        return Result.success();
    }
}
