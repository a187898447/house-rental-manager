package com.rental.property.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_property")
public class Property {

    /**
     * 房源 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 房源名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 楼栋 ID
     */
    private Long buildingId;

    /**
     * 户型（如：3 室 2 厅 1 卫）
     */
    private String layout;

    /**
     * 面积（平方米）
     */
    private BigDecimal area;

    /**
     * 租金（元/月）
     */
    private BigDecimal rentAmount;

    /**
     * 状态：0-未出租，1-已出租
     */
    private Integer status;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;

    /**
     * 创建人 ID（房东 ID）
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
