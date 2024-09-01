package pl.ariglos.tickettracker.tickets.services;

import org.springframework.data.domain.Page;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;

public interface TicketService {

  Page<TicketDto> browseTickets(BrowseTickets query) throws TicketTrackerException;

  TicketDto getTicket(Long id) throws TicketTrackerException;
}
