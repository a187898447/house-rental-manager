package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同更新DTO
 */
@Data
public class ContractUpdateDTO {
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private BigDecimal waterFee;
    
    private BigDecimal electricityFee;
    
    private String otherFees;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String signUrl;
    
    private Integer status;
}
