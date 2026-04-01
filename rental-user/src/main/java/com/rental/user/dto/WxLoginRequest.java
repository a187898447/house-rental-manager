package com.rental.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求 DTO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "微信登录请求")
public class WxLoginRequest {

    /**
     * 微信 openid
     */
    @NotBlank(message = "openid 不能为空")
    @Schema(description = "微信 openid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String openid;

    /**
     * 微信 unionid（可选）
     */
    @Schema(description = "微信 unionid")
    private String unionid;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像 URL")
    private String avatarUrl;
}
