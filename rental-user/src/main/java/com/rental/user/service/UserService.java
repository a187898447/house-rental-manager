package com.rental.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;

/**
 * 用户服务接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录
     *
     * @param request 微信登录请求
     * @return 用户信息
     */
    User wxLogin(WxLoginRequest request);

    /**
     * 手机号验证码登录
     *
     * @param phone 手机号
     * @param code  验证码
     * @param role  角色：landlord-房东，tenant-住户
     * @return 用户信息
     */
    User phoneLogin(String phone, String code, String role);

    /**
     * 根据 openid 查询用户
     *
     * @param openid 微信 openid
     * @return 用户信息
     */
    User getByOpenid(String openid);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getByPhone(String phone);
}
