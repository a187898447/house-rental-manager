package com.rental.property.controller;

import com.rental.common.result.Result;
import com.rental.property.dto.PropertyCreateDTO;
import com.rental.property.dto.PropertyQueryDTO;
import com.rental.property.dto.PropertyUpdateDTO;
import com.rental.property.entity.Property;
import com.rental.property.service.PropertyService;
import com.rental.property.vo.PropertyVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 房源控制器
 */
@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
@Tag(name = "房源管理", description = "房源增删改查相关接口")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/list")
    @Operation(summary = "分页查询房源列表")
    public Result<Page<PropertyVO>> list(PropertyQueryDTO queryDTO, 
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        // 从 Header 获取房东ID (JwtAuthFilter 设置)
        Long ownerId = Long.valueOf(userId);
        queryDTO.setOwnerId(ownerId);
        
        var page = propertyService.queryPage(queryDTO);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取房源详情")
    @Parameter(name = "id", description = "房源ID")
    public Result<PropertyVO> getDetail(@PathVariable Long id) {
        PropertyVO vo = propertyService.getDetail(id);
        return Result.success(vo);
    }

    @PostMapping
    @Operation(summary = "新增房源")
    public Result<Long> create(@RequestBody PropertyCreateDTO dto,
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        // 从 Header 获取房东ID (JwtAuthFilter 设置)
        Long ownerId = Long.valueOf(userId);
        dto.setOwnerId(ownerId);
        
        Long id = propertyService.create(dto);
        return Result.success("房源创建成功", id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新房源")
    @Parameter(name = "id", description = "房源ID")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody PropertyUpdateDTO dto) {
        boolean result = propertyService.updateProperty(id, dto);
        return Result.success("房源更新成功", result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除房源")
    @Parameter(name = "id", description = "房源ID")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = propertyService.deleteProperty(id);
        return Result.success("房源删除成功", result);
    }

    // ========== 公开接口（无需登录）==========

    @GetMapping("/public/list")
    @Operation(summary = "公开房源列表（无需登录）")
    public Result<Page<PropertyVO>> getPublicList(PropertyQueryDTO queryDTO) {
        // 不需要ownerId筛选，返回所有公开房源
        queryDTO.setOwnerId(null);
        var page = propertyService.queryPage(queryDTO);
        return Result.success(page);
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "公开房源详情（无需登录）")
    @Parameter(name = "id", description = "房源ID")
    public Result<PropertyVO> getPublicDetail(@PathVariable Long id) {
        PropertyVO vo = propertyService.getDetail(id);
        return Result.success(vo);
    }

    // ========== 水电费配置 ==========

    @GetMapping("/{id}/utility-config")
    @Operation(summary = "获取水电费配置")
    @Parameter(name = "id", description = "房源ID")
    public Result<Map<String, Object>> getUtilityConfig(@PathVariable Long id) {
        return Result.success(propertyService.getUtilityConfig(id));
    }

    @PutMapping("/{id}/utility-config")
    @Operation(summary = "设置水电费单价")
    @Parameter(name = "id", description = "房源ID")
    public Result<Boolean> setUtilityConfig(
            @PathVariable Long id,
            @RequestParam BigDecimal waterPrice,
            @RequestParam BigDecimal electricityPrice) {
        boolean result = propertyService.setUtilityConfig(id, waterPrice, electricityPrice);
        return Result.success("水电费配置更新成功", result);
    }
}
