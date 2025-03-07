
package com.hitorro.service.impl;

import com.hitorro.model.Department;
import com.hitorro.repository.DepartmentRepository;
import com.hitorro.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    @Transactional
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    existingDepartment.setName(departmentDetails.getName());
                    existingDepartment.setDescription(departmentDetails.getDescription());
                    return departmentRepository.save(existingDepartment);
                })
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    @Transactional
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

