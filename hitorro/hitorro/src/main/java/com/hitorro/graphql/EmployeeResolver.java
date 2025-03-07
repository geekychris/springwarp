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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class EmployeeResolver {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeResolver(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    // Query resolvers

    @QueryMapping
    public List<Employee> employees() {
        return employeeService.getAllEmployees();
    }

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return employeeService.getEmployeeById(id).orElse(null);
    }

    @QueryMapping
    public List<Employee> employeesByDepartment(@Argument Long departmentId) {
        return employeeService.getEmployeesByDepartmentId(departmentId);
    }

    @QueryMapping
    public Employee employeeByEmail(@Argument String email) {
        return employeeService.getAllEmployees().stream()
                .filter(e -> email.equals(e.getEmail()))
                .findFirst()
                .orElse(null);
    }

    // Field resolvers

    @SchemaMapping(typeName = "Employee", field = "department")
    public Department getDepartment(Employee employee) {
        // If the department is already loaded, return it
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            // Check if we need to fully load the department
            if (employee.getDepartment().getName() == null) {
                return departmentService.getDepartmentById(employee.getDepartment().getId()).orElse(null);
            }
            return employee.getDepartment();
        }
        return null;
    }

    @SchemaMapping(typeName = "Employee", field = "hireDate")
    public String getHireDate(Employee employee) {
        if (employee.getHireDate() != null) {
            return employee.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return null;
    }

    // Mutation resolvers

    @MutationMapping
    public Employee createEmployee(@Argument Map<String, Object> employee) {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName((String) employee.get("firstName"));
        newEmployee.setLastName((String) employee.get("lastName"));
        newEmployee.setEmail((String) employee.get("email"));
        newEmployee.setPhoneNumber((String) employee.get("phoneNumber"));
        
        if (employee.get("salary") != null) {
            newEmployee.setSalary(Double.valueOf(employee.get("salary").toString()));
        }
        
        if (employee.get("hireDate") != null) {
            newEmployee.setHireDate(LocalDate.parse(
                (String) employee.get("hireDate"),
                DateTimeFormatter.ISO_LOCAL_DATE
            ));
        }
        
        newEmployee.setPosition((String) employee.get("position"));
        
        // Set department
        if (employee.get("departmentId") != null) {
            Long deptId = Long.valueOf(employee.get("departmentId").toString());
            departmentService.getDepartmentById(deptId).ifPresent(newEmployee::setDepartment);
        }
        
        return employeeService.saveEmployee(newEmployee);
    }

    @MutationMapping
    public Employee updateEmployee(@Argument Map<String, Object> employee) {
        Long id = Long.valueOf(employee.get("id").toString());
        Optional<Employee> existingEmp = employeeService.getEmployeeById(id);
        
        if (existingEmp.isPresent()) {
            Employee empToUpdate = existingEmp.get();
            
            if (employee.containsKey("firstName")) {
                empToUpdate.setFirstName((String) employee.get("firstName"));
            }
            
            if (employee.containsKey("lastName")) {
                empToUpdate.setLastName((String) employee.get("lastName"));
            }
            
            if (employee.containsKey("email")) {
                empToUpdate.setEmail((String) employee.get("email"));
            }
            
            if (employee.containsKey("phoneNumber")) {
                empToUpdate.setPhoneNumber((String) employee.get("phoneNumber"));
            }
            
            if (employee.containsKey("salary")) {
                empToUpdate.setSalary(Double.valueOf(employee.get("salary").toString()));
            }
            
            if (employee.containsKey("hireDate")) {
                empToUpdate.setHireDate(LocalDate.parse(
                    (String) employee.get("hireDate"),
                    DateTimeFormatter.ISO_LOCAL_DATE
                ));
            }
            
            if (employee.containsKey("position")) {
                empToUpdate.setPosition((String) employee.get("position"));
            }
            
            if (employee.containsKey("departmentId")) {
                Long deptId = Long.valueOf(employee.get("departmentId").toString());
                departmentService.getDepartmentById(deptId).ifPresent(empToUpdate::setDepartment);
            }
            
            return employeeService.updateEmployee(id, empToUpdate);
        }
        
        return null;
    }

    @MutationMapping
    public Boolean deleteEmployee(@Argument Long id) {
        return employeeService.deleteEmployee(id);
    }

    @MutationMapping
    public Employee assignEmployeeToDepartment(@Argument Long employeeId, @Argument Long departmentId) {
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(employeeId);
        Optional<Department> departmentOpt = departmentService.getDepartmentById(departmentId);
        
        if (employeeOpt.isPresent() && departmentOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            Department department = departmentOpt.get();
            
            employee.setDepartment(department);
            return employeeService.updateEmployee(employeeId, employee);
        }
        
        return null;
    }
}

