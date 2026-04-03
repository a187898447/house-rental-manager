package com.rental.bill.service;

import com.rental.bill.entity.DepositRecord;
import com.rental.bill.mapper.DepositRecordMapper;
import com.rental.bill.service.impl.DepositRecordServiceImpl;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.user.entity.Tenant;
import com.rental.user.mapper.TenantMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 押金记录服务单元测试
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class DepositRecordServiceTest {

    @Mock
    private DepositRecordMapper depositRecordMapper;

    @Mock
    private TenantMapper tenantMapper;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private DepositRecordServiceImpl depositRecordService;

    private DepositRecord testDepositRecord;
    private Tenant testTenant;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testTenant = new Tenant();
        testTenant.setId(1L);
        testTenant.setTenantName("张三");
        testTenant.setTenantPhone("13800138000");

        testProperty = new Property();
        testProperty.setId(100L);
        testProperty.setTitle("测试房源");
        testProperty.setCreateBy(50L); // landlordId
        testProperty.setDeleted(0);

        testDepositRecord = new DepositRecord();
        testDepositRecord.setId(1L);
        testDepositRecord.setTenantId(1L);
        testDepositRecord.setPropertyId(100L);
        testDepositRecord.setLandlordId(50L);
        testDepositRecord.setAmount(new BigDecimal("6000.00"));
        testDepositRecord.setPaidDate(LocalDate.now());
        testDepositRecord.setStatus(1); // 已支付
        testDepositRecord.setRefundDate(null);
    }

    @Test
    void testCollectDeposit_Success() {
        // Arrange
        when(tenantMapper.selectById(1L)).thenReturn(testTenant);
        when(propertyMapper.selectById(100L)).thenReturn(testProperty);
        when(depositRecordMapper.selectCount(any())).thenReturn(0L);
        when(depositRecordMapper.insert(any(DepositRecord.class))).thenReturn(1);
        when(depositRecordMapper.selectById(anyLong())).thenReturn(testDepositRecord);

        // Act
        Long recordId = depositRecordService.collectDeposit(1L, 100L, new BigDecimal("6000.00"));

        // Assert
        assertNotNull(recordId);
        assertEquals(1L, recordId);
        verify(depositRecordMapper, times(1)).insert(any(DepositRecord.class));
    }

    @Test
    void testCollectDeposit_TenantNotFound() {
        // Arrange
        when(tenantMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.collectDeposit(999L, 100L, new BigDecimal("6000.00"));
        });
    }

    @Test
    void testCollectDeposit_PropertyNotFound() {
        // Arrange
        when(tenantMapper.selectById(1L)).thenReturn(testTenant);
        when(propertyMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.collectDeposit(1L, 999L, new BigDecimal("6000.00"));
        });
    }

    @Test
    void testCollectDeposit_PropertyDeleted() {
        // Arrange
        Property deletedProperty = new Property();
        deletedProperty.setId(100L);
        deletedProperty.setDeleted(1);

        when(tenantMapper.selectById(1L)).thenReturn(testTenant);
        when(propertyMapper.selectById(100L)).thenReturn(deletedProperty);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.collectDeposit(1L, 100L, new BigDecimal("6000.00"));
        });
    }

    @Test
    void testCollectDeposit_AlreadyExists() {
        // Arrange
        when(tenantMapper.selectById(1L)).thenReturn(testTenant);
        when(propertyMapper.selectById(100L)).thenReturn(testProperty);
        when(depositRecordMapper.selectCount(any())).thenReturn(1L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.collectDeposit(1L, 100L, new BigDecimal("6000.00"));
        });
    }

    @Test
    void testRefundDeposit_Success() {
        // Arrange
        when(depositRecordMapper.selectById(1L)).thenReturn(testDepositRecord);
        when(depositRecordMapper.updateById(any(DepositRecord.class))).thenReturn(1);

        // Act
        depositRecordService.refundDeposit(1L, LocalDate.of(2026, 6, 30));

        // Assert
        verify(depositRecordMapper, times(1)).selectById(1L);
        verify(depositRecordMapper, times(1)).updateById(any(DepositRecord.class));
    }

    @Test
    void testRefundDeposit_NotFound() {
        // Arrange
        when(depositRecordMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.refundDeposit(999L, LocalDate.of(2026, 6, 30));
        });
    }

    @Test
    void testRefundDeposit_AlreadyRefunded() {
        // Arrange
        DepositRecord refundedRecord = new DepositRecord();
        refundedRecord.setId(1L);
        refundedRecord.setStatus(2); // 已退还

        when(depositRecordMapper.selectById(1L)).thenReturn(refundedRecord);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            depositRecordService.refundDeposit(1L, LocalDate.of(2026, 6, 30));
        });
    }

    @Test
    void testGetByTenantId_Found() {
        // Arrange
        when(depositRecordMapper.selectOne(any())).thenReturn(testDepositRecord);

        // Act
        DepositRecord result = depositRecordService.getByTenantId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getTenantId());
    }

    @Test
    void testGetByTenantId_NotFound() {
        // Arrange
        when(depositRecordMapper.selectOne(any())).thenReturn(null);

        // Act
        DepositRecord result = depositRecordService.getByTenantId(999L);

        // Assert
        assertNull(result);
    }
}
