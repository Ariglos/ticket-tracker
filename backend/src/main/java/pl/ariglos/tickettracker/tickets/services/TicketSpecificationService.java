package pl.ariglos.tickettracker.tickets.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.ariglos.tickettracker.common.api.BrowseQuery;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;

public interface TicketSpecificationService {

  Specification<Ticket> createSpecification(BrowseTickets query);

  Pageable createPageable(BrowseQuery query);
}
