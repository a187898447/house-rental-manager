package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.ContractCreateDTO;
import com.rental.bill.dto.ContractUpdateDTO;
import com.rental.bill.vo.ContractVO;

/**
 * 合同服务接口
 */
public interface ContractService {
    
    /**
     * 创建合同
     */
    Long create(ContractCreateDTO dto);
    
    /**
     * 合同详情
     */
    ContractVO getDetail(Long id);
    
    /**
     * 房东合同列表
     */
    Page<ContractVO> getOwnerContracts(Long ownerId, Integer page, Integer size);
    
    /**
     * 租客合同列表
     */
    Page<ContractVO> getTenantContracts(Long tenantId, Integer page, Integer size);
    
    /**
     * 更新合同
     */
    boolean update(Long id, ContractUpdateDTO dto);
    
    /**
     * 签署合同
     */
    boolean sign(Long id, String signUrl);
    
    /**
     * 更新合同状态
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 解除合同
     */
    boolean terminate(Long id);
    
    /**
     * 获取合同签署URL（用于微信小程序签名）
     */
    String getSignUrl(Long id);
}
