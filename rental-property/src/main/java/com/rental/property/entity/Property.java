package com.rental.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源实体
 */
@Data
@TableName("property")
public class Property {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ownerId;

    private Long buildingId;

    /**
     * 房源名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 楼栋（例：1栋）
     */
    private String building;

    /**
     * 户型（如：2室1厅）
     */
    private String type;

    private String unit;

    private String roomNumber;

    private BigDecimal area;

    private BigDecimal rentAmount;

    private BigDecimal depositAmount;

    /**
     * 0:未出租 1:已出租
     */
    private Integer status;

    private BigDecimal dailyRate;

    /**
     * 水费单价（元/吨）
     */
    private BigDecimal waterUnitPrice;

    /**
     * 电费单价（元/度）
     */
    private BigDecimal electricityUnitPrice;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
