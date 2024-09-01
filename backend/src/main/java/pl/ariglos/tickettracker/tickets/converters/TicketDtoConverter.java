package pl.ariglos.tickettracker.tickets.converters;

import org.springframework.stereotype.Component;
import pl.ariglos.tickettracker.common.converters.AutoRegisteringConverter;
import pl.ariglos.tickettracker.employees.domain.Company;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;

@Component
public class TicketDtoConverter extends AutoRegisteringConverter<Ticket, TicketDto> {

  @Override
  public TicketDto convert(Ticket source) {
    Employee employee = source.getEmployee();
    Company company = employee.getCompany();
    Offence offence = source.getOffence();

    String employeeFullName = employee.getName() + " " + employee.getSurname();

    String offenceDescription;
    if (source.getCustomOffence() != null) {
      offenceDescription = source.getCustomOffence();
    } else {
      offenceDescription = offence != null ? offence.getDescription() : "";
    }

    return TicketDto.builder()
        .id(source.getId())
        .employee(employeeFullName)
        .company(company.getName())
        .employeePhoneNo(employee.getPhoneNo())
        .signature(source.getSignature())
        .offence(offenceDescription)
        .fineAmount(source.getFineAmount())
        .administrationFee(source.getAdministrationFee())
        .currency(source.getCurrency())
        .status(source.getStatus())
        .offenceDate(source.getOffenceDate())
        .dueDate(source.getDueDate())
        .build();
  }
}
