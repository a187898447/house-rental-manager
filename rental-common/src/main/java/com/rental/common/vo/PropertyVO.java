package com.rental.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
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
public class PropertyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "房源 ID")
    private Long id;

    @Schema(description = "房源名称")
    private String name;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "楼栋 ID")
    private Long buildingId;

    @Schema(description = "户型")
    private String layout;

    @Schema(description = "面积（平方米）")
    private BigDecimal area;

    @Schema(description = "租金（元/月）")
    private BigDecimal rentAmount;

    @Schema(description = "状态：0-未出租，1-已出租")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
