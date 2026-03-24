package com.rental.property.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 房源更新DTO
 */
@Data
public class PropertyUpdateDTO {
    
    private Long buildingId;

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
    
    private BigDecimal dailyRate;
    
    private String remark;
}
