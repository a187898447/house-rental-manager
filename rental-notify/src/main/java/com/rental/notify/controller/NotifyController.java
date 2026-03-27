package com.rental.notify.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.notify.service.NotifyService;
import com.rental.notify.vo.NotifyMessageVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer readStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userIdLong = Long.valueOf(userId);
        Page<NotifyMessageVO> result = notifyService.getList(userIdLong, type, readStatus, page, size);
        return Result.success(result);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "未读数量")
    public Result<Long> getUnreadCount(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        Long userIdLong = Long.valueOf(userId);
        long count = notifyService.getUnreadCount(userIdLong);
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
    public Result<Boolean> markAllRead(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        Long userIdLong = Long.valueOf(userId);
        boolean result = notifyService.markAllRead(userIdLong);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知")
    @Parameter(name = "id", description = "通知ID")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = notifyService.delete(id);
        return Result.success(result);
    }

    @GetMapping("/settings")
    @Operation(summary = "获取通知设置")
    public Result<java.util.Map<String, Object>> getSettings(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId) {
        java.util.Map<String, Object> settings = notifyService.getSettings(Long.valueOf(userId));
        return Result.success(settings);
    }

    @PutMapping("/settings")
    @Operation(summary = "更新通知设置")
    public Result<Boolean> updateSettings(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") String userId,
            @RequestBody java.util.Map<String, Object> settings) {
        boolean result = notifyService.updateSettings(Long.valueOf(userId), settings);
        return Result.success(result);
    }
}
