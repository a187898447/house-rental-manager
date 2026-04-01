package com.rental.property.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源视图对象 VO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "房源信息")
public class PropertyVO {

    /**
     * 房源 ID
     */
    @Schema(description = "房源 ID")
    private Long id;

    /**
     * 房源名称
     */
    @Schema(description = "房源名称")
    private String name;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 楼栋 ID
     */
    @Schema(description = "楼栋 ID")
    private Long buildingId;

    /**
     * 户型
     */
    @Schema(description = "户型")
    private String layout;

    /**
     * 面积
     */
    @Schema(description = "面积（平方米）")
    private BigDecimal area;

    /**
     * 租金
     */
    @Schema(description = "租金（元/月）")
    private BigDecimal rentAmount;

    /**
     * 状态：0-未出租，1-已出租
     */
    @Schema(description = "状态：0-未出租，1-已出租")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
