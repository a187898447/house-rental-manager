package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.bill.entity.Receipt;
import com.rental.bill.vo.ReceiptVO;

/**
 * 收据服务接口
 */
public interface ReceiptService extends IService<Receipt> {

    /**
     * 房东收据列表
     */
    Page<ReceiptVO> getOwnerReceipts(Long ownerId, String type, String month, Integer page, Integer size);

    /**
     * 获取收据详情
     */
    ReceiptVO getDetail(Long id);
}