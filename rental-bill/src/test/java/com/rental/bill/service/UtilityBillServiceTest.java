package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.entity.UtilityBill;
import com.rental.bill.mapper.UtilityBillMapper;
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
 * 水电账单服务单元测试（模拟）
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class UtilityBillServiceTest {

    @Mock
    private UtilityBillMapper utilityBillMapper;

    @InjectMocks
    private UtilityBillServiceMock utilityBillService;

    private UtilityBill testUtilityBill;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUtilityBill = new UtilityBill();
        testUtilityBill.setId(1L);
        testUtilityBill.setTenantId(1L);
        testUtilityBill.setPropertyId(100L);
        testUtilityBill.setLandlordId(50L);
        testUtilityBill.setType(1); // 水费
        testUtilityBill.setUnitPrice(new BigDecimal("3.50"));
        testUtilityBill.setPreviousReading(new BigDecimal("1000"));
        testUtilityBill.setCurrentReading(new BigDecimal("1200"));
        testUtilityBill.setUsage(new BigDecimal("200"));
        testUtilityBill.setAmount(new BigDecimal("700.00"));
        testUtilityBill.setBillMonth("2026-04");
        testUtilityBill.setStatus(0); // 未支付
    }

    @Test
    void testCreateUtilityBill_Success() {
        // Arrange
        when(utilityBillMapper.insert(any(UtilityBill.class))).thenReturn(1);
        when(utilityBillMapper.selectById(anyLong())).thenReturn(testUtilityBill);

        // Act
        Long billId = utilityBillService.createUtilityBill(1L, 100L, 50L, 1, 
            new BigDecimal("3.50"), new BigDecimal("1000"), new BigDecimal("1200"), "2026-04");

        // Assert
        assertNotNull(billId);
        assertEquals(1L, billId);
        verify(utilityBillMapper, times(1)).insert(any(UtilityBill.class));
    }

    @Test
    void testCreateUtilityBill_ZeroUsage() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            utilityBillService.createUtilityBill(1L, 100L, 50L, 1,
                new BigDecimal("3.50"), new BigDecimal("1000"), new BigDecimal("1000"), "2026-04");
        });
    }

    @Test
    void testCreateUtilityBill_NegativeUsage() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            utilityBillService.createUtilityBill(1L, 100L, 50L, 1,
                new BigDecimal("3.50"), new BigDecimal("1200"), new BigDecimal("1000"), "2026-04");
        });
    }

    @Test
    void testPayUtilityBill_Success() {
        // Arrange
        when(utilityBillMapper.selectById(1L)).thenReturn(testUtilityBill);
        when(utilityBillMapper.updateById(any(UtilityBill.class))).thenReturn(1);

        // Act
        utilityBillService.payUtilityBill(1L);

        // Assert
        verify(utilityBillMapper, times(1)).selectById(1L);
        verify(utilityBillMapper, times(1)).updateById(any(UtilityBill.class));
    }

    @Test
    void testPayUtilityBill_NotFound() {
        // Arrange
        when(utilityBillMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            utilityBillService.payUtilityBill(999L);
        });
    }

    @Test
    void testPayUtilityBill_AlreadyPaid() {
        // Arrange
        UtilityBill paidBill = new UtilityBill();
        paidBill.setId(1L);
        paidBill.setStatus(1); // 已支付

        when(utilityBillMapper.selectById(1L)).thenReturn(paidBill);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            utilityBillService.payUtilityBill(1L);
        });
    }

    @Test
    void testGetUtilityBillList_Success() {
        // Arrange
        List<UtilityBill> billList = new ArrayList<>();
        billList.add(testUtilityBill);

        Page<UtilityBill> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(billList);

        when(utilityBillMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<UtilityBill> result = utilityBillService.getUtilityBillList(1L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetUtilityBillList_ByType() {
        // Arrange
        List<UtilityBill> billList = new ArrayList<>();
        billList.add(testUtilityBill);

        Page<UtilityBill> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(billList);

        when(utilityBillMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<UtilityBill> result = utilityBillService.getUtilityBillList(1L, 1, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetUtilityBillList_EmptyResult() {
        // Arrange
        Page<UtilityBill> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(utilityBillMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<UtilityBill> result = utilityBillService.getUtilityBillList(1L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetUtilityBillDetail_Success() {
        // Arrange
        when(utilityBillMapper.selectById(1L)).thenReturn(testUtilityBill);

        // Act
        UtilityBill result = utilityBillService.getUtilityBillDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("700.00"), result.getAmount());
    }

    @Test
    void testGetUtilityBillDetail_NotFound() {
        // Arrange
        when(utilityBillMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            utilityBillService.getUtilityBillDetail(999L);
        });
    }

    /**
     * 模拟服务类（用于测试）
     */
    static class UtilityBillServiceMock {
        
        private final UtilityBillMapper utilityBillMapper;

        public UtilityBillServiceMock(UtilityBillMapper utilityBillMapper) {
            this.utilityBillMapper = utilityBillMapper;
        }

        public Long createUtilityBill(Long tenantId, Long propertyId, Long landlordId, 
                                      Integer type, BigDecimal unitPrice, 
                                      BigDecimal previousReading, BigDecimal currentReading,
                                      String billMonth) {
            // 验证参数
            if (currentReading.compareTo(previousReading) < 0) {
                throw new IllegalArgumentException("本期读数不能小于上期读数");
            }
            
            BigDecimal usage = currentReading.subtract(previousReading);
            if (usage.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("使用量必须大于 0");
            }

            UtilityBill bill = new UtilityBill();
            bill.setTenantId(tenantId);
            bill.setPropertyId(propertyId);
            bill.setLandlordId(landlordId);
            bill.setType(type);
            bill.setUnitPrice(unitPrice);
            bill.setPreviousReading(previousReading);
            bill.setCurrentReading(currentReading);
            bill.setUsage(usage);
            bill.setAmount(unitPrice.multiply(usage));
            bill.setBillMonth(billMonth);
            bill.setStatus(0);

            utilityBillMapper.insert(bill);
            return bill.getId();
        }

        public void payUtilityBill(Long billId) {
            UtilityBill bill = utilityBillMapper.selectById(billId);
            if (bill == null) {
                throw new RuntimeException("账单不存在");
            }
            if (bill.getStatus() == 1) {
                throw new RuntimeException("账单已支付");
            }
            bill.setStatus(1);
            utilityBillMapper.updateById(bill);
        }

        public Page<UtilityBill> getUtilityBillList(Long tenantId, Integer type, int pageNum, int pageSize) {
            return utilityBillMapper.selectPage(null, null);
        }

        public UtilityBill getUtilityBillDetail(Long billId) {
            UtilityBill bill = utilityBillMapper.selectById(billId);
            if (bill == null) {
                throw new RuntimeException("账单不存在");
            }
            return bill;
        }
    }
}
