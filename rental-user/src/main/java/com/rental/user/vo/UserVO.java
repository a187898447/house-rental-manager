package com.rental.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息VO
 */
@Data
@Schema(description = "用户信息")
public class UserVO {

    private Long id;

    private String phone;

    private String nickname;

    private String avatarUrl;

    private String role;

    private Integer status;
}
