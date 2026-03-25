package com.rental.property.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源视图对象
 */
@Data
public class PropertyVO {
    
    private Long id;
    
    private Long ownerId;
    
    private Long buildingId;

    /**
     * 楼栋名称（来自楼栋表）
     */
    private String buildingName;

    /**
     * 房源名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 楼栋（例：1栋）
     */
    private String building;

    /**
     * 户型（例：2室1厅）
     */
    private String type;

    private String unit;
    
    private String roomNumber;
    
    private BigDecimal area;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private Integer status;
    
    private String statusName;
    
    private BigDecimal dailyRate;
    
    private String remark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
