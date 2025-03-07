package com.hitorro.repository;

import com.hitorro.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find an employee by email address.
     *
     * @param email the email address
     * @return the employee if found
     */
    Optional<Employee> findByEmail(String email);
    
    /**
     * Find employees by last name.
     *
     * @param lastName the last name
     * @return list of employees with the given last name
     */
    List<Employee> findByLastName(String lastName);
    
    /**
     * Find employees by first name and last name.
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return list of matching employees
     */
    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);
    
    /**
     * Find employees by department id.
     *
     * @param departmentId the department id
     * @return list of employees in the department
     */
    List<Employee> findByDepartmentId(Long departmentId);
    
    /**
     * Find employees by department name.
     *
     * @param departmentName the department name
     * @return list of employees in the department
     */
    @Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);
    
    /**
     * Count the number of employees in a department.
     *
     * @param departmentId the department id
     * @return the count of employees
     */
    long countByDepartmentId(Long departmentId);
}

