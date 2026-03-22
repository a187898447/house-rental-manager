package com.rental.bill.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租客VO
 */
@Data
public class TenantVO {
    
    private Long id;
    
    private Long userId;
    
    private String nickname;
    
    private Long propertyId;
    
    private String propertyName;
    
    private String roomNumber;
    
    private String name;
    
    private String phone;
    
    private String idCard;
    
    private LocalDate leaseStartDate;
    
    private LocalDate leaseEndDate;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private Integer status;
    
    private String statusName;
    
    private String remark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
