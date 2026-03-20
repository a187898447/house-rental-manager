package com.rental.user.controller;

import com.rental.common.result.Result;
import com.rental.user.dto.BindPhoneRequest;
import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;
import com.rental.user.service.UserService;
import com.rental.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public Result<UserVO> wxLogin(@RequestBody WxLoginRequest request) {
        User user = userService.wxLogin(request.getOpenid(), request.getUnionid());
        return Result.success(convertToVO(user));
    }

    @PostMapping("/bind-phone")
    @Operation(summary = "绑定手机号")
    public Result<UserVO> bindPhone(@RequestBody BindPhoneRequest request) {
        User user = userService.bindPhone(request.getUserId(), request.getPhone());
        return Result.success(convertToVO(user));
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<UserVO> getUserInfo(@RequestParam Long userId) {
        User user = userService.getById(userId);
        return Result.success(convertToVO(user));
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public Result<UserVO> updateUserInfo(@RequestBody User user) {
        User updated = userService.updateUser(user);
        return Result.success(convertToVO(updated));
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
        return vo;
    }
}
