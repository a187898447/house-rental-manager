package com.rental.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.common.exception.BusinessException;
import com.rental.common.utils.JwtUtils;
import com.rental.user.entity.User;
import com.rental.user.mapper.UserMapper;
import com.rental.user.service.UserService;
import com.rental.user.service.WxService;
import com.rental.user.vo.LoginVO;
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

    private final WxService wxService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO wxLogin(String code) {
        // 1. 用 code 换取 openid
        String openid = wxService.getOpenid(code);
        
        // 2. 查询用户是否存在
        User user = getByOpenid(openid);
        
        if (user == null) {
            // 3. 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setRole("tenant"); // 默认租客
            user.setStatus(1); // 正常状态
            this.save(user);
            log.info("新用户注册: openid={}", openid);
        }

        // 4. 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }

        // 5. 生成 JWT Token
        String token = JwtUtils.generateToken(user.getId(), user.getOpenid(), user.getRole());

        // 6. 返回登录响应
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setRole(user.getRole());
        vo.setPhone(user.getPhone());

        log.info("用户登录成功: userId={}, role={}", user.getId(), user.getRole());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO phoneLogin(String phone, String code, String role) {
        // 1. 验证验证码（简化：开发环境固定验证码 123456）
        if (!"123456".equals(code) && !"666666".equals(code)) {
            // TODO: 生产环境应从Redis校验验证码
            throw new BusinessException("验证码错误");
        }

        // 2. 根据手机号查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));

        // 3. 如果用户不存在，创建虚拟账户
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setRole(role != null ? role : "tenant");
            user.setNickname(role != null && "landlord".equals(role) ? "房东用户" : "住户用户");
            user.setStatus(1);
            this.save(user);
            log.info("创建虚拟账户: phone={}, role={}", phone, role);
        }

        // 4. 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }

        // 5. 如果传了角色，更新用户角色
        if (role != null && !role.equals(user.getRole())) {
            user.setRole(role);
            this.updateById(user);
        }

        // 6. 生成 JWT Token
        String token = JwtUtils.generateToken(user.getId(), user.getOpenid() != null ? user.getOpenid() : phone, user.getRole());

        // 7. 返回登录响应
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setRole(user.getRole());
        vo.setPhone(user.getPhone());

        log.info("手机号登录成功: userId={}, role={}", user.getId(), user.getRole());
        return vo;
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
                .eq(User::getPhone, phone));
        if (existUser != null && !existUser.getId().equals(userId)) {
            throw new BusinessException("该手机号已被绑定");
        }

        user.setPhone(phone);
        this.updateById(user);
        log.info("用户绑定手机号: userId={}, phone={}", userId, phone);
        return user;
    }

    @Override
    public User getByOpenid(String openid) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid));
    }
}
