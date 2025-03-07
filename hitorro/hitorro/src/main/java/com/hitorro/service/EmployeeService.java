
package com.hitorro.service;

import com.hitorro.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    
    /**
     * Get all employees
     * @return list of all employees
     */
    List<Employee> getAllEmployees();
    
    /**
     * Get employee by ID
     * @param id the employee ID
     * @return the employee if found
     */
    Optional<Employee> getEmployeeById(Long id);
    
    /**
     * Get employees by department ID
     * @param departmentId the department ID
     * @return list of employees in the specified department
     */
    List<Employee> getEmployeesByDepartmentId(Long departmentId);
    
    /**
     * Save an employee
     * @param employee the employee to save
     * @return the saved employee
     */
    Employee saveEmployee(Employee employee);
    
    /**
     * Update an employee
     * @param id the employee ID
     * @param employeeDetails the updated employee data
     * @return the updated employee
     */
    Employee updateEmployee(Long id, Employee employeeDetails);
    
    /**
     * Delete an employee
     * @param id the employee ID
     * @return true if deleted successfully
     */
    boolean deleteEmployee(Long id);
    
    /**
     * Find employees by name
     * @param name the employee name to search for
     * @return list of employees matching the name
     */
    List<Employee> findEmployeesByName(String name);
    
    /**
     * Get average salary by department
     * @param departmentId the department ID
     * @return the average salary
     */
    Double getAverageSalaryByDepartment(Long departmentId);
}

