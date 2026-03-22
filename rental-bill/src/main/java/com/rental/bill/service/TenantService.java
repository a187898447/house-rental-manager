package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.TenantCreateDTO;
import com.rental.bill.vo.TenantVO;

/**
 * 租客服务接口
 */
public interface TenantService {
    
    /**
     * 入住登记
     */
    Long checkIn(TenantCreateDTO dto);
    
    /**
     * 租客详情
     */
    TenantVO getDetail(Long id);
    
    /**
     * 房源租客列表
     */
    Page<TenantVO> getPropertyTenants(Long propertyId, Integer status, Integer page, Integer size);
    
    /**
     * 房东所有租客
     */
    Page<TenantVO> getOwnerTenants(Long ownerId, Integer status, Integer page, Integer size);
    
    /**
     * 退租办理
     */
    boolean checkOut(Long id, String remark);
    
    /**
     * 删除租客
     */
    boolean delete(Long id);
}
