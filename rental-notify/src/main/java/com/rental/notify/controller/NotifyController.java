package com.rental.notify.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.notify.service.NotifyService;
import com.rental.notify.vo.NotifyMessageVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
@Tag(name = "通知管理", description = "通知消息相关接口")
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping("/list")
    @Operation(summary = "通知列表")
    public Result<Page<NotifyMessageVO>> getList(
            Authentication authentication,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer readStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = 1L; // TODO: 临时处理
        Page<NotifyMessageVO> result = notifyService.getList(userId, type, readStatus, page, size);
        return Result.success(result);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "未读数量")
    public Result<Long> getUnreadCount(Authentication authentication) {
        Long userId = 1L; // TODO: 临时处理
        long count = notifyService.getUnreadCount(userId);
        return Result.success(count);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记已读")
    @Parameter(name = "id", description = "通知ID")
    public Result<Boolean> markRead(@PathVariable Long id) {
        boolean result = notifyService.markRead(id);
        return Result.success(result);
    }

    @PutMapping("/read-all")
    @Operation(summary = "全部已读")
    public Result<Boolean> markAllRead(Authentication authentication) {
        Long userId = 1L; // TODO: 临时处理
        boolean result = notifyService.markAllRead(userId);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知")
    @Parameter(name = "id", description = "通知ID")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = notifyService.delete(id);
        return Result.success(result);
    }
}
