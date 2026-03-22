package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 租金账单创建DTO
 */
@Data
public class RentRecordCreateDTO {
    
    private Long tenantId;
    
    private Long propertyId;
    
    private BigDecimal amount;
    
    private Integer days;
    
    private BigDecimal dailyRate;
    
    private String payMonth;
    
    private String remark;
}
