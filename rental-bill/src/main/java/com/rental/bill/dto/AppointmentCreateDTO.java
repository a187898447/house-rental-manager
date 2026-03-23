package com.rental.bill.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约看房创建DTO
 */
@Data
public class AppointmentCreateDTO {
    
    private Long propertyId;
    
    private String tenantName;
    
    private String tenantPhone;
    
    private LocalDateTime appointmentDate;
    
    private String remark;
}
