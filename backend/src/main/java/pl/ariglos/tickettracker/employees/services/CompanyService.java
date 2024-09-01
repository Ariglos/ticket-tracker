package pl.ariglos.tickettracker.employees.services;

import java.util.List;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.employees.dto.CompanyDto;

public interface CompanyService {

  List<CompanyDto> getCompanies() throws TicketTrackerException;
}
