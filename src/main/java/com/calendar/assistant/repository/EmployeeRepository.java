package com.calendar.assistant.repository;

import java.util.Set;

import com.calendar.assistant.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Set<Employee> findByEmailIdIn(Set<String> emails);
}
