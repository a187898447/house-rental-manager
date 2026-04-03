package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.service.impl.RentRecordServiceImpl;
import com.rental.bill.vo.RentRecordVO;
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
 * 租金记录服务单元测试（补充）
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class RentRecordServiceExtendedTest {

    @Mock
    private RentRecordMapper rentRecordMapper;

    @InjectMocks
    private RentRecordServiceImpl rentRecordService;

    private RentRecord testRentRecord;
    private RentRecordDTO testDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testRentRecord = new RentRecord();
        testRentRecord.setId(1L);
        testRentRecord.setTenantId(1L);
        testRentRecord.setPropertyId(100L);
        testRentRecord.setLandlordId(50L);
        testRentRecord.setAmount(new BigDecimal("3000.00"));
        testRentRecord.setDueDate(LocalDate.of(2026, 5, 1));
        testRentRecord.setPaidDate(null);
        testRentRecord.setStatus(0); // 待支付
        testRentRecord.setType("monthly");

        testDTO = new RentRecordDTO();
        testDTO.setTenantId(1L);
        testDTO.setPropertyId(100L);
        testDTO.setAmount(new BigDecimal("3000.00"));
        testDTO.setDueDate(LocalDate.of(2026, 5, 1));
        testDTO.setType("monthly");
    }

    @Test
    void testConfirmPayment_Success() {
        // Arrange
        when(rentRecordMapper.selectById(1L)).thenReturn(testRentRecord);
        when(rentRecordMapper.updateById(any(RentRecord.class))).thenReturn(1);

        // Act
        rentRecordService.confirmPayment(1L, LocalDate.of(2026, 4, 28));

        // Assert
        verify(rentRecordMapper, times(1)).selectById(1L);
        verify(rentRecordMapper, times(1)).updateById(any(RentRecord.class));
    }

    @Test
    void testConfirmPayment_NotFound() {
        // Arrange
        when(rentRecordMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            rentRecordService.confirmPayment(999L, LocalDate.of(2026, 4, 28));
        });
    }

    @Test
    void testConfirmPayment_AlreadyPaid() {
        // Arrange
        RentRecord paidRecord = new RentRecord();
        paidRecord.setId(1L);
        paidRecord.setStatus(1); // 已支付

        when(rentRecordMapper.selectById(1L)).thenReturn(paidRecord);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            rentRecordService.confirmPayment(1L, LocalDate.of(2026, 4, 28));
        });
    }

    @Test
    void testGetRentRecordList_Success() {
        // Arrange
        List<RentRecord> recordList = new ArrayList<>();
        recordList.add(testRentRecord);

        Page<RentRecord> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(recordList);

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getRentRecordList(50L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetRentRecordList_ByStatus() {
        // Arrange
        List<RentRecord> recordList = new ArrayList<>();
        recordList.add(testRentRecord);

        Page<RentRecord> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(recordList);

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getRentRecordList(50L, 0, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetDueTodayList_Success() {
        // Arrange
        List<RentRecord> dueList = new ArrayList<>();
        dueList.add(testRentRecord);

        Page<RentRecord> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(dueList);

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getDueTodayList(50L, LocalDate.of(2026, 5, 1), 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetDueTodayList_Empty() {
        // Arrange
        Page<RentRecord> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getDueTodayList(50L, LocalDate.of(2026, 5, 1), 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetOverdueList_Success() {
        // Arrange
        List<RentRecord> overdueList = new ArrayList<>();
        overdueList.add(testRentRecord);

        Page<RentRecord> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(overdueList);

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getOverdueList(50L, 2, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetOverdueList_Empty() {
        // Arrange
        Page<RentRecord> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecordVO> result = rentRecordService.getOverdueList(50L, 3, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }
}
