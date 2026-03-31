package com.rental.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.user.entity.User;
import com.rental.user.vo.LoginVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录
     * @param code 微信授权 code
     * @return 登录响应
     */
    LoginVO wxLogin(String code);

    /**
     * 手机号登录（房东/住户）
     * @param phone 手机号
     * @param code 验证码
     * @param role 角色
     * @return 登录响应
     */
    LoginVO phoneLogin(String phone, String code, String role);

    /**
     * 绑定手机号
     * @param userId 用户ID
     * @param phone 手机号
     * @return 用户信息
     */
    User bindPhone(Long userId, String phone);

    /**
     * 根据openid获取用户
     */
    User getByOpenid(String openid);
}
