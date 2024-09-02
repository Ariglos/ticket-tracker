package pl.ariglos.tickettracker.tickets.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;

public interface TicketMailService {

  void notifyEmployeeAboutTicket(
      Long ticketId,
      String ticketSignature,
      LocalDate dueDate,
      BigDecimal fineAmount,
      Currency fineCurrency)
      throws TicketTrackerException;
}
