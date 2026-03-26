package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordCreateDTO;
import com.rental.bill.service.RentRecordService;
import com.rental.bill.vo.RentRecordVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租金账单控制器
 */
@RestController
@RequestMapping("/rent")
@RequiredArgsConstructor
@Tag(name = "租金账单", description = "租金账单管理相关接口")
public class RentRecordController {

    private final RentRecordService rentRecordService;

    @PostMapping("/bills")
    @Operation(summary = "创建租金账单")
    public Result<Long> create(@RequestBody RentRecordCreateDTO dto) {
        Long id = rentRecordService.create(dto);
        return Result.success("账单创建成功", id);
    }

    @GetMapping("/bills/{id}")
    @Operation(summary = "账单详情")
    @Parameter(name = "id", description = "账单ID")
    public Result<RentRecordVO> getDetail(@PathVariable Long id) {
        RentRecordVO vo = rentRecordService.getDetail(id);
        return Result.success(vo);
    }

    @GetMapping("/bills/owner")
    @Operation(summary = "房东账单列表")
    public Result<Page<RentRecordVO>> getOwnerBills(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId,
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = Long.valueOf(userId);
        Page<RentRecordVO> result = rentRecordService.getOwnerBills(ownerId, propertyId, status, month, page, size);
        return Result.success(result);
    }

    @GetMapping("/bills/tenant")
    @Operation(summary = "租客账单列表")
    public Result<Page<RentRecordVO>> getTenantBills(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<RentRecordVO> result = rentRecordService.getTenantBills(tenantId, status, page, size);
        return Result.success(result);
    }

    @PostMapping("/bills/{id}/pay")
    @Operation(summary = "标记账单已支付")
    @Parameter(name = "id", description = "账单ID")
    public Result<Boolean> markPaid(@PathVariable Long id) {
        boolean result = rentRecordService.markPaid(id);
        return Result.success("支付成功", result);
    }

    @PostMapping("/bills/{id}/remind")
    @Operation(summary = "发送催租提醒")
    @Parameter(name = "id", description = "账单ID")
    public Result<Boolean> sendReminder(@PathVariable Long id) {
        boolean result = rentRecordService.sendReminder(id);
        return Result.success("提醒已发送", result);
    }

    @DeleteMapping("/bills/{id}")
    @Operation(summary = "取消账单")
    @Parameter(name = "id", description = "账单ID")
    public Result<Boolean> cancel(@PathVariable Long id) {
        boolean result = rentRecordService.cancel(id);
        return Result.success("账单已取消", result);
    }
}
