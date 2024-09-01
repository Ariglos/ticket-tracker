package pl.ariglos.tickettracker.employees.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.employees.dto.CompanyDto;
import pl.ariglos.tickettracker.employees.services.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<?> getCompanies() throws TicketTrackerException {
        List<CompanyDto> companies = this.companyService.getCompanies();

        return ResponseEntity.ok(companies);
    }
}
