package pl.ariglos.tickettracker.common.emails;

import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;

public interface EmailService {

    void sendEmail(String receiver, String subject, String body) throws TicketTrackerException;
}
