package com.nmodi.ticketserviceapp.exception;

/**
 * This class defines any kind of exception that occurs with the seating arrangement.
 */
public class SeatingArrangementNotValidException extends RuntimeException {

    private static final long serialVersionUID = -254884840794121379L;

    private static final String DEFAULT_EXCEPTION_MESSAGE = "An error occurred while initializing/manipulating seating arrangement";

    private String message;

    private Throwable cause;

    /**
     * This is the no-arg constructor.
     */
    public SeatingArrangementNotValidException() {
        this(DEFAULT_EXCEPTION_MESSAGE);
    }

    /**
     * This is the single arg constructor with the message.
     * 
     * @param message The exception message to set
     */
    public SeatingArrangementNotValidException(String message) {
        this(message, null);
    }

    /**
     * This is the 2-arg constructor with the message and cause.
     * 
     * @param message The exception message to set
     * @param cause The cause to set
     */
    public SeatingArrangementNotValidException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    /**
     * Getter method for message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for message.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter method for cause.
     * 
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Setter method for cause.
     * 
     * @param cause the cause to set
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
