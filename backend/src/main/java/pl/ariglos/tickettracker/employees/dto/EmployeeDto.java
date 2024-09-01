package pl.ariglos.tickettracker.employees.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.ariglos.tickettracker.employees.enumerations.EmployeeStatus;

@Builder
@Getter
@Setter
public class EmployeeDto {
    private long id;
    private String name;
    private String surname;
    private String phoneNo;
    private String email;
    private EmployeeStatus status;
    private String companyName;
}
