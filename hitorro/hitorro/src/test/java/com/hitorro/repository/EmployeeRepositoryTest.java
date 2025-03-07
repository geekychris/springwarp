package com.hitorro.repository;

import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void shouldSaveEmployee() {
        // Create a department
        Department department = new Department();
        department.setName("Engineering");
        department.setLocation("Building A");
        entityManager.persist(department);

        // Create an employee
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@hitorro.com");
        employee.setSalary(75000.0);
        employee.setHireDate(LocalDate.of(2020, 1, 15));
        employee.setPhoneNumber("555-1234");
        employee.setDepartment(department);

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Verify the employee was saved
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        assertThat(savedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(savedEmployee.getDepartment().getName()).isEqualTo("Engineering");
    }

    @Test
    public void shouldFindEmployeeById() {
        // Create a department
        Department department = new Department();
        department.setName("Marketing");
        department.setLocation("Building B");
        entityManager.persist(department);

        // Create and persist an employee
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setEmail("jane.smith@hitorro.com");
        employee.setSalary(65000.0);
        employee.setHireDate(LocalDate.of(2021, 3, 10));
        employee.setPhoneNumber("555-5678");
        employee.setDepartment(department);

        Employee persistedEmployee = entityManager.persistAndFlush(employee);

        // Find the employee by ID
        Optional<Employee> foundEmployee = employeeRepository.findById(persistedEmployee.getId());

        // Verify the employee was found
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("Jane");
        assertThat(foundEmployee.get().getLastName()).isEqualTo("Smith");
        assertThat(foundEmployee.get().getDepartment().getName()).isEqualTo("Marketing");
    }

    @Test
    public void shouldFindAllEmployees() {
        // Create a department
        Department department = new Department();
        department.setName("HR");
        department.setLocation("Building C");
        entityManager.persist(department);

        // Create and persist multiple employees
        Employee emp1 = new Employee();
        emp1.setFirstName("Robert");
        emp1.setLastName("Johnson");
        emp1.setEmail("robert.johnson@hitorro.com");
        emp1.setSalary(55000.0);
        emp1.setHireDate(LocalDate.of(2019, 5, 20));
        emp1.setPhoneNumber("555-9012");
        emp1.setDepartment(department);

        Employee emp2 = new Employee();
        emp2.setFirstName("Lisa");
        emp2.setLastName("Brown");
        emp2.setEmail("lisa.brown@hitorro.com");
        emp2.setSalary(60000.0);
        emp2.setHireDate(LocalDate.of(2022, 2, 15));
        emp2.setPhoneNumber("555-3456");
        emp2.setDepartment(department);

        entityManager.persist(emp1);
        entityManager.persist(emp2);
        entityManager.flush();

        // Find all employees
        List<Employee> employees = employeeRepository.findAll();

        // Verify all employees were found
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindEmployeesByDepartment() {
        // Create departments
        Department hr = new Department();
        hr.setName("HR");
        hr.setLocation("Building C");
        entityManager.persist(hr);

        Department finance = new Department();
        finance.setName("Finance");
        finance.setLocation("Building D");
        entityManager.persist(finance);

        // Create and persist employees in different departments
        Employee emp1 = new Employee();
        emp1.setFirstName("Michael");
        emp1.setLastName("Wilson");
        emp1.setEmail("michael.wilson@hitorro.com");
        emp1.setSalary(70000.0);
        emp1.setHireDate(LocalDate.of(2018, 8, 5));
        emp1.setPhoneNumber("555-7890");
        emp1.setDepartment(hr);

        Employee emp2 = new Employee();
        emp2.setFirstName("Sarah");
        emp2.setLastName("Davis");
        emp2.setEmail("sarah.davis@hitorro.com");
        emp2.setSalary(80000.0);
        emp2.setHireDate(LocalDate.of(2020, 10, 12));
        emp2.setPhoneNumber("555-2345");
        emp2.setDepartment(finance);

        Employee emp3 = new Employee();
        emp3.setFirstName("David");
        emp3.setLastName("Clark");
        emp3.setEmail("david.clark@hitorro.com");
        emp3.setSalary(75000.0);
        emp3.setHireDate(LocalDate.of(2021, 6, 18));
        emp3.setPhoneNumber("555-6789");
        emp3.setDepartment(hr);

        entityManager.persist(emp1);
        entityManager.persist(emp2);
        entityManager.persist(emp3);
        entityManager.flush();

        // Find employees by department
        List<Employee> hrEmployees = employeeRepository.findByDepartmentName("HR");

        // Verify the correct employees were found
        assertThat(hrEmployees).isNotNull();
        assertThat(hrEmployees.size()).isEqualTo(2);
        assertThat(hrEmployees).extracting(Employee::getFirstName)
                .containsExactlyInAnyOrder("Michael", "David");
    }

    @Test
    public void shouldFindEmployeesByLastName() {
        // Create a department
        Department department = new Department();
        department.setName("IT");
        department.setLocation("Building E");
        entityManager.persist(department);

        // Create and persist employees
        Employee emp1 = new Employee();
        emp1.setFirstName("James");
        emp1.setLastName("Smith");
        emp1.setEmail("james.smith@hitorro.com");
        emp1.setSalary(85000.0);
        emp1.setHireDate(LocalDate.of(2019, 11, 3));
        emp1.setPhoneNumber("555-4321");
        emp1.setDepartment(department);

        Employee emp2 = new Employee();
        emp2.setFirstName("Maria");
        emp2.setLastName("Garcia");
        emp2.setEmail("maria.garcia@hitorro.com");
        emp2.setSalary(90000.0);
        emp2.setHireDate(LocalDate.of(2020, 4, 22));
        emp2.setPhoneNumber("555-8765");
        emp2.setDepartment(department);

        Employee emp3 = new Employee();
        emp3.setFirstName("Kevin");
        emp3.setLastName("Smith");
        emp3.setEmail("kevin.smith@hitorro.com");
        emp3.setSalary(82000.0);
        emp3.setHireDate(LocalDate.of(2021, 9, 8));
        emp3.setPhoneNumber("555-0123");
        emp3.setDepartment(department);

        entityManager.persist(emp1);
        entityManager.persist(emp2);
        entityManager.persist(emp3);
        entityManager.flush();

        // Find employees by last name
        List<Employee> smithEmployees = employeeRepository.findByLastName("Smith");

        // Verify the correct employees were found
        assertThat(smithEmployees).isNotNull();
        assertThat(smithEmployees.size()).isEqualTo(2);
        assertThat(smithEmployees).extracting(Employee::getFirstName)
                .containsExactlyInAnyOrder("James", "Kevin");
    }

    @Test
    public void shouldFindEmployeesBySalaryGreaterThan() {
        // Create a department
        Department department = new Department();
        department.setName("Sales");
        department.setLocation("Building F");
        entityManager.persist(department);

        // Create and persist employees with different salaries
        Employee emp1 = new Employee();
        emp1.setFirstName("Thomas");
        emp1.setLastName("Anderson");
        emp1.setEmail("thomas.anderson@hitorro.com");
        emp1.setSalary(60000.0);
        emp1.setHireDate(LocalDate.of(2020, 7, 14));
        emp1.setPhoneNumber("555-5432");
        emp1.setDepartment(department);
    }
}

