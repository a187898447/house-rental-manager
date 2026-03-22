package com.rental.bill.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 租客入住DTO
 */
@Data
public class TenantCreateDTO {
    
    private Long userId;
    
    private Long propertyId;
    
    private String name;
    
    private String phone;
    
    private String idCard;
    
    private LocalDate leaseStartDate;
    
    private LocalDate leaseEndDate;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private String remark;
}
