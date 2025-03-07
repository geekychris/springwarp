package com.hitorro.repository;

import com.hitorro.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void shouldSaveDepartment() {
        // Create a department
        Department department = new Department();
        department.setName("Engineering");
        department.setLocation("Building A");

        // Save the department
        Department savedDepartment = departmentRepository.save(department);

        // Verify the department was saved
        assertThat(savedDepartment).isNotNull();
        assertThat(savedDepartment.getId()).isNotNull();
        assertThat(savedDepartment.getName()).isEqualTo("Engineering");
        assertThat(savedDepartment.getLocation()).isEqualTo("Building A");
    }

    @Test
    public void shouldFindDepartmentById() {
        // Create and persist a department
        Department department = new Department();
        department.setName("Marketing");
        department.setLocation("Building B");
        
        Department persistedDepartment = entityManager.persistAndFlush(department);

        // Find the department by ID
        Optional<Department> foundDepartment = departmentRepository.findById(persistedDepartment.getId());

        // Verify the department was found
        assertThat(foundDepartment).isPresent();
        assertThat(foundDepartment.get().getName()).isEqualTo("Marketing");
        assertThat(foundDepartment.get().getLocation()).isEqualTo("Building B");
    }

    @Test
    public void shouldFindAllDepartments() {
        // Create and persist multiple departments
        Department dept1 = new Department();
        dept1.setName("HR");
        dept1.setLocation("Building C");
        
        Department dept2 = new Department();
        dept2.setName("Finance");
        dept2.setLocation("Building D");
        
        entityManager.persist(dept1);
        entityManager.persist(dept2);
        entityManager.flush();

        // Find all departments
        List<Department> departments = departmentRepository.findAll();

        // Verify all departments were found
        assertThat(departments).isNotNull();
        assertThat(departments.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindDepartmentByName() {
        // Create and persist a department
        Department department = new Department();
        department.setName("Research");
        department.setLocation("Building E");
        
        entityManager.persistAndFlush(department);

        // Find the department by name
        Optional<Department> foundDepartment = departmentRepository.findByName("Research");

        // Verify the department was found
        assertThat(foundDepartment).isPresent();
        assertThat(foundDepartment.get().getName()).isEqualTo("Research");
        assertThat(foundDepartment.get().getLocation()).isEqualTo("Building E");
    }

    @Test
    public void shouldUpdateDepartment() {
        // Create and persist a department
        Department department = new Department();
        department.setName("Sales");
        department.setLocation("Building F");
        
        Department persistedDepartment = entityManager.persistAndFlush(department);

        // Update the department
        persistedDepartment.setName("Global Sales");
        persistedDepartment.setLocation("Building G");
        departmentRepository.save(persistedDepartment);

        // Find the department to verify the update
        Department updatedDepartment = entityManager.find(Department.class, persistedDepartment.getId());

        // Verify the department was updated
        assertThat(updatedDepartment.getName()).isEqualTo("Global Sales");
        assertThat(updatedDepartment.getLocation()).isEqualTo("Building G");
    }

    @Test
    public void shouldDeleteDepartment() {
        // Create and persist a department
        Department department = new Department();
        department.setName("IT Support");
        department.setLocation("Building H");
        
        Department persistedDepartment = entityManager.persistAndFlush(department);

        // Delete the department
        departmentRepository.deleteById(persistedDepartment.getId());

        // Try to find the department
        Department deletedDepartment = entityManager.find(Department.class, persistedDepartment.getId());

        // Verify the department was deleted
        assertThat(deletedDepartment).isNull();
    }
}

