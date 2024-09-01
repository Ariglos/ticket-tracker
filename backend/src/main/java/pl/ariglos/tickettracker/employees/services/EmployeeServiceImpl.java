package pl.ariglos.tickettracker.employees.services;

import java.util.List;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.employees.dto.EmployeeDto;
import pl.ariglos.tickettracker.employees.repositories.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.employeeRepository = employeeRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public List<EmployeeDto> getEmployees() throws TicketTrackerException {
    List<Employee> employees = this.employeeRepository.findAll();

    try {
      return employees.stream()
          .map(employee -> conversionService.convert(employee, EmployeeDto.class))
          .toList();
    } catch (Exception e) {
      String errorCode = "EXC_001";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }
}
