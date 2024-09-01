package pl.ariglos.tickettracker.employees.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.employees.dto.EmployeeDto;
import pl.ariglos.tickettracker.employees.services.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public ResponseEntity<?> getEmployees() throws TicketTrackerException {
    List<EmployeeDto> employees = this.employeeService.getEmployees();

    return ResponseEntity.ok(employees);
  }
}
