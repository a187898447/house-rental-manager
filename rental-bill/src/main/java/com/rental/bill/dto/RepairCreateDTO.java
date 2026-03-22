package com.rental.bill.dto;

import lombok.Data;

/**
 * 报修创建DTO
 */
@Data
public class RepairCreateDTO {
    
    private Long tenantId;
    
    private Long propertyId;
    
    private String title;
    
    private String description;
    
    private String images;
    
    private String contactPhone;
}
