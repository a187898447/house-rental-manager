package com.rental.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.user.dto.TenantDTO;
import com.rental.user.entity.Tenant;
import com.rental.user.mapper.TenantMapper;
import com.rental.user.service.impl.TenantServiceImpl;
import com.rental.user.vo.TenantVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 租客服务单元测试
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private TenantMapper tenantMapper;

    @InjectMocks
    private TenantServiceImpl tenantService;

    private Tenant testTenant;
    private TenantDTO testDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testTenant = new Tenant();
        testTenant.setId(1L);
        testTenant.setLandlordId(100L);
        testTenant.setPropertyId(200L);
        testTenant.setTenantName("张三");
        testTenant.setTenantPhone("13800138000");
        testTenant.setLeaseStart(LocalDate.of(2026, 4, 1));
        testTenant.setLeaseEnd(LocalDate.of(2027, 4, 1));
        testTenant.setDeposit(6000.0);
        testTenant.setStatus(1); // 正常

        testDTO = new TenantDTO();
        testDTO.setLandlordId(100L);
        testDTO.setPropertyId(200L);
        testDTO.setTenantName("张三");
        testDTO.setTenantPhone("13800138000");
        testDTO.setLeaseStart(LocalDate.of(2026, 4, 1));
        testDTO.setLeaseEnd(LocalDate.of(2027, 4, 1));
        testDTO.setDeposit(6000.0);
    }

    @Test
    void testCheckIn_Success() {
        // Arrange
        when(tenantMapper.insert(any(Tenant.class))).thenReturn(1);
        when(tenantMapper.selectById(anyLong())).thenReturn(testTenant);

        // Act
        Long tenantId = tenantService.checkIn(testDTO);

        // Assert
        assertNotNull(tenantId);
        assertEquals(1L, tenantId);
        verify(tenantMapper, times(1)).insert(any(Tenant.class));
    }

    @Test
    void testCheckIn_NullDTO() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.checkIn(null);
        });
    }

    @Test
    void testCheckOut_Success() {
        // Arrange
        when(tenantMapper.selectById(1L)).thenReturn(testTenant);
        when(tenantMapper.updateById(any(Tenant.class))).thenReturn(1);

        // Act
        tenantService.checkOut(1L, LocalDate.of(2026, 6, 30));

        // Assert
        verify(tenantMapper, times(1)).selectById(1L);
        verify(tenantMapper, times(1)).updateById(any(Tenant.class));
    }

    @Test
    void testCheckOut_TenantNotFound() {
        // Arrange
        when(tenantMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.checkOut(999L, LocalDate.of(2026, 6, 30));
        });
    }

    @Test
    void testGetTenantList_Success() {
        // Arrange
        List<Tenant> tenantList = new ArrayList<>();
        tenantList.add(testTenant);

        Page<Tenant> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(tenantList);

        // 模拟分页查询
        when(tenantMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<TenantVO> result = tenantService.getTenantList(100L, 1, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetTenantList_EmptyResult() {
        // Arrange
        Page<Tenant> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(tenantMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<TenantVO> result = tenantService.getTenantList(100L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetTenantDetail_Success() {
        // Arrange
        when(tenantMapper.selectById(1L)).thenReturn(testTenant);

        // Act
        TenantVO result = tenantService.getTenantDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("张三", result.getTenantName());
        assertEquals("13800138000", result.getTenantPhone());
    }

    @Test
    void testGetTenantDetail_NotFound() {
        // Arrange
        when(tenantMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.getTenantDetail(999L);
        });
    }
}
