package com.rental.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户视图对象 VO
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@Schema(description = "用户信息")
public class UserVO {

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID")
    private Long id;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 头像 URL
     */
    @Schema(description = "头像 URL")
    private String avatarUrl;

    /**
     * 角色：landlord-房东，tenant-住户
     */
    @Schema(description = "角色：landlord-房东，tenant-住户")
    private String role;

    /**
     * Token
     */
    @Schema(description = "登录 Token")
    private String token;
}
