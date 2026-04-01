package com.rental.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;
import com.rental.user.mapper.UserMapper;
import com.rental.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 开发环境固定验证码
     */
    private static final String DEV_VERIFY_CODE = "123456";

    @Override
    public User wxLogin(WxLoginRequest request) {
        log.info("微信登录请求：openid={}, unionid={}", request.getOpenid(), request.getUnionid());

        // 1. 先根据 openid 查询用户
        User user = baseMapper.selectByOpenid(request.getOpenid());
        if (user != null) {
            log.info("用户已存在：userId={}", user.getId());
            // 更新用户信息
            if (request.getNickname() != null) {
                user.setNickname(request.getNickname());
            }
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }
            user.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(user);
            return user;
        }

        // 2. openid 不存在，尝试根据 unionid 查询（同一微信主体不同应用）
        if (request.getUnionid() != null) {
            user = baseMapper.selectByUnionid(request.getUnionid());
            if (user != null) {
                log.info("根据 unionid 找到用户：userId={}", user.getId());
                // 绑定 openid
                user.setOpenid(request.getOpenid());
                if (request.getNickname() != null) {
                    user.setNickname(request.getNickname());
                }
                if (request.getAvatarUrl() != null) {
                    user.setAvatarUrl(request.getAvatarUrl());
                }
                user.setUpdateTime(LocalDateTime.now());
                baseMapper.updateById(user);
                return user;
            }
        }

        // 3. 新用户，创建账户
        log.info("创建新用户：openid={}", request.getOpenid());
        user = new User();
        user.setOpenid(request.getOpenid());
        user.setUnionid(request.getUnionid());
        user.setNickname(request.getNickname());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(user);

        log.info("新用户创建成功：userId={}", user.getId());
        return user;
    }

    @Override
    public User phoneLogin(String phone, String code, String role) {
        log.info("手机号登录请求：phone={}, role={}", phone, role);

        // 1. 验证验证码（开发环境固定为 123456）
        if (!DEV_VERIFY_CODE.equals(code)) {
            log.warn("验证码错误：phone={}, code={}", phone, code);
            throw new BusinessException(400, "验证码错误");
        }

        // 2. 查询用户
        User user = baseMapper.selectByPhone(phone);
        if (user != null) {
            log.info("用户已存在：userId={}", user.getId());
            // 如果用户已有角色，不允许修改
            if (user.getRole() != null && !user.getRole().equals(role)) {
                log.warn("用户角色已存在，不允许修改：userId={}, existingRole={}, newRole={}",
                        user.getId(), user.getRole(), role);
            }
            return user;
        }

        // 3. 新用户，创建账户
        log.info("创建新用户：phone={}, role={}", phone, role);
        user = new User();
        user.setPhone(phone);
        user.setRole(role);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(user);

        log.info("新用户创建成功：userId={}", user.getId());
        return user;
    }

    @Override
    public User getByOpenid(String openid) {
        return baseMapper.selectByOpenid(openid);
    }

    @Override
    public User getByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }
}
