package com.rental.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据openid查询用户
     */
    User getByOpenid(String openid);

    /**
     * 微信登录
     */
    User wxLogin(String openid, String wxUnionid);

    /**
     * 绑定手机号
     */
    User bindPhone(Long userId, String phone);

    /**
     * 更新用户信息
     */
    User updateUser(User user);
}
