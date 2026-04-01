package com.rental.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.common.result.Result;
import com.rental.property.dto.PropertyDTO;
import com.rental.property.service.PropertyService;
import com.rental.property.vo.PropertyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 房源控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
@Tag(name = "房源管理", description = "房源 CRUD 相关接口")
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * 添加房源
     *
     * @param dto 房源信息
     * @return 房源 ID
     */
    @PostMapping
    @Operation(summary = "添加房源")
    public Result<Long> addProperty(@Valid @RequestBody PropertyDTO dto,
                                    @RequestParam Long landlordId) {
        log.info("添加房源请求：name={}", dto.getName());
        Long propertyId = propertyService.addProperty(dto, landlordId);
        return Result.success(propertyId);
    }

    /**
     * 编辑房源
     *
     * @param dto 房源信息
     * @return 成功结果
     */
    @PutMapping
    @Operation(summary = "编辑房源")
    public Result<Void> updateProperty(@Valid @RequestBody PropertyDTO dto) {
        log.info("编辑房源请求：propertyId={}", dto.getId());
        propertyService.updateProperty(dto);
        return Result.success();
    }

    /**
     * 删除房源
     *
     * @param propertyId 房源 ID
     * @return 成功结果
     */
    @DeleteMapping("/{propertyId}")
    @Operation(summary = "删除房源")
    public Result<Void> deleteProperty(@PathVariable Long propertyId) {
        log.info("删除房源请求：propertyId={}", propertyId);
        propertyService.deleteProperty(propertyId);
        return Result.success();
    }

    /**
     * 查询房源列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 房源列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询房源列表")
    public Result<Page<PropertyVO>> getPropertyList(
            @RequestParam Long landlordId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询房源列表：landlordId={}, status={}", landlordId, status);
        Page<PropertyVO> page = propertyService.getPropertyList(landlordId, status, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询房源详情
     *
     * @param propertyId 房源 ID
     * @return 房源信息
     */
    @GetMapping("/{propertyId}")
    @Operation(summary = "查询房源详情")
    public Result<PropertyVO> getPropertyDetail(@PathVariable Long propertyId) {
        log.info("查询房源详情：propertyId={}", propertyId);
        PropertyVO vo = propertyService.getPropertyDetail(propertyId);
        return Result.success(vo);
    }
}
