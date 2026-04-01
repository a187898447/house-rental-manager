package com.rental.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_user")
public class User {

    /**
     * 用户 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 微信 openid
     */
    private String openid;

    /**
     * 微信 unionid
     */
    private String unionid;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    /**
     * 角色：landlord-房东，tenant-住户
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
