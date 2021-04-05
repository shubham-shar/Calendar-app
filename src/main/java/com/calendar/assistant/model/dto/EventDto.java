package com.calendar.assistant.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
@Getter
@Builder
public class EventDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private Date startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private Date endDate;
    
    @Override
    public String toString() {
        return "{" + "startDate: " + startDate + ", endDate: " + endDate + '}';
    }
}
