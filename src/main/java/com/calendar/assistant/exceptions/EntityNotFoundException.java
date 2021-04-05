package com.calendar.assistant.exceptions;

/**
 * @author shubham sharma
 *         <p>
 *         28/03/21
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
