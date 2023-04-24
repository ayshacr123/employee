package com.ust.employee.exception;

/**
 * Exception thrown when attempting to create a new user with an email that already exists in the system.
 */
public class EmailAlreadyExistException extends Exception {

    /**
     * Constructs a new {@code EmailAlreadyExistException} with the specified detail message.
     *
     * @param emailAlreadyInUse the email that already exists in the system
     */
    public EmailAlreadyExistException(String emailAlreadyInUse) {
        super(emailAlreadyInUse);
    }
}

