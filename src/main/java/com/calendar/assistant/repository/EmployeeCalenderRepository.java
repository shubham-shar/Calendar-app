package com.calendar.assistant.repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import com.calendar.assistant.model.EmployeeCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
public interface EmployeeCalenderRepository extends JpaRepository<EmployeeCalendar, Long> {
    
    Optional<EmployeeCalendar> findByEmployeeIdAndCalendarDate(Long id, Date date);
    
    Set<EmployeeCalendar> findByCalendarDateAndEmployeeIdIn(Date date, Set<Long> ids);
    
}
