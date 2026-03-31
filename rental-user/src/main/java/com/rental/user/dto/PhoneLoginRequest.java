package com.rental.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 手机号登录请求
 */
@Data
@Schema(description = "手机号登录请求")
public class PhoneLoginRequest {

    @Schema(description = "手机号", required = true)
    private String phone;

    @Schema(description = "验证码", required = true)
    private String code;

    @Schema(description = "角色: landlord/tenant")
    private String role;
}