package com.rental.property.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 水电费配置DTO
 */
@Data
public class UtilityConfigDTO {
    
    /**
     * 水费单价（元/吨）
     */
    private BigDecimal waterUnitPrice;
    
    /**
     * 电费单价（元/度）
     */
    private BigDecimal electricityUnitPrice;
    
    /**
     * 备注
     */
    private String remark;
}
