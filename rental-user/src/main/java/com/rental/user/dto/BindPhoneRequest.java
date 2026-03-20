package com.rental.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 绑定手机号请求
 */
@Data
@Schema(description = "绑定手机号请求")
public class BindPhoneRequest {

    @Schema(description = "手机号", required = true)
    private String phone;

    @Schema(description = "短信验证码")
    private String smsCode;
}
