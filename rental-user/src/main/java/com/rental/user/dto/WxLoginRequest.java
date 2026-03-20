package com.rental.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
@Schema(description = "微信登录请求")
public class WxLoginRequest {

    @Schema(description = "微信授权code", required = true)
    private String code;
}
