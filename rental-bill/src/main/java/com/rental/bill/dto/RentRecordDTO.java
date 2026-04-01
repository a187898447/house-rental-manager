package com.rental.bill.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 租金记录 DTO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "租金记录")
public class RentRecordDTO {

    /**
     * 租客 ID
     */
    @NotNull(message = "租客 ID 不能为空")
    @Schema(description = "租客 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;

    /**
     * 房源 ID
     */
    @NotNull(message = "房源 ID 不能为空")
    @Schema(description = "房源 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long propertyId;

    /**
     * 租金金额
     */
    @NotNull(message = "租金金额不能为空")
    @DecimalMin(value = "0.01", message = "租金金额必须大于 0")
    @Schema(description = "租金金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    /**
     * 应收日期
     */
    @NotNull(message = "应收日期不能为空")
    @Schema(description = "应收日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dueDate;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
