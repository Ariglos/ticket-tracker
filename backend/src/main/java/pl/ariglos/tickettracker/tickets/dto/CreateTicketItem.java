package pl.ariglos.tickettracker.tickets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CreateTicketItem {
    private String signature;
    private BigDecimal fineAmount;
    private Currency currency;
    private LocalDate offenceDate;
    private LocalDate dueDate;
    private String customOffence;
    private Long offenceId;
    private Long employeeId;
    private AttachmentDto attachment;
}
