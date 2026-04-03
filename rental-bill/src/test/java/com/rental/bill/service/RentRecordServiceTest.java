package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.service.impl.RentRecordServiceImpl;
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
 * 租金记录服务单元测试
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class RentRecordServiceTest {

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
        testRentRecord.setAmount(new BigDecimal("3000.00"));
        testRentRecord.setDueDate(LocalDate.of(2026, 5, 1));
        testRentRecord.setPayDate(null);
        testRentRecord.setStatus(0); // 待支付
        testRentRecord.setType("monthly");

        testDTO = new RentRecordDTO();
        testDTO.setTenantId(1L);
        testDTO.setAmount(new BigDecimal("3000.00"));
        testDTO.setDueDate(LocalDate.of(2026, 5, 1));
        testDTO.setType("monthly");
    }

    @Test
    void testCreateRentRecord_Success() {
        // Arrange
        when(rentRecordMapper.insert(any(RentRecord.class))).thenReturn(1);
        when(rentRecordMapper.selectById(anyLong())).thenReturn(testRentRecord);

        // Act
        Long recordId = rentRecordService.createRentRecord(testDTO);

        // Assert
        assertNotNull(recordId);
        assertEquals(1L, recordId);
        verify(rentRecordMapper, times(1)).insert(any(RentRecord.class));
    }

    @Test
    void testCreateRentRecord_NullDTO() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            rentRecordService.createRentRecord(null);
        });
    }

    @Test
    void testPayRent_Success() {
        // Arrange
        when(rentRecordMapper.selectById(1L)).thenReturn(testRentRecord);
        when(rentRecordMapper.updateById(any(RentRecord.class))).thenReturn(1);

        // Act
        rentRecordService.payRent(1L);

        // Assert
        verify(rentRecordMapper, times(1)).selectById(1L);
        verify(rentRecordMapper, times(1)).updateById(any(RentRecord.class));
    }

    @Test
    void testPayRent_NotFound() {
        // Arrange
        when(rentRecordMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            rentRecordService.payRent(999L);
        });
    }

    @Test
    void testPayRent_AlreadyPaid() {
        // Arrange
        RentRecord paidRecord = new RentRecord();
        paidRecord.setId(1L);
        paidRecord.setStatus(1); // 已支付

        when(rentRecordMapper.selectById(1L)).thenReturn(paidRecord);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            rentRecordService.payRent(1L);
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
        Page<RentRecord> result = rentRecordService.getRentRecordList(1L, null, 1, 10);

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
        Page<RentRecord> result = rentRecordService.getRentRecordList(1L, 0, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetRentRecordList_EmptyResult() {
        // Arrange
        Page<RentRecord> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(rentRecordMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<RentRecord> result = rentRecordService.getRentRecordList(1L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetOverdueRecords_Success() {
        // Arrange
        List<RentRecord> overdueList = new ArrayList<>();
        overdueList.add(testRentRecord);

        when(rentRecordMapper.selectOverdueRecords(any())).thenReturn(overdueList);

        // Act
        List<RentRecord> result = rentRecordService.getOverdueRecords(LocalDate.of(2026, 5, 2));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetOverdueRecords_Empty() {
        // Arrange
        when(rentRecordMapper.selectOverdueRecords(any())).thenReturn(new ArrayList<>());

        // Act
        List<RentRecord> result = rentRecordService.getOverdueRecords(LocalDate.of(2026, 5, 2));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
