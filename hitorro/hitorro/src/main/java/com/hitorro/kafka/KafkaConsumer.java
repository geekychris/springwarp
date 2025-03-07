package com.hitorro.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitorro.model.Department;
import com.hitorro.model.Employee;
import com.hitorro.service.DepartmentService;
import com.hitorro.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private static final String DEPARTMENT_TOPIC = "department-topic";
    private static final String EMPLOYEE_TOPIC = "employee-topic";

    private final ObjectMapper objectMapper;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, DepartmentService departmentService, EmployeeService employeeService) {
        this.objectMapper = objectMapper;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @KafkaListener(topics = DEPARTMENT_TOPIC, groupId = "${spring.kafka.consumer.group-id:hitorro-group}",
            containerFactory = "departmentKafkaListenerContainerFactory")
    public void consumeDepartment(String message) {
        logger.info("Received department message: {}", message);
        try {
            Department department = objectMapper.readValue(message, Department.class);
            departmentService.saveDepartment(department);
            logger.info("Department created successfully: {}", department.getName());
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing department message: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing department message: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = EMPLOYEE_TOPIC, groupId = "${spring.kafka.consumer.group-id:hitorro-group}",
            containerFactory = "employeeKafkaListenerContainerFactory")
    public void consumeEmployee(String message) {
        logger.info("Received employee message: {}", message);
        try {
            Employee employee = objectMapper.readValue(message, Employee.class);
            
            // Check if department exists
            if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
                employeeService.saveEmployee(employee);
                logger.info("Employee created successfully: {}", employee.getFirstName() + " " + employee.getLastName());
            } else {
                logger.error("Cannot create employee without department information");
            }
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing employee message: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing employee message: {}", e.getMessage());
        }
    }
}

