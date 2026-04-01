package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.common.result.Result;
import com.rental.bill.dto.RentRecordDTO;
import com.rental.bill.service.RentRecordService;
import com.rental.bill.vo.RentRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 租金记录控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/rent")
@RequiredArgsConstructor
@Tag(name = "租金管理", description = "租金记录、收租提醒、催租提醒接口")
public class RentRecordController {

    private final RentRecordService rentRecordService;

    /**
     * 创建租金记录
     *
     * @param dto 租金记录 DTO
     * @return 记录 ID
     */
    @PostMapping
    @Operation(summary = "创建租金记录")
    public Result<Long> createRentRecord(@Valid @RequestBody RentRecordDTO dto) {
        log.info("创建租金记录请求：tenantId={}", dto.getTenantId());
        Long recordId = rentRecordService.createRentRecord(dto);
        return Result.success(recordId);
    }

    /**
     * 确认收款
     *
     * @param recordId 记录 ID
     * @param paidDate 实收日期
     * @return 成功结果
     */
    @PostMapping("/{recordId}/payment")
    @Operation(summary = "确认收款")
    public Result<Void> confirmPayment(@PathVariable Long recordId,
                                       @RequestParam LocalDate paidDate) {
        log.info("确认收款请求：recordId={}", recordId);
        rentRecordService.confirmPayment(recordId, paidDate);
        return Result.success();
    }

    /**
     * 查询租金记录列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 租金记录列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询租金记录列表")
    public Result<Page<RentRecordVO>> getRentRecordList(
            @RequestParam Long landlordId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询租金记录列表：landlordId={}, status={}", landlordId, status);
        Page<RentRecordVO> page = rentRecordService.getRentRecordList(landlordId, status, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询今日待收租（D+0 提醒）
     *
     * @param landlordId 房东 ID
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 待收租列表
     */
    @GetMapping("/due-today")
    @Operation(summary = "查询今日待收租（D+0 提醒）")
    public Result<Page<RentRecordVO>> getDueTodayList(
            @RequestParam Long landlordId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询今日待收租：landlordId={}", landlordId);
        Page<RentRecordVO> page = rentRecordService.getDueTodayList(landlordId, LocalDate.now(), pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询催租提醒（D+2/D+3）
     *
     * @param landlordId 房东 ID
     * @param days       逾期天数（2 或 3）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 催租列表
     */
    @GetMapping("/overdue")
    @Operation(summary = "查询催租提醒（D+2/D+3）")
    public Result<Page<RentRecordVO>> getOverdueList(
            @RequestParam Long landlordId,
            @RequestParam Integer days,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询催租提醒：landlordId={}, days={}", landlordId, days);
        Page<RentRecordVO> page = rentRecordService.getOverdueList(landlordId, days, pageNum, pageSize);
        return Result.success(page);
    }
}
