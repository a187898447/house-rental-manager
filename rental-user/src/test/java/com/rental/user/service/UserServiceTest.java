package com.rental.user.service;

import com.rental.user.dto.WxLoginRequest;
import com.rental.user.entity.User;
import com.rental.user.mapper.UserMapper;
import com.rental.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private WxLoginRequest testRequest;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setOpenid("test_openid_123");
        testUser.setPhone("13800138000");
        testUser.setNickname("测试用户");
        testUser.setAvatar("avatar.jpg");
        testUser.setRole("user");
        testUser.setCreatedAt(LocalDateTime.now());

        testRequest = new WxLoginRequest();
        testRequest.setCode("test_wx_code");
        testRequest.setEncryptedData("test_encrypted_data");
        testRequest.setIv("test_iv");
    }

    @Test
    void testWxLogin_NewUser() {
        // Arrange
        when(userMapper.selectByOpenid("test_openid_123")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);
        when(userMapper.selectById(anyLong())).thenReturn(testUser);

        // Act
        User result = userService.wxLogin(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("test_openid_123", result.getOpenid());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testWxLogin_ExistingUser() {
        // Arrange
        when(userMapper.selectByOpenid("test_openid_123")).thenReturn(testUser);

        // Act
        User result = userService.wxLogin(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userMapper, times(0)).insert(any(User.class));
    }

    @Test
    void testWxLogin_NullRequest() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.wxLogin(null);
        });
    }

    @Test
    void testPhoneLogin_NewUser() {
        // Arrange
        when(userMapper.selectByPhone("13800138000")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);
        when(userMapper.selectById(anyLong())).thenReturn(testUser);

        // Act
        User result = userService.phoneLogin("13800138000", "123456", "user");

        // Assert
        assertNotNull(result);
        assertEquals("13800138000", result.getPhone());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testPhoneLogin_ExistingUser() {
        // Arrange
        when(userMapper.selectByPhone("13800138000")).thenReturn(testUser);

        // Act
        User result = userService.phoneLogin("13800138000", "123456", "user");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userMapper, times(0)).insert(any(User.class));
    }

    @Test
    void testPhoneLogin_InvalidCode() {
        // Arrange
        when(userMapper.selectByPhone("13800138000")).thenReturn(null);

        // Act & Assert - 验证码错误应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            userService.phoneLogin("13800138000", "wrong_code", "user");
        });
    }

    @Test
    void testGetByOpenid_Found() {
        // Arrange
        when(userMapper.selectByOpenid("test_openid_123")).thenReturn(testUser);

        // Act
        User result = userService.getByOpenid("test_openid_123");

        // Assert
        assertNotNull(result);
        assertEquals("test_openid_123", result.getOpenid());
    }

    @Test
    void testGetByOpenid_NotFound() {
        // Arrange
        when(userMapper.selectByOpenid("not_exist")).thenReturn(null);

        // Act
        User result = userService.getByOpenid("not_exist");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetByPhone_Found() {
        // Arrange
        when(userMapper.selectByPhone("13800138000")).thenReturn(testUser);

        // Act
        User result = userService.getByPhone("13800138000");

        // Assert
        assertNotNull(result);
        assertEquals("13800138000", result.getPhone());
    }

    @Test
    void testGetByPhone_NotFound() {
        // Arrange
        when(userMapper.selectByPhone("not_exist")).thenReturn(null);

        // Act
        User result = userService.getByPhone("not_exist");

        // Assert
        assertNull(result);
    }
}
