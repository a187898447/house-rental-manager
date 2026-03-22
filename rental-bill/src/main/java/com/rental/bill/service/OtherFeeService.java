package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.vo.OtherFeeVO;
import java.math.BigDecimal;

public interface OtherFeeService {
    Long create(Long tenantId, Long propertyId, String feeType, BigDecimal amount, String billMonth);
    OtherFeeVO getDetail(Long id);
    Page<OtherFeeVO> getTenantFees(Long tenantId, Integer status, Integer page, Integer size);
    Page<OtherFeeVO> getOwnerFees(Long ownerId, Integer status, Integer page, Integer size);
    boolean pay(Long id);
}
