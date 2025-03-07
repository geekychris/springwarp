
package com.hitorro.service.impl;

import com.hitorro.model.Employee;
import com.hitorro.repository.DepartmentRepository;
import com.hitorro.repository.EmployeeRepository;
import com.hitorro.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) {
        // Validate department exists
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + employee.getDepartment().getId()));
        }
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setFirstName(employeeDetails.getFirstName());
                    existingEmployee.setLastName(employeeDetails.getLastName());
                    existingEmployee.setEmail(employeeDetails.getEmail());
                    existingEmployee.setSalary(employeeDetails.getSalary());
                    existingEmployee.setHireDate(employeeDetails.getHireDate());
                    
                    // Update department if provided
                    if (employeeDetails.getDepartment() != null && employeeDetails.getDepartment().getId() != null) {
                        departmentRepository.findById(employeeDetails.getDepartment().getId())
                                .ifPresent(existingEmployee::setDepartment);
                    }
                    
                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Employee> findEmployeesByName(String name) {
        // Search in both first name and last name
        return employeeRepository.findAll().stream()
                .filter(employee -> 
                    employee.getFirstName().toLowerCase().contains(name.toLowerCase()) || 
                    employee.getLastName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageSalaryByDepartment(Long departmentId) {
        List<Employee> departmentEmployees = getEmployeesByDepartmentId(departmentId);
        if (departmentEmployees.isEmpty()) {
            return 0.0;
        }
        
        return departmentEmployees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }
}

