package com.calender.assistant.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.calender.assistant.exceptions.EntityNotFoundException;
import com.calender.assistant.exceptions.InvalidRequestException;
import com.calender.assistant.exceptions.UnauthorizedException;
import com.calender.assistant.model.Employee;
import com.calender.assistant.model.EmployeeCalender;
import com.calender.assistant.model.Event;
import com.calender.assistant.model.dto.BookEvent;
import com.calender.assistant.model.dto.EventDto;
import com.calender.assistant.repository.EmployeeCalenderRepository;
import com.calender.assistant.repository.EmployeeRepository;
import com.calender.assistant.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
@Slf4j
@Service
public class EventService {
    
    public static final String ASIA_KOLKATA = "Asia/Kolkata";
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EmployeeCalenderRepository employeeCalenderRepository;
    
    @Transactional
    public void bookEvent(Long id, BookEvent bookEvent) {
        
        validateRequest(bookEvent);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            log.error("Employee not found with id: {}", id);
            throw new EntityNotFoundException("Employee not found with id: " + id);
        });
    
        Optional<EmployeeCalender> employeeCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalenderDate(id, bookEvent.getDate());
    
        employeeCalender.ifPresentOrElse(calender -> {
            Set<Event> events = Optional.ofNullable(calender.getEvents()).orElse(new HashSet<>());
            if(events.isEmpty()) {
                Event event = createEvent(bookEvent);
                event.setEmployeeCalender(calender);
                calender.getEvents().add(event);
                employeeCalenderRepository.save(calender);
            } else {
                if(isAnySlotPresentForEmployee(events, bookEvent)) {
                    Event event = createEvent(bookEvent);
                    event.setEmployeeCalender(calender);
                    calender.getEvents().add(event);
                    employeeCalenderRepository.save(calender);
                } else {
                    log.info("employee {} not available for meeting for given time", id);
                    throw new UnauthorizedException( "employee " + id + " not available for meeting for given time");
                }
            }
        }, () -> {
            EmployeeCalender empCalender = new EmployeeCalender();
            empCalender.setEmployee(employee);
            empCalender.setCalenderDate(bookEvent.getDate());
            Event event = createEvent(bookEvent);
            event.setEmployeeCalender(empCalender);
            empCalender.setEvents(Collections.singleton(event));
            empCalender.setEmployee(employee);
            employee.getCalenders().add(empCalender);
            employeeRepository.save(employee);
        });
        
    
    }
    
    private boolean isAnySlotPresentForEmployee(Set<Event> events, BookEvent bookEvent) {
        return events.stream().noneMatch(event -> {
            return event.getStartTime().toInstant().equals(bookEvent.getStartTime().toInstant())
                    || (event.getStartTime().before(bookEvent.getStartTime())
                            && event.getEndTime().after(bookEvent.getStartTime()))
                    || (event.getStartTime().before(bookEvent.getEndTime())
                            && event.getEndTime().after(bookEvent.getEndTime()))
                    || (event.getEndTime().toInstant().equals(bookEvent.getEndTime().toInstant()));
    
        });
    }
    
    private Event createEvent(BookEvent bookEvent) {
        Event event = new Event();
        event.setTitle(bookEvent.getTitle());
        event.setDescription(bookEvent.getDescription());
        event.setStartTime(bookEvent.getStartTime());
        event.setEndTime(bookEvent.getEndTime());
        return event;
    }
    
    private void validateRequest(BookEvent bookEvent) {
        Date date = new Date();
        if(date.after(bookEvent.getDate()) || date.after(bookEvent.getStartTime()) || date.after(bookEvent.getEndTime())) {
            log.error("Event dates cannot be in the past");
            throw new InvalidRequestException("Event dates cannot be in the past");
        }
        if(bookEvent.getStartTime().after(bookEvent.getEndTime())) {
            log.error("Start time should be previous to end Time");
            throw new InvalidRequestException("Start time should be previous to end Time");
        }
    }
    
    public Set<EventDto> fetchSlots(Long firstEmpId, Long secondEmpId, Date date) throws ParseException {
        Employee firstEmployee = employeeRepository.findById(firstEmpId).orElseThrow(() -> {
            log.error("Employee not found with id: {}", firstEmpId);
            throw new EntityNotFoundException("Employee not found with id: " + firstEmpId);
        });
    
        Employee secondEmployee = employeeRepository.findById(secondEmpId).orElseThrow(() -> {
            log.error("Employee not found with id: {}", secondEmpId);
            throw new EntityNotFoundException("Employee not found with id: " + secondEmpId);
        });
    
        Optional<EmployeeCalender> firstEmpCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalenderDate(firstEmployee.getId(), date);
        Optional<EmployeeCalender> secondEmpCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalenderDate(secondEmployee.getId(), date);
    
        Set<Event> eventsOfFirstEmp = firstEmpCalender
                .map(cal -> Optional.ofNullable(cal.getEvents()).orElse(new HashSet<>())).orElse(new HashSet<>());
        Set<Event> eventsOfSecEmp = secondEmpCalender
                .map(cal -> Optional.ofNullable(cal.getEvents()).orElse(new HashSet<>())).orElse(new HashSet<>());
    
        LinkedList<Event> firstEvents = eventsOfFirstEmp.stream().sorted(Comparator.comparing(Event::getStartTime))
                                                    .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<Event> secondEvents = eventsOfSecEmp.stream().sorted(Comparator.comparing(Event::getStartTime))
                                                        .collect(Collectors.toCollection(LinkedList::new));
        
        LinkedList<EventDto> events = new LinkedList<>();
        for(int index=0; index < firstEvents.size(); index++) {
            EventDto eventDto = EventDto.builder()
                                        .startDate(firstEvents.get(index).getStartTime())
                                        .endDate(firstEvents.get(index).getEndTime())
                                        .build();
            events.add(eventDto);
        }
    
        for(int index=0; index < secondEvents.size(); index++) {
            EventDto eventDto = EventDto.builder()
                                        .startDate(secondEvents.get(index).getStartTime())
                                        .endDate(secondEvents.get(index).getEndTime())
                                        .build();
            events.add(eventDto);
        }
    
        events.sort(Comparator.comparing(EventDto::getStartDate));
    
        LocalDateTime start = date.toInstant().atZone(ZoneId.of(ASIA_KOLKATA)).toLocalDateTime();
        LocalDateTime end = start.plusDays(1);
        Set<EventDto> response =  new HashSet<>();
        LocalDateTime lastEndTime = start;
        for(int index=0; index < events.size(); index++) {
            LocalDateTime startTime = events.get(index).getStartDate().toInstant().atZone(ZoneId.of(ASIA_KOLKATA))
                                        .toLocalDateTime();
            LocalDateTime endTime = events.get(index).getEndDate().toInstant().atZone(ZoneId.of(ASIA_KOLKATA))
                                            .toLocalDateTime();
            if(start.compareTo(startTime) < 0 ) {
    
                EventDto build = EventDto.builder()
                                         .startDate(Date.from(start.atZone(ZoneId.of(ASIA_KOLKATA)).toInstant()))
                                         .endDate(Date.from(startTime.atZone(ZoneId.of(ASIA_KOLKATA)).toInstant()))
                                         .build();
                response.add(build);
                start = endTime;
                lastEndTime = endTime;
            }
            
            if(endTime.compareTo(end) >= 0) {
                break;
            }
        }
        if(lastEndTime.compareTo(end) < 0) {
            EventDto build = EventDto.builder()
                                     .startDate(Date.from(lastEndTime.atZone(ZoneId.of(ASIA_KOLKATA)).toInstant()))
                                     .endDate(Date.from(end.atZone(ZoneId.of(ASIA_KOLKATA)).toInstant()))
                                     .build();
            response.add(build);
        }
        return response.stream().sorted(Comparator.comparing(EventDto::getStartDate)).collect(
                Collectors.toCollection(LinkedHashSet::new));
    }
}
