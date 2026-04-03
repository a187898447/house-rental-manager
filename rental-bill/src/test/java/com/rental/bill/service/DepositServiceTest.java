package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.DepositDTO;
import com.rental.bill.entity.Deposit;
import com.rental.bill.mapper.DepositMapper;
import com.rental.bill.service.impl.DepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 押金服务单元测试
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    private DepositMapper depositMapper;

    @InjectMocks
    private DepositServiceImpl depositService;

    private Deposit testDeposit;
    private DepositDTO testDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testDeposit = new Deposit();
        testDeposit.setId(1L);
        testDeposit.setTenantId(1L);
        testDeposit.setAmount(new BigDecimal("6000.00"));
        testDeposit.setType("checkin");
        testDeposit.setStatus(0); // 未退还
        testDeposit.setCreatedAt(LocalDate.of(2026, 4, 1));
        testDeposit.setRefundAt(null);

        testDTO = new DepositDTO();
        testDTO.setTenantId(1L);
        testDTO.setAmount(new BigDecimal("6000.00"));
        testDTO.setType("checkin");
    }

    @Test
    void testCreateDeposit_Success() {
        // Arrange
        when(depositMapper.insert(any(Deposit.class))).thenReturn(1);
        when(depositMapper.selectById(anyLong())).thenReturn(testDeposit);

        // Act
        Long depositId = depositService.createDeposit(testDTO);

        // Assert
        assertNotNull(depositId);
        assertEquals(1L, depositId);
        verify(depositMapper, times(1)).insert(any(Deposit.class));
    }

    @Test
    void testCreateDeposit_NullDTO() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            depositService.createDeposit(null);
        });
    }

    @Test
    void testRefundDeposit_Success() {
        // Arrange
        when(depositMapper.selectById(1L)).thenReturn(testDeposit);
        when(depositMapper.updateById(any(Deposit.class))).thenReturn(1);

        // Act
        depositService.refundDeposit(1L);

        // Assert
        verify(depositMapper, times(1)).selectById(1L);
        verify(depositMapper, times(1)).updateById(any(Deposit.class));
    }

    @Test
    void testRefundDeposit_NotFound() {
        // Arrange
        when(depositMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            depositService.refundDeposit(999L);
        });
    }

    @Test
    void testRefundDeposit_AlreadyRefunded() {
        // Arrange
        Deposit refundedDeposit = new Deposit();
        refundedDeposit.setId(1L);
        refundedDeposit.setStatus(1); // 已退还

        when(depositMapper.selectById(1L)).thenReturn(refundedDeposit);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            depositService.refundDeposit(1L);
        });
    }

    @Test
    void testGetDepositList_Success() {
        // Arrange
        List<Deposit> depositList = new ArrayList<>();
        depositList.add(testDeposit);

        Page<Deposit> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(depositList);

        when(depositMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<Deposit> result = depositService.getDepositList(1L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetDepositList_ByStatus() {
        // Arrange
        List<Deposit> depositList = new ArrayList<>();
        depositList.add(testDeposit);

        Page<Deposit> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(depositList);

        when(depositMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<Deposit> result = depositService.getDepositList(1L, 0, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetDepositList_EmptyResult() {
        // Arrange
        Page<Deposit> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(depositMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<Deposit> result = depositService.getDepositList(1L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetDepositDetail_Success() {
        // Arrange
        when(depositMapper.selectById(1L)).thenReturn(testDeposit);

        // Act
        Deposit result = depositService.getDepositDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("6000.00"), result.getAmount());
    }

    @Test
    void testGetDepositDetail_NotFound() {
        // Arrange
        when(depositMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            depositService.getDepositDetail(999L);
        });
    }
}
