package pl.ariglos.tickettracker.common.api.exceptions;

import lombok.Getter;

@Getter
public class TicketTrackerException extends Exception {
    private final String errorCode;

    public TicketTrackerException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
