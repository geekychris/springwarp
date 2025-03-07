package com.hitorro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import com.hitorro.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Department department1;
    private Department department2;
    private List<Department> departments;

    @BeforeEach
    public void setup() {
        department1 = new Department();
        department1.setId(1L);
        department1.setName("Engineering");
        department1.setLocation("Building A");

        department2 = new Department();
        department2.setId(2L);
        department2.setName("Marketing");
        department2.setLocation("Building B");

        departments = Arrays.asList(department1, department2);
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Engineering")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Marketing")));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department1));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Engineering")))
                .andExpect(jsonPath("$.location", is("Building A")));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    public void testGetDepartmentByIdNotFound() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/999"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(999L);
    }

    @Test
    public void testCreateDepartment() throws Exception {
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department1);

        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(department1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Engineering")));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setName("Engineering Updated");
        updatedDepartment.setLocation("Building C");

        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department1));
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(updatedDepartment);

        mockMvc.perform(put("/api/departments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Engineering Updated")))
                .andExpect(jsonPath("$.location", is("Building C")));

        verify(departmentService, times(1)).getDepartmentById(1L);
        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    public void testUpdateDepartmentNotFound() throws Exception {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(999L);
        updatedDepartment.setName("Non-existent Department");

        when(departmentService.getDepartmentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/departments/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(999L);
        verify(departmentService, never()).saveDepartment(any(Department.class));
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department1));
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).getDepartmentById(1L);
        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    public void testDeleteDepartmentNotFound() throws Exception {
        when(departmentService.getDepartmentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/departments/999"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(999L);
        verify(departmentService, never()).deleteDepartment(anyLong());
    }

    @Test
    public void testGetEmployeesByDepartmentId() throws Exception {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department1));
       // when(departmentService.getEmployeesByDepartmentId(1L)).thenReturn(employees);

        mockMvc.perform(get("/api/departments/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(departmentService, times(1)).getDepartmentById(1L);
        //verify(departmentService, times(1)).getEmployeesByDepartmentId(1L);
    }

    @Test
    public void testGetEmployeesByDepartmentIdNotFound() throws Exception {
        when(departmentService.getDepartmentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/999/employees"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(999L);
        //verify(departmentService, never()).getEmployeesByDepartmentId(anyLong());
    }
}

