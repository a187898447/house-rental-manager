package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 水电账单创建DTO
 */
@Data
public class UtilityBillCreateDTO {

    private Long propertyId;

    private Long tenantId;

    /**
     * 账单月份（YYYY-MM）
     */
    private String billMonth;

    /**
     * 上期水表读数
     */
    private BigDecimal waterReading;

    /**
     * 本期水表读数
     */
    private BigDecimal waterReadingCurrent;

    /**
     * 水费（自动计算或手动填）
     */
    private BigDecimal waterAmount;

    /**
     * 上期电表读数
     */
    private BigDecimal electricityReading;

    /**
     * 本期电表读数
     */
    private BigDecimal electricityReadingCurrent;

    /**
     * 电费（自动计算或手动填）
     */
    private BigDecimal electricityAmount;

    /**
     * 来源：0-手动，1-API获取
     */
    private Integer source;

    private String remark;
}
