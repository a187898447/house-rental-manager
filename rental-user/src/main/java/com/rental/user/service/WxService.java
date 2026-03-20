package com.rental.user.service;

import com.rental.user.entity.User;

/**
 * 微信服务接口
 */
public interface WxService {

    /**
     * 用 code 换取 openid
     * @param code 微信授权 code
     * @return openid
     */
    String getOpenid(String code);
}
