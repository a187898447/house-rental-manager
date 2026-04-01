package com.rental.bill.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租金记录 VO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "租金记录")
public class RentRecordVO {

    /**
     * 记录 ID
     */
    @Schema(description = "记录 ID")
    private Long id;

    /**
     * 租客 ID
     */
    @Schema(description = "租客 ID")
    private Long tenantId;

    /**
     * 租客姓名
     */
    @Schema(description = "租客姓名")
    private String tenantName;

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
     * 租金金额
     */
    @Schema(description = "租金金额")
    private BigDecimal amount;

    /**
     * 应收日期
     */
    @Schema(description = "应收日期")
    private LocalDate dueDate;

    /**
     * 实收日期
     */
    @Schema(description = "实收日期")
    private LocalDate paidDate;

    /**
     * 状态：0-未支付，1-已支付，2-逾期
     */
    @Schema(description = "状态：0-未支付，1-已支付，2-逾期")
    private Integer status;

    /**
     * 逾期天数
     */
    @Schema(description = "逾期天数")
    private Long overdueDays;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
