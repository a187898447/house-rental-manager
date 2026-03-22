package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同VO
 */
@Data
public class ContractVO {
    
    private Long id;
    
    private Long tenantId;
    
    private String tenantName;
    
    private String tenantPhone;
    
    private Long propertyId;
    
    private String propertyName;
    
    private String roomNumber;
    
    private Long ownerId;
    
    private String ownerName;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private BigDecimal waterFee;
    
    private BigDecimal electricityFee;
    
    private String otherFees;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String signUrl;
    
    private Integer status;
    
    private String statusName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
