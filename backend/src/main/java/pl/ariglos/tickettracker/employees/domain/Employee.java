package pl.ariglos.tickettracker.employees.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.employees.enumerations.EmployeeStatus;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private Company company;
}
