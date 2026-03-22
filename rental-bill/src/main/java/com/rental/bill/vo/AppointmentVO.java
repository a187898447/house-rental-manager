package com.rental.bill.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约看房视图对象
 */
@Data
public class AppointmentVO {
    
    private Long id;
    
    private Long propertyId;
    
    private String propertyTitle;
    
    private String propertyAddress;
    
    private String tenantName;
    
    private String tenantPhone;
    
    private LocalDateTime appointmentDate;
    
    private Integer status;
    
    private String statusName;
    
    private String remark;
    
    private LocalDateTime createdAt;
}
