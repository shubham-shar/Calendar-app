package com.calender.assistant.repository;

import java.util.Set;

import com.calender.assistant.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    
    @Query(value = Queries.FETCH_EMPLOYEE_EVENTS)
    Set<Event> findEventByEmployeeId(@Param("id") Long id);
    
    class Queries {
        private Queries(){}
        
        private final static String FETCH_EMPLOYEE_EVENTS = "select e from Employee emp "
                + "join EmployeeCalender emp_cal on emp.id = emp_cal.employee.id "
                + "join Event e on e.employeeCalender.id  = emp_cal.id where emp.id = :id";
    }
}
