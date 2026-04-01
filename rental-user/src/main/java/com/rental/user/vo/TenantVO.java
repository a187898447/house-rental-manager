package com.rental.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租客视图对象 VO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "租客信息")
public class TenantVO {

    /**
     * 租客 ID
     */
    @Schema(description = "租客 ID")
    private Long id;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID")
    private Long userId;

    /**
     * 房源 ID
     */
    @Schema(description = "房源 ID")
    private Long propertyId;

    /**
     * 房源名称
     */
    @Schema(description = "房源名称")
    private String propertyName;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 入住日期
     */
    @Schema(description = "入住日期")
    private LocalDate checkInDate;

    /**
     * 退租日期
     */
    @Schema(description = "退租日期")
    private LocalDate checkOutDate;

    /**
     * 状态：0-在住，1-已退租
     */
    @Schema(description = "状态：0-在住，1-已退租")
    private Integer status;

    /**
     * 入住天数
     */
    @Schema(description = "入住天数")
    private Long stayDays;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
