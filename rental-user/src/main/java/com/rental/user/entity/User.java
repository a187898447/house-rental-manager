package com.rental.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;

    private String phone;

    private String nickname;

    private String avatarUrl;

    /**
     * 角色: tenant/landlord/admin
     */
    private String role;

    /**
     * 状态: 0禁用 1正常
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private LocalDateTime deletedAt;
}