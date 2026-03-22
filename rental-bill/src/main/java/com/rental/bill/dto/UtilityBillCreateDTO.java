package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 水电账单创建DTO
 */
@Data
public class UtilityBillCreateDTO {
    
    private Long tenantId;
    
    private Long propertyId;
    
    private String billMonth;
    
    private BigDecimal waterReading;
    
    private BigDecimal electricityReading;
    
    private BigDecimal waterAmount;
    
    private BigDecimal electricityAmount;
    
    private String remark;
}
