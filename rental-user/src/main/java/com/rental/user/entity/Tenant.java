package com.rental.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租客实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_tenant")
public class Tenant {

    /**
     * 租客 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 房源 ID
     */
    private Long propertyId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 退租日期
     */
    private LocalDate checkOutDate;

    /**
     * 状态：0-在住，1-已退租
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
