package com.rental.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.user.entity.User;
import com.rental.user.mapper.UserMapper;
import com.rental.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByOpenid(String openid) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid)
                .isNull(User::getDeletedAt));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User wxLogin(String openid, String wxUnionid) {
        User user = getByOpenid(openid);
        if (user != null) {
            log.info("用户已存在，登录成功: {}", openid);
            return user;
        }

        // 创建新用户
        user = new User();
        user.setOpenid(openid);
        user.setWxUnionid(wxUnionid);
        user.setRole(0); // 默认普通用户
        this.save(user);
        log.info("新用户注册: {}", openid);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User bindPhone(Long userId, String phone) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查手机号是否已被绑定
        User existUser = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
                .isNull(User::getDeletedAt));
        if (existUser != null && !existUser.getId().equals(userId)) {
            throw new BusinessException("该手机号已被绑定");
        }

        user.setPhone(phone);
        this.updateById(user);
        log.info("用户绑定手机号: userId={}, phone={}", userId, phone);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        User existUser = this.getById(user.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        this.updateById(user);
        return this.getById(user.getId());
    }
}
