package com.rental.bill.controller;

import com.rental.bill.service.*;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "数据统计", description = "房东数据统计相关接口")
public class StatisticsController {

    private final RentRecordService rentRecordService;
    private final DepositService depositService;
    private final PropertyService propertyService;
    private final OtherFeeService otherFeeService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取房东数据统计")
    public Result<Map<String, Object>> getDashboard(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        Long ownerId = Long.valueOf(userId);
        
        Map<String, Object> stats = new HashMap<>();
        
        // 房源统计 - 从 property 服务获取
        Map<String, Object> propertyStats = propertyService.getStatistics(ownerId);
        stats.put("totalProperties", propertyStats.getOrDefault("total", 0));
        stats.put("rentedCount", propertyStats.getOrDefault("rented", 0));
        stats.put("vacantCount", propertyStats.getOrDefault("vacant", 0));
        
        // 租金统计
        Map<String, Object> rentStats = rentRecordService.getOwnerStats(ownerId);
        stats.put("monthlyRent", rentStats.getOrDefault("totalAmount", BigDecimal.ZERO));
        stats.put("pendingAmount", rentStats.getOrDefault("pendingAmount", BigDecimal.ZERO));
        stats.put("pendingCount", rentStats.getOrDefault("pendingCount", 0));
        stats.put("rentIncome", rentStats.getOrDefault("paidAmount", BigDecimal.ZERO));
        
        // 押金统计
        stats.put("depositIncome", depositService.getTotalDeposits(ownerId));
        
        // 其他费用统计
        stats.put("otherIncome", otherFeeService.getTotalByOwner(ownerId));
        
        log.info("获取房东统计数据: ownerId={}, stats={}", ownerId, stats);
        return Result.success(stats);
    }
}
