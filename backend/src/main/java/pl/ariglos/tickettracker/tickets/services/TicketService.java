package pl.ariglos.tickettracker.tickets.services;

import org.springframework.data.domain.Page;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.ModifyTicketItem;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.dto.CreateTicketItem;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;

public interface TicketService {

  Page<TicketDto> browseTickets(BrowseTickets query) throws TicketTrackerException;

  TicketDto getTicket(Long id) throws TicketTrackerException;

  TicketDto createTicket(CreateTicketItem createTicketItem) throws TicketTrackerException;

  TicketDto modifyTicket(Long id, ModifyTicketItem modifyTicketItem) throws TicketTrackerException;

  void deleteTicket(Long id) throws TicketTrackerException;
}
