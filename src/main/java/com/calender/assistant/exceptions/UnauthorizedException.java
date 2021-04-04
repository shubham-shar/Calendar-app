package com.calender.assistant.exceptions;

/**
 * @author shubham sharma
 *         <p>
 *         28/03/21
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
