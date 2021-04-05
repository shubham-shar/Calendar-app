package com.calendar.assistant.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

/**
 * @author shubham sharma
 *         <p>
 *         04/04/21
 */
@Data
@Entity
public class EmployeeCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    
    @Column(name = "calendar_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
    private Date calendarDate;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "calendars")
    private Employee employee;
    
    @OneToMany(mappedBy = "employeeCalendar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "events")
    private Set<Event> events;
    
    @Override
    public String toString() {
        return "EmployeeCalender{" + "id=" + id + ", date=" + calendarDate + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + ", events=" + events + '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeCalendar)) {
            return false;
        }
        EmployeeCalendar that = (EmployeeCalendar) o;
        return Objects.equals(id, that.id) && Objects.equals(calendarDate, that.calendarDate) && Objects
                .equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects
                .equals(events, that.events);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, calendarDate, createdAt, updatedAt, events);
    }
}
