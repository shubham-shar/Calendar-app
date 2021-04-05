package com.calendar.assistant.service;

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

import com.calendar.assistant.exceptions.EntityNotFoundException;
import com.calendar.assistant.exceptions.InvalidRequestException;
import com.calendar.assistant.exceptions.UnauthorizedException;
import com.calendar.assistant.model.Employee;
import com.calendar.assistant.model.EmployeeCalendar;
import com.calendar.assistant.model.Event;
import com.calendar.assistant.model.dto.BookEvent;
import com.calendar.assistant.model.dto.EventDto;
import com.calendar.assistant.model.dto.Meeting;
import com.calendar.assistant.repository.EmployeeRepository;
import com.calendar.assistant.repository.EventRepository;
import com.calendar.assistant.repository.EmployeeCalenderRepository;
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
    
        Optional<EmployeeCalendar> employeeCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalendarDate(id, bookEvent.getDate());
    
        employeeCalender.ifPresentOrElse(calender -> {
            Set<Event> events = Optional.ofNullable(calender.getEvents()).orElse(new HashSet<>());
            if(events.isEmpty()) {
                Event event = createEvent(bookEvent);
                event.setEmployeeCalendar(calender);
                calender.getEvents().add(event);
                employeeCalenderRepository.save(calender);
            } else {
                if(isAnySlotPresentForEmployee(events, bookEvent.getStartTime(), bookEvent.getEndTime())) {
                    Event event = createEvent(bookEvent);
                    event.setEmployeeCalendar(calender);
                    calender.getEvents().add(event);
                    employeeCalenderRepository.save(calender);
                } else {
                    log.info("employee {} not available for meeting for given time", id);
                    throw new UnauthorizedException("employee " + id + " not available for meeting for given time");
                }
            }
        }, () -> {
            EmployeeCalendar empCalender = new EmployeeCalendar();
            empCalender.setEmployee(employee);
            empCalender.setCalendarDate(bookEvent.getDate());
            Event event = createEvent(bookEvent);
            event.setEmployeeCalendar(empCalender);
            empCalender.setEvents(Collections.singleton(event));
            empCalender.setEmployee(employee);
            employee.getCalendars().add(empCalender);
            employeeRepository.save(employee);
        });
        
    
    }
    
    private boolean isAnySlotPresentForEmployee(Set<Event> events, Date start, Date end) {
        return events.stream().noneMatch(event -> {
            return event.getStartTime().toInstant().equals(start.toInstant())
                    || (event.getStartTime().before(start)
                            && event.getEndTime().after(start))
                    || (event.getStartTime().before(end)
                            && event.getEndTime().after(end))
                    || (event.getEndTime().toInstant().equals(end.toInstant()));
    
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
    
        Optional<EmployeeCalendar> firstEmpCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalendarDate(firstEmployee.getId(), date);
        Optional<EmployeeCalendar> secondEmpCalender =
                employeeCalenderRepository.findByEmployeeIdAndCalendarDate(secondEmployee.getId(), date);
    
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
    
    public Set<String> bookMeeting(Meeting meeting) {
        Set<Employee> employees = employeeRepository.findByEmailIdIn(meeting.getEmails());
        if(employees.size() != meeting.getEmails().size()) {
            Set<String> emails = employees.stream().map(Employee::getEmailId).collect(Collectors.toSet());
            Set<String> allEmails = meeting.getEmails();
            allEmails.removeAll(emails);
            log.info("Given email ids {} are not present in the system", allEmails);
            throw new InvalidRequestException("Given email ids " + allEmails + " are not present in the system");
        }
        Set<EmployeeCalendar> dates = employeeCalenderRepository
                .findByCalendarDateAndEmployeeIdIn(meeting.getDate(), employees.stream().map(Employee::getId)
                                                                               .collect(Collectors.toSet()));
        Set<String> response = new HashSet<>();
        dates.forEach(calender -> {
                if(!isAnySlotPresentForEmployee(calender.getEvents(), meeting.getStartTime(), meeting.getEndTime())) {
                    response.add(calender.getEmployee().getEmailId());
                }
             });
        return response;
    }
}
