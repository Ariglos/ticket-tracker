package pl.ariglos.tickettracker.tickets.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.dto.OffenceDto;
import pl.ariglos.tickettracker.tickets.services.OffenceService;

@CrossOrigin
@RestController
@RequestMapping("/offences")
public class OffenceController {

  private final OffenceService offenceService;

  public OffenceController(OffenceService offenceService) {
    this.offenceService = offenceService;
  }

  @GetMapping
  public ResponseEntity<?> getCompanies() throws TicketTrackerException {
    List<OffenceDto> offences = this.offenceService.getOffences();

    return ResponseEntity.ok(offences);
  }
}
