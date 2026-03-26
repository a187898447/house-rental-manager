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

    @TableLogic
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ownerId;

    private Long buildingId;

    private String name;
    private String address;
    private String building;
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

    /**
     * 水费单价（元/吨）
     */
    private BigDecimal waterPrice;

    /**
     * 电费单价（元/度）
     */
    private BigDecimal electricityPrice;

    private BigDecimal dailyRate;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
