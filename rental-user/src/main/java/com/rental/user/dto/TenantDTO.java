package com.rental.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 租客 DTO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "租客信息")
public class TenantDTO {

    /**
     * 租客 ID（退租时必填）
     */
    @Schema(description = "租客 ID")
    private Long id;

    /**
     * 用户 ID
     */
    @NotNull(message = "用户 ID 不能为空")
    @Schema(description = "用户 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 房源 ID
     */
    @NotNull(message = "房源 ID 不能为空")
    @Schema(description = "房源 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long propertyId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String idCard;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    /**
     * 入住日期
     */
    @NotNull(message = "入住日期不能为空")
    @Schema(description = "入住日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate checkInDate;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
