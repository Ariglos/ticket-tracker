package pl.ariglos.tickettracker.employees.converters;

import org.springframework.stereotype.Component;
import pl.ariglos.tickettracker.common.converters.AutoRegisteringConverter;
import pl.ariglos.tickettracker.employees.domain.Company;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.employees.dto.EmployeeDto;

@Component
public class EmployeeDtoConverter extends AutoRegisteringConverter<Employee, EmployeeDto> {

    @Override
    public EmployeeDto convert(Employee source) {
        Company company = source.getCompany();
        String companyName = company != null ? company.getName() : "";

        return EmployeeDto.builder()
                .id(source.getId())
                .name(source.getName())
                .surname(source.getSurname())
                .phoneNo(source.getPhoneNo())
                .email(source.getEmail())
                .status(source.getStatus())
                .companyName(companyName)
                .build();
    }
}
