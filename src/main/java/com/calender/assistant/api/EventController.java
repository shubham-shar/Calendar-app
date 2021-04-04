package com.calender.assistant.api;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import javax.validation.Valid;

import com.calender.assistant.model.dto.BookEvent;
import com.calender.assistant.model.dto.EventDto;
import com.calender.assistant.service.EventService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    EventService eventService;
    
    @PostMapping("/book")
    private String bookEventForEmployee(@RequestParam Long id, @Valid @RequestBody BookEvent bookEvent) {
        log.info("Received request to book event for {} with details {}", id, bookEvent.toString());
        eventService.bookEvent(id, bookEvent);
        return "Event Booked";
    }
    
    @GetMapping("/available-slots")
    private Set<EventDto> findAvailableSlots(@RequestParam Long firstEmpId, @RequestParam Long secondEmpId,
            @Valid @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata") Date date) throws ParseException {
        log.info("Received request to find slots {} and {} on {}", firstEmpId, secondEmpId, date);
        return eventService.fetchSlots(firstEmpId, secondEmpId, date);
    }
}
