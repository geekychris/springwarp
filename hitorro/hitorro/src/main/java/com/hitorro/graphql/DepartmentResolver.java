package com.hitorro.graphql;

import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import com.hitorro.service.DepartmentService;
import com.hitorro.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DepartmentResolver {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentResolver(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    // Query resolvers

    @QueryMapping
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @QueryMapping
    public Department department(@Argument Long id) {
        return departmentService.getDepartmentById(id).orElse(null);
    }

    @QueryMapping
    public Department departmentByName(@Argument String name) {
        return departmentService.getDepartmentByName(name).orElse(null);
    }

    @QueryMapping
    public List<Employee> departmentEmployees(@Argument Long departmentId) {
        return employeeService.getEmployeesByDepartmentId(departmentId);
    }

    // Field resolvers

    @SchemaMapping(typeName = "Department", field = "employees")
    public List<Employee> getEmployees(Department department) {
        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            return department.getEmployees();
        }
        return employeeService.getEmployeesByDepartmentId(department.getId());
    }

    // Mutation resolvers

    @MutationMapping
    public Department createDepartment(@Argument Map<String, Object> department) {
        Department newDepartment = new Department();
        newDepartment.setName((String) department.get("name"));
        newDepartment.setLocation((String) department.get("location"));
        newDepartment.setDescription((String) department.get("description"));
        return departmentService.saveDepartment(newDepartment);
    }

    @MutationMapping
    public Department updateDepartment(@Argument Map<String, Object> department) {
        Long id = Long.valueOf(department.get("id").toString());
        Optional<Department> existingDept = departmentService.getDepartmentById(id);
        
        if (existingDept.isPresent()) {
            Department deptToUpdate = existingDept.get();
            
            if (department.containsKey("name")) {
                deptToUpdate.setName((String) department.get("name"));
            }
            
            if (department.containsKey("location")) {
                deptToUpdate.setLocation((String) department.get("location"));
            }
            
            if (department.containsKey("description")) {
                deptToUpdate.setDescription((String) department.get("description"));
            }
            
            return departmentService.updateDepartment(id, deptToUpdate);
        }
        
        return null;
    }

    @MutationMapping
    public Boolean deleteDepartment(@Argument Long id) {
        return departmentService.deleteDepartment(id);
    }
}

