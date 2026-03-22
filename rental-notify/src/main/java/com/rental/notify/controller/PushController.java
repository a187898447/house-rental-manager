package com.rental.notify.controller;

import com.rental.notify.service.NotifyService;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 消息推送控制器（供其他服务调用）
 */
@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
@Tag(name = "消息推送", description = "内部服务调用发送通知")
public class PushController {

    private final NotifyService notifyService;

    @PostMapping("/send")
    @Operation(summary = "发送通知")
    public Result<Long> send(
            @RequestParam Long userId,
            @RequestParam String type,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Long businessId,
            @RequestParam(required = false) String businessType) {
        Long id = notifyService.send(userId, type, title, content, businessId, businessType);
        return Result.success("通知发送成功", id);
    }
}
