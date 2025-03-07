package com.hitorro.service;

import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import com.hitorro.repository.DepartmentRepository;
import com.hitorro.repository.EmployeeRepository;
import com.hitorro.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("HR");
        department.setDescription("Human Resources");

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john.doe@hitorro.com");
        employee1.setSalary(50000.0);
        employee1.setHireDate(LocalDate.of(2020, 1, 15));
        employee1.setDepartment(department);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane.smith@hitorro.com");
        employee2.setSalary(60000.0);
        employee2.setHireDate(LocalDate.of(2019, 5, 10));
        employee2.setDepartment(department);

        employee3 = new Employee();
        employee3.setId(3L);
        employee3.setFirstName("Mike");
        employee3.setLastName("Johnson");
        employee3.setEmail("mike.johnson@hitorro.com");
        employee3.setSalary(55000.0);
        employee3.setHireDate(LocalDate.of(2021, 3, 20));
        employee3.setDepartment(department);
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2, employee3));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertEquals(3, employees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(employeeRepository.findById(4L)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(4L);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(4L);
    }

    @Test
    void getEmployeesByDepartmentId_ShouldReturnEmployeesInDepartment() {
        // Arrange
        when(employeeRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(employee1, employee2, employee3));

        // Act
        List<Employee> results = employeeService.getEmployeesByDepartmentId(1L);

        // Assert
        assertEquals(3, results.size());
        verify(employeeRepository, times(1)).findByDepartmentId(1L);
    }

    @Test
    void saveEmployee_WithValidDepartment_ShouldSaveEmployee() {
        // Arrange
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Alice");
        newEmployee.setLastName("Brown");
        newEmployee.setEmail("alice.brown@hitorro.com");
        newEmployee.setSalary(52000.0);
        newEmployee.setHireDate(LocalDate.now());
        newEmployee.setDepartment(department);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        // Act
        Employee result = employeeService.saveEmployee(newEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        verify(departmentRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(newEmployee);
    }

    @Test
    void saveEmployee_WithInvalidDepartment_ShouldThrowException() {
        // Arrange
        Department invalidDepartment = new Department();
        invalidDepartment.setId(99L);

        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Bob");
        newEmployee.setLastName("Wilson");
        newEmployee.setDepartment(invalidDepartment);

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> employeeService.saveEmployee(newEmployee));
        verify(departmentRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WhenEmployeeExists_ShouldUpdateEmployee() {
        // Arrange
        Employee updatedDetails = new Employee();
        updatedDetails.setFirstName("John Updated");
        updatedDetails.setLastName("Doe Updated");
        updatedDetails.setEmail("john.updated@hitorro.com");
        updatedDetails.setSalary(55000.0);
        updatedDetails.setHireDate(LocalDate.now());
        updatedDetails.setDepartment(department);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Employee result = employeeService.updateEmployee(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe Updated", result.getLastName());
        assertEquals("john.updated@hitorro.com", result.getEmail());
        assertEquals(55000.0, result.getSalary());
        verify(employeeRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WhenEmployeeDoesNotExist_ShouldThrowException() {
        // Arrange
        Employee updatedDetails = new Employee();

    }
}