package com.rental.property.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.property.dto.PropertyDTO;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.property.service.impl.PropertyServiceImpl;
import com.rental.property.vo.PropertyVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 房源服务单元测试（补充）
 *
 * @author rental-team
 * @date 2026-04-03
 */
@ExtendWith(MockitoExtension.class)
class PropertyServiceExtendedTest {

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property testProperty;
    private PropertyDTO testDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testProperty = new Property();
        testProperty.setId(1L);
        testProperty.setLandlordId(100L);
        testProperty.setTitle("测试房源");
        testProperty.setAddress("测试地址 123 号");
        testProperty.setRent(new BigDecimal("3000.00"));
        testProperty.setArea(80.0);
        testProperty.setRooms(2);
        testProperty.setHalls(1);
        testProperty.setBathrooms(1);
        testProperty.setStatus(1); // 可租
        testProperty.setDeleted(0);

        testDTO = new PropertyDTO();
        testDTO.setPropertyId(1L);
        testDTO.setTitle("测试房源");
        testDTO.setAddress("测试地址 123 号");
        testDTO.setRent(new BigDecimal("3000.00"));
        testDTO.setArea(80.0);
        testDTO.setRooms(2);
        testDTO.setHalls(1);
        testDTO.setBathrooms(1);
    }

    @Test
    void testAddProperty_Success() {
        // Arrange
        when(propertyMapper.insert(any(Property.class))).thenReturn(1);
        when(propertyMapper.selectById(anyLong())).thenReturn(testProperty);

        // Act
        Long propertyId = propertyService.addProperty(testDTO, 100L);

        // Assert
        assertNotNull(propertyId);
        assertEquals(1L, propertyId);
        verify(propertyMapper, times(1)).insert(any(Property.class));
    }

    @Test
    void testAddProperty_NullDTO() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.addProperty(null, 100L);
        });
    }

    @Test
    void testUpdateProperty_Success() {
        // Arrange
        when(propertyMapper.selectById(1L)).thenReturn(testProperty);
        when(propertyMapper.updateById(any(Property.class))).thenReturn(1);

        // Act
        propertyService.updateProperty(testDTO);

        // Assert
        verify(propertyMapper, times(1)).selectById(1L);
        verify(propertyMapper, times(1)).updateById(any(Property.class));
    }

    @Test
    void testUpdateProperty_NotFound() {
        // Arrange
        when(propertyMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.updateProperty(testDTO);
        });
    }

    @Test
    void testDeleteProperty_Success() {
        // Arrange
        when(propertyMapper.selectById(1L)).thenReturn(testProperty);
        when(propertyMapper.deleteById(1L)).thenReturn(1);

        // Act
        propertyService.deleteProperty(1L);

        // Assert
        verify(propertyMapper, times(1)).selectById(1L);
        verify(propertyMapper, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProperty_NotFound() {
        // Arrange
        when(propertyMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.deleteProperty(999L);
        });
    }

    @Test
    void testDeleteProperty_AlreadyDeleted() {
        // Arrange
        Property deletedProperty = new Property();
        deletedProperty.setId(1L);
        deletedProperty.setDeleted(1);

        when(propertyMapper.selectById(1L)).thenReturn(deletedProperty);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.deleteProperty(1L);
        });
    }

    @Test
    void testGetPropertyList_Success() {
        // Arrange
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(testProperty);

        Page<Property> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(propertyList);

        when(propertyMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<PropertyVO> result = propertyService.getPropertyList(100L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetPropertyList_ByStatus() {
        // Arrange
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(testProperty);

        Page<Property> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(propertyList);

        when(propertyMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<PropertyVO> result = propertyService.getPropertyList(100L, 1, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testGetPropertyList_EmptyResult() {
        // Arrange
        Page<Property> mockPage = new Page<>(1, 10, 0);
        mockPage.setRecords(new ArrayList<>());

        when(propertyMapper.selectPage(any(), any())).thenReturn(mockPage);

        // Act
        Page<PropertyVO> result = propertyService.getPropertyList(100L, null, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getRecords().isEmpty());
    }

    @Test
    void testGetPropertyDetail_Success() {
        // Arrange
        when(propertyMapper.selectById(1L)).thenReturn(testProperty);

        // Act
        PropertyVO result = propertyService.getPropertyDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试房源", result.getTitle());
        assertEquals(new BigDecimal("3000.00"), result.getRent());
    }

    @Test
    void testGetPropertyDetail_NotFound() {
        // Arrange
        when(propertyMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.getPropertyDetail(999L);
        });
    }

    @Test
    void testGetPropertyDetail_Deleted() {
        // Arrange
        Property deletedProperty = new Property();
        deletedProperty.setId(1L);
        deletedProperty.setDeleted(1);

        when(propertyMapper.selectById(1L)).thenReturn(deletedProperty);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            propertyService.getPropertyDetail(1L);
        });
    }
}
