package pl.ariglos.tickettracker.tickets.services;

import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.OffenceDto;

import java.util.List;

public interface OffenceService {
    List<OffenceDto> getOffences() throws TicketTrackerException;
}
