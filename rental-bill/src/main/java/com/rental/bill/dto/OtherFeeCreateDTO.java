package com.rental.bill.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 其他费用创建DTO
 */
@Data
public class OtherFeeCreateDTO {

    private Long tenantId;

    private Long propertyId;

    private String feeType;

    private BigDecimal amount;

    private String billMonth;

    private String remark;
}
