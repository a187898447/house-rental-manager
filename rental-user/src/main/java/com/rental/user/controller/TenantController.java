package com.rental.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.common.result.Result;
import com.rental.user.dto.TenantDTO;
import com.rental.user.service.TenantService;
import com.rental.user.vo.TenantVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 租客控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
@Tag(name = "租客管理", description = "租客入住、退租相关接口")
public class TenantController {

    private final TenantService tenantService;

    /**
     * 入住登记
     *
     * @param dto 租客信息
     * @return 租客 ID
     */
    @PostMapping("/check-in")
    @Operation(summary = "入住登记")
    public Result<Long> checkIn(@Valid @RequestBody TenantDTO dto) {
        log.info("入住登记请求：name={}", dto.getName());
        Long tenantId = tenantService.checkIn(dto);
        return Result.success(tenantId);
    }

    /**
     * 退租办理
     *
     * @param tenantId     租客 ID
     * @param checkOutDate 退租日期
     * @return 成功结果
     */
    @PostMapping("/check-out")
    @Operation(summary = "退租办理")
    public Result<Void> checkOut(@RequestParam Long tenantId,
                                 @RequestParam LocalDate checkOutDate) {
        log.info("退租办理请求：tenantId={}", tenantId);
        tenantService.checkOut(tenantId, checkOutDate);
        return Result.success();
    }

    /**
     * 查询租客列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 租客列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询租客列表")
    public Result<Page<TenantVO>> getTenantList(
            @RequestParam Long landlordId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询租客列表：landlordId={}, status={}", landlordId, status);
        Page<TenantVO> page = tenantService.getTenantList(landlordId, status, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询租客详情
     *
     * @param tenantId 租客 ID
     * @return 租客信息
     */
    @GetMapping("/{tenantId}")
    @Operation(summary = "查询租客详情")
    public Result<TenantVO> getTenantDetail(@PathVariable Long tenantId) {
        log.info("查询租客详情：tenantId={}", tenantId);
        TenantVO vo = tenantService.getTenantDetail(tenantId);
        return Result.success(vo);
    }
}
