package pl.ariglos.tickettracker.employees.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CompanyDto {
    private long id;
    private String name;
}
