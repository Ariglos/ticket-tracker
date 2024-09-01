package pl.ariglos.tickettracker.tickets.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;
import pl.ariglos.tickettracker.tickets.enumerations.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ticket")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String signature;

    @Column(nullable = false)
    private BigDecimal fineAmount;
    @Column(nullable = false)
    private BigDecimal administrationFee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(nullable = false)
    private LocalDate offenceDate;
    @Column(nullable = false)
    private LocalDate dueDate;

    private String customOffence;

    @ManyToOne()
    @JoinColumn(name = "offenceId")
    private Offence offence;

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
