package com.hitorro.controller;

import com.hitorro.model.Employee;
import com.hitorro.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Get all employees
     * @return list of all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Get employee by ID
     * @param id the employee ID
     * @return the employee if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get employees by department ID
     * @param departmentId the department ID
     * @return list of employees in the specified department
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(departmentId));
    }

    /**
     * Create a new employee
     * @param employee the employee to create
     * @return the created employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    /**
     * Update an employee
     * @param id the employee ID
     * @param employeeDetails the updated employee data
     * @return the updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDetails));
    }

    /**
     * Delete an employee
     * @param id the employee ID
     * @return success message or not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Long id) {
        boolean isDeleted = employeeService.deleteEmployee(id);
        if (isDeleted) {
            return ResponseEntity.ok().body("Employee deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find employees by name
     * @param name the employee name to search for
     * @return list of employees matching the name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> findEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.findEmployeesByName(name));
    }

    /**
     * Get average salary by department
     * @param departmentId the department ID
     * @return the average salary
     */
    @GetMapping("/department/{departmentId}/average-salary")
    public ResponseEntity<Map<String, Double>> getAverageSalaryByDepartment(@PathVariable Long departmentId) {
        Double averageSalary = employeeService.getAverageSalaryByDepartment(departmentId);
        return ResponseEntity.ok(Map.of("averageSalary", averageSalary));
    }
}

