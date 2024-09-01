package pl.ariglos.tickettracker.tickets.queries;

import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.common.api.BrowseQuery;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;
import pl.ariglos.tickettracker.tickets.enumerations.TicketStatus;

import java.time.LocalDate;

@Getter
@Setter
public class BrowseTickets extends BrowseQuery {
    private String name;
    private String surname;
    private String phoneNo;
    private String signature;
    private TicketStatus status;
    private Currency currency;
    private LocalDate offenceDate;
    private LocalDate dueDate;
    private Long companyId;
    private Long offenceId;
}
