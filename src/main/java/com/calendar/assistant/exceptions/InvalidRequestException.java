package com.calendar.assistant.exceptions;

/**
 * @author shubham sharma
 *         <p>
 *         28/03/21
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
