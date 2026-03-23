package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同创建DTO
 */
@Data
public class ContractCreateDTO {
    
    private Long tenantId;
    
    private Long propertyId;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private BigDecimal waterFee;
    
    private BigDecimal electricityFee;
    
    private String otherFees;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
}
