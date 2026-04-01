package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 水电单价配置实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_utility_price")
public class UtilityPrice {

    /**
     * 配置 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 房源 ID
     */
    private Long propertyId;

    /**
     * 类型：1-水费，2-电费
     */
    private Integer type;

    /**
     * 单价（元/度或元/吨）
     */
    private BigDecimal unitPrice;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

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
