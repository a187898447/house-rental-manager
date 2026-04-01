package com.rental.user.controller;

import com.rental.common.result.Result;
import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;
import com.rental.user.service.UserService;
import com.rental.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户登录、认证相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 微信一键登录
     *
     * @param request 微信登录请求
     * @return 用户信息
     */
    @PostMapping("/wx-login")
    @Operation(summary = "微信一键登录")
    public Result<UserVO> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        log.info("微信登录请求：openid={}", request.getOpenid());
        User user = userService.wxLogin(request);
        UserVO userVO = convertToVO(user);
        return Result.success(userVO);
    }

    /**
     * 手机号验证码登录
     *
     * @param phone 手机号
     * @param code  验证码
     * @param role  角色：landlord-房东，tenant-住户
     * @return 用户信息
     */
    @PostMapping("/phone-login")
    @Operation(summary = "手机号验证码登录")
    public Result<UserVO> phoneLogin(
            @RequestParam String phone,
            @RequestParam String code,
            @RequestParam String role) {
        log.info("手机号登录请求：phone={}, role={}", phone, role);
        User user = userService.phoneLogin(phone, code, role);
        UserVO userVO = convertToVO(user);
        return Result.success(userVO);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<UserVO> getUserInfo(@RequestParam Long userId) {
        log.info("获取用户信息：userId={}", userId);
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        UserVO userVO = convertToVO(user);
        return Result.success(userVO);
    }

    /**
     * 转换为用户 VO
     *
     * @param user 用户实体
     * @return 用户 VO
     */
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
