package com.hitorro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import com.hitorro.service.EmployeeService;
import com.hitorro.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    
    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employees;
    private Department department;

    @BeforeEach
    public void setup() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");
        department.setLocation("Building A");

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john.doe@hitorro.com");
        employee1.setSalary(75000.0);
        employee1.setHireDate(LocalDate.of(2020, 1, 15));
        employee1.setPhoneNumber("555-1234");
        employee1.setDepartment(department);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane.smith@hitorro.com");
        employee2.setSalary(82000.0);
        employee2.setHireDate(LocalDate.of(2019, 5, 10));
        employee2.setPhoneNumber("555-5678");
        employee2.setDepartment(department);

        employees = Arrays.asList(employee1, employee2);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")))
                .andExpect(jsonPath("$[1].lastName", is("Smith")));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@hitorro.com")))
                .andExpect(jsonPath("$.salary", is(75000.0)))
                .andExpect(jsonPath("$.department.id", is(1)))
                .andExpect(jsonPath("$.department.name", is("Engineering")));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    public void testGetEmployeeByIdNotFound() throws Exception {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department));
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee1);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidDepartment() {
    }
}

