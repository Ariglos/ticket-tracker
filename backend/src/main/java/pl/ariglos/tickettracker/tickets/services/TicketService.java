package pl.ariglos.tickettracker.tickets.services;

import org.springframework.data.domain.Page;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;

public interface TicketService {

  Page<TicketDto> browseTickets() throws TicketTrackerException;

  TicketDto getTicket(Long id) throws TicketTrackerException;
}
