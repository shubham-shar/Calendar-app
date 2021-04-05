package com.calendar.assistant.model.dto;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import lombok.Data;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
@Data
@Validated
public class BookEvent {
    
    @NotEmpty
    private String title;
    
    @NotEmpty
    private String description;
    
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
    private Date date;
    
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.NONE, pattern = "yyyy-mm-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kolkata")
    private Date startTime;
    
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.NONE, pattern = "yyyy-mm-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kolkata")
    private Date endTime;
    
    @Override
    public String toString() {
        return "BookEvent{" + "date: " + date + ", startTime: " + startTime + ", endTime: " + endTime + '}';
    }
}
