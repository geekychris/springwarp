package com.hitorro.controller;

import com.hitorro.model.Department;
import com.hitorro.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Get all departments
     * @return list of all departments
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    /**
     * Get department by ID
     * @param id the department ID
     * @return the department if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get department by name
     * @param name the department name
     * @return the department if found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        return departmentService.getDepartmentByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new department
     * @param department the department to create
     * @return the created department
     */
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.saveDepartment(department), HttpStatus.CREATED);
    }

    /**
     * Update a department
     * @param id the department ID
     * @param departmentDetails the updated department data
     * @return the updated department
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDetails));
    }

    /**
     * Delete a department
     * @param id the department ID
     * @return success message or not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable Long id) {
        boolean isDeleted = departmentService.deleteDepartment(id);
        if (isDeleted) {
            return ResponseEntity.ok().body("Department deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

