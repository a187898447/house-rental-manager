package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.OtherFeeCreateDTO;
import com.rental.bill.vo.OtherFeeVO;

public interface OtherFeeService {
    Long create(OtherFeeCreateDTO dto);
    OtherFeeVO getDetail(Long id);
    Page<OtherFeeVO> getTenantFees(Long tenantId, Integer status, Integer page, Integer size);
    Page<OtherFeeVO> getOwnerFees(Long ownerId, Integer status, Integer page, Integer size);
    boolean pay(Long id);
    boolean delete(Long id);
    
    /**
     * 获取房东其他费用总收入
     */
    java.math.BigDecimal getTotalByOwner(Long ownerId);
}
