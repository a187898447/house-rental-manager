package com.rental.property.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 楼栋实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_building")
public class Building {

    /**
     * 楼栋 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 楼栋名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 总层数
     */
    private Integer totalFloors;

    /**
     * 总房间数
     */
    private Integer totalRooms;

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
