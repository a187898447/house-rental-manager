package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RepairCreateDTO;
import com.rental.bill.vo.RepairVO;

/**
 * 报修服务接口
 */
public interface RepairService {
    
    /**
     * 提交报修
     */
    Long create(RepairCreateDTO dto);
    
    /**
     * 报修详情
     */
    RepairVO getDetail(Long id);
    
    /**
     * 租客报修列表
     */
    Page<RepairVO> getTenantRepairs(Long tenantId, Integer status, Integer page, Integer size);
    
    /**
     * 房东报修列表
     */
    Page<RepairVO> getOwnerRepairs(Long ownerId, Long propertyId, Integer status, Integer page, Integer size);
    
    /**
     * 开始处理
     */
    boolean startProcess(Long id, Long handlerId);
    
    /**
     * 完成处理
     */
    boolean complete(Long id, String remark);
    
    /**
     * 取消报修
     */
    boolean cancel(Long id);
}
