package pl.ariglos.tickettracker.employees.services;

import java.util.List;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.employees.dto.EmployeeDto;

public interface EmployeeService {

  List<EmployeeDto> getEmployees() throws TicketTrackerException;


}
