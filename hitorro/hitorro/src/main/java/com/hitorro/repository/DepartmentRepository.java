package com.hitorro.repository;

import com.hitorro.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Find a department by its name.
     *
     * @param name the department name
     * @return the department if found
     */
    Optional<Department> findByName(String name);
    
    /**
     * Find departments by name containing the given substring.
     *
     * @param namePart the name substring to search for
     * @return list of matching departments
     */
    List<Department> findByNameContainingIgnoreCase(String namePart);
    
    /**
     * Check if a department with the given name exists.
     *
     * @param name the department name
     * @return true if the department exists, false otherwise
     */
    boolean existsByName(String name);
}

