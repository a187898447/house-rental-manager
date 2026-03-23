package com.rental.user.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.common.result.Result;
import com.rental.user.dto.BindPhoneRequest;
import com.rental.user.dto.SendCodeRequest;
import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;
import com.rental.user.service.UserService;
import com.rental.user.vo.LoginVO;
import com.rental.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户登录、绑定、认证相关接口")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "微信登录")
    public Result<LoginVO> wxLogin(@RequestBody WxLoginRequest request) {
        LoginVO loginVO = userService.wxLogin(request.getCode());
        return Result.success(loginVO);
    }

    @PostMapping("/send-code")
    @Operation(summary = "发送验证码")
    public Result<Boolean> sendCode(@RequestBody SendCodeRequest request) {
        // TODO: 实现短信验证码发送逻辑
        // 1. 验证手机号格式
        // 2. 生成6位验证码
        // 3. 调用短信服务发送
        // 4. 存入Redis，设置过期时间
        return Result.success(true);
    }

    @PostMapping("/bind-phone")
    @Operation(summary = "绑定手机号")
    public Result<UserVO> bindPhone(@RequestBody BindPhoneRequest request, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.bindPhone(userId, request.getPhone());
        return Result.success(convertToVO(user));
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<UserVO> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getById(userId);
        return Result.success(convertToVO(user));
    }

    @PutMapping
    @Operation(summary = "更新用户信息")
    public Result<UserVO> updateUserInfo(@RequestBody User user, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        user.setId(userId);
        userService.updateById(user);
        User updatedUser = userService.getById(userId);
        return Result.success(convertToVO(updatedUser));
    }

    private UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        return vo;
    }
}
