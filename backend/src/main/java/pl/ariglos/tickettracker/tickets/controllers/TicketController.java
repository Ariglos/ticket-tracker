package pl.ariglos.tickettracker.tickets.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.services.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping()
    public ResponseEntity<?> browseTickets(BrowseTickets query) throws TicketTrackerException {
        Page<TicketDto> tickets = ticketService.browseTickets(query);

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable Long id) throws TicketTrackerException {
        TicketDto ticket = ticketService.getTicket(id);

        return ResponseEntity.ok(ticket);
    }
}
