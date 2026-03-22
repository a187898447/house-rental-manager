package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordCreateDTO;
import com.rental.bill.vo.RentRecordVO;

/**
 * 租金账单服务接口
 */
public interface RentRecordService {
    
    /**
     * 创建租金账单
     */
    Long create(RentRecordCreateDTO dto);
    
    /**
     * 账单详情
     */
    RentRecordVO getDetail(Long id);
    
    /**
     * 房东账单列表
     */
    Page<RentRecordVO> getOwnerBills(Long ownerId, Long propertyId, Integer status, String month, Integer page, Integer size);
    
    /**
     * 租客账单列表
     */
    Page<RentRecordVO> getTenantBills(Long tenantId, Integer status, Integer page, Integer size);
    
    /**
     * 标记已支付
     */
    boolean markPaid(Long id);
    
    /**
     * 发送催租提醒
     */
    boolean sendReminder(Long id);
    
    /**
     * 取消账单
     */
    boolean cancel(Long id);
}
