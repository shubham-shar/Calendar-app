package com.calender.assistant.repository;

import java.util.Date;
import java.util.Optional;

import com.calender.assistant.model.EmployeeCalender;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
public interface EmployeeCalenderRepository extends JpaRepository<EmployeeCalender, Long> {
    
    Optional<EmployeeCalender> findByEmployeeIdAndCalenderDate(Long id, Date date);
}
