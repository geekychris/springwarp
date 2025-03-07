package com.hitorro.service;

import com.hitorro.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    
    /**
     * Get all departments
     * @return list of all departments
     */
    List<Department> getAllDepartments();
    
    /**
     * Get department by ID
     * @param id the department ID
     * @return the department if found
     */
    Optional<Department> getDepartmentById(Long id);
    
    /**
     * Get department by name
     * @param name the department name
     * @return the department if found
     */
    Optional<Department> getDepartmentByName(String name);
    
    /**
     * Save a department
     * @param department the department to save
     * @return the saved department
     */
    Department saveDepartment(Department department);
    
    /**
     * Update a department
     * @param id the department ID
     * @param departmentDetails the updated department data
     * @return the updated department
     */
    Department updateDepartment(Long id, Department departmentDetails);
    
    /**
     * Delete a department
     * @param id the department ID
     * @return true if deleted successfully
     */
    boolean deleteDepartment(Long id);
}

