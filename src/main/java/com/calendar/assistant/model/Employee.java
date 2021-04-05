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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

/**
 * @author shubham sharma
 *         <p>
 *         03/04/21
 */
@Data
@Entity
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    
    @Email
    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at") // joining date
    private Date createdAt;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "calendars")
    private Set<EmployeeCalendar> calendars;
    
    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", emailId='" + emailId + '\'' + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + ", calender=" + calendars + '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(emailId, employee.emailId) && Objects
                .equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects
                .equals(createdAt, employee.createdAt) && Objects.equals(updatedAt, employee.updatedAt) && Objects
                .equals(calendars, employee.calendars);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, emailId, firstName, lastName, createdAt, updatedAt, calendars);
    }
}
