package pl.ariglos.tickettracker.tickets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;
import pl.ariglos.tickettracker.tickets.enumerations.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class TicketDto {
    private long id;

    private String employee;
    private Long employeeId;
    private String company;
    private String employeePhoneNo;
    private String signature;
    private String offence;
    private Long offenceId;

    private BigDecimal fineAmount;
    private BigDecimal administrationFee;
    private Currency currency;

    private TicketStatus status;

    private LocalDate offenceDate;
    private LocalDate dueDate;
}
