package com.rental.property.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 房源 DTO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "房源信息")
public class PropertyDTO {

    /**
     * 房源 ID（编辑时必填）
     */
    @Schema(description = "房源 ID")
    private Long id;

    /**
     * 房源名称
     */
    @NotBlank(message = "房源名称不能为空")
    @Schema(description = "房源名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    /**
     * 楼栋 ID
     */
    @Schema(description = "楼栋 ID")
    private Long buildingId;

    /**
     * 户型
     */
    @NotBlank(message = "户型不能为空")
    @Schema(description = "户型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String layout;

    /**
     * 面积
     */
    @NotNull(message = "面积不能为空")
    @DecimalMin(value = "0.01", message = "面积必须大于 0")
    @Schema(description = "面积（平方米）", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal area;

    /**
     * 租金
     */
    @NotNull(message = "租金不能为空")
    @DecimalMin(value = "0.01", message = "租金必须大于 0")
    @Schema(description = "租金（元/月）", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal rentAmount;
}
