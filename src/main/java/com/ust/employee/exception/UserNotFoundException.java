package com.ust.employee.exception;

/**
 * Custom exception thrown when a user is not found.
 */
public class UserNotFoundException extends Exception {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

