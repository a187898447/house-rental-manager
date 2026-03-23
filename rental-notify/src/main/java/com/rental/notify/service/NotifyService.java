package com.rental.notify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.notify.vo.NotifyMessageVO;

/**
 * 通知服务接口
 */
public interface NotifyService {
    
    /**
     * 发送通知
     */
    Long send(Long userId, String type, String title, String content, Long businessId, String businessType);
    
    /**
     * 通知列表
     */
    Page<NotifyMessageVO> getList(Long userId, String type, Integer readStatus, Integer page, Integer size);
    
    /**
     * 标记已读
     */
    boolean markRead(Long id);
    
    /**
     * 标记全部已读
     */
    boolean markAllRead(Long userId);
    
    /**
     * 未读数量
     */
    long getUnreadCount(Long userId);
    
    /**
     * 删除通知
     */
    boolean delete(Long id);
}
