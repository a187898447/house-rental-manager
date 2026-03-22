package com.rental.common.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 通知服务客户端 - 供其他服务调用发送通知
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyClient {

    private final RestTemplate restTemplate;

    @Value("${notify.service.url:http://rental-notify}")
    private String notifyServiceUrl;

    /**
     * 发送通知
     */
    public void sendNotify(Long userId, String type, String title, String content, Long businessId, String businessType) {
        try {
            String url = notifyServiceUrl + "/push/send?userId={1}&type={2}&title={3}&content={4}&businessId={5}&businessType={6}";
            restTemplate.postForObject(url, null, Long.class, userId, type, title, content, businessId, businessType);
            log.info("通知发送成功: userId={}, type={}", userId, type);
        } catch (Exception e) {
            log.error("通知发送失败: userId={}, type={}, error={}", userId, type, e.getMessage());
        }
    }

    /**
     * 预约通知 - 通知房东
     */
    public void notifyAppointmentCreated(Long ownerId, String propertyName, String tenantName, String phone) {
        sendNotify(ownerId, "appointment", "新预约看房", 
                String.format("租客 %s(%s) 预约看房: %s", tenantName, phone, propertyName), 
                null, "appointment");
    }

    /**
     * 账单通知 - 通知租客
     */
    public void notifyBillCreated(Long tenantId, String propertyName, String amount, String payMonth) {
        sendNotify(tenantId, "bill", "新账单生成", 
                String.format("您有新的租金账单: %s %s元", propertyName, amount), 
                null, "bill");
    }

    /**
     * 合同通知 - 通知双方
     */
    public void notifyContractSigned(Long userId, String propertyName, boolean isOwner) {
        String title = isOwner ? "合同已签署" : "合同签署成功";
        String content = isOwner ? 
                String.format("租客已签署合同: %s", propertyName) :
                String.format("您的合同已签署: %s", propertyName);
        sendNotify(userId, "contract", title, content, null, "contract");
    }

    /**
     * 报修通知 - 通知房东
     */
    public void notifyRepairCreated(Long ownerId, String propertyName, String title) {
        sendNotify(ownerId, "repair", "新报修单", 
                String.format("您有新的报修单: %s - %s", propertyName, title), 
                null, "repair");
    }
}
