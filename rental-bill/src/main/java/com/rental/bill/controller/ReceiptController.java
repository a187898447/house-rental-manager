package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.service.ReceiptService;
import com.rental.bill.vo.ReceiptVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收据控制器
 */
@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Tag(name = "收据管理", description = "电子收据相关接口")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/owner")
    @Operation(summary = "房东收据列表")
    public Result<Page<ReceiptVO>> getOwnerReceipts(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = Long.valueOf(userId);
        return Result.success(receiptService.getOwnerReceipts(ownerId, type, month, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "收据详情")
    @Parameter(name = "id", description = "收据ID")
    public Result<ReceiptVO> getDetail(@PathVariable Long id) {
        return Result.success(receiptService.getDetail(id));
    }
}