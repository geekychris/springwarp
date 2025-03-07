package com.hitorro.service;

import com.hitorro.model.Department;
import com.hitorro.repository.DepartmentRepository;
import com.hitorro.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        department1 = new Department();
        department1.setId(1L);
        department1.setName("HR");
        department1.setDescription("Human Resources");
        department1.setLocation("Building A");

        department2 = new Department();
        department2.setId(2L);
        department2.setName("IT");
        department2.setDescription("Information Technology");
        department2.setLocation("Building B");
    }

    @Test
    void getAllDepartments_ShouldReturnAllDepartments() {
        // Arrange
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department1, department2));

        // Act
        List<Department> departments = departmentService.getAllDepartments();

        // Assert
        assertEquals(2, departments.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getDepartmentById_WhenDepartmentExists_ShouldReturnDepartment() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));

        // Act
        Optional<Department> result = departmentService.getDepartmentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("HR", result.get().getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void getDepartmentById_WhenDepartmentDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(departmentRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Department> result = departmentService.getDepartmentById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(3L);
    }

    @Test
    void getDepartmentByName_WhenDepartmentExists_ShouldReturnDepartment() {
        // Arrange
        when(departmentRepository.findByName("HR")).thenReturn(Optional.of(department1));

        // Act
        Optional<Department> result = departmentService.getDepartmentByName("HR");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(departmentRepository, times(1)).findByName("HR");
    }

    @Test
    void saveDepartment_ShouldSaveDepartment() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setName("Finance");
        newDepartment.setDescription("Finance Department");
        
        when(departmentRepository.save(any(Department.class))).thenReturn(newDepartment);

        // Act
        Department result = departmentService.saveDepartment(newDepartment);

        // Assert
        assertNotNull(result);
        assertEquals("Finance", result.getName());
        verify(departmentRepository, times(1)).save(newDepartment);
    }

    @Test
    void updateDepartment_WhenDepartmentExists_ShouldUpdateDepartment() {
        // Arrange
        Department updatedDetails = new Department();
        updatedDetails.setName("HR Updated");
        updatedDetails.setDescription("HR Department Updated");
        
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Department result = departmentService.updateDepartment(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("HR Updated", result.getName());
        assertEquals("HR Department Updated", result.getDescription());
        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void updateDepartment_WhenDepartmentDoesNotExist_ShouldThrowException() {
        // Arrange
        Department updatedDetails = new Department();
        updatedDetails.setName("Non-existent");
        
        when(departmentRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departmentService.updateDepartment(3L, updatedDetails));
        verify(departmentRepository, times(1)).findById(3L);
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    void deleteDepartment_WhenDepartmentExists_ShouldReturnTrue() {
        // Arrange
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        // Act
        boolean result = departmentService.deleteDepartment(1L);

        // Assert
        assertTrue(result);
        verify(departmentRepository, times(1)).existsById(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDepartment_WhenDepartmentDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(departmentRepository.existsById(3L)).thenReturn(false);

        // Act
        boolean result = departmentService.deleteDepartment(3L);

        // Assert
        assertFalse(result);
        verify(departmentRepository, times(1)).existsById(3L);
        verify(departmentRepository, never()).deleteById(3L);
    }
}

