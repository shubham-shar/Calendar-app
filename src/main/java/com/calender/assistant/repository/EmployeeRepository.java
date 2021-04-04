package com.calender.assistant.repository;

import com.calender.assistant.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
