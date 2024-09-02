package pl.ariglos.tickettracker.tickets.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.tickets.domain.Attachment;
import pl.ariglos.tickettracker.tickets.dto.CreateTicketItem;
import pl.ariglos.tickettracker.tickets.dto.ModifyTicketItem;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.services.TicketService;

import java.util.Base64;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @GetMapping()
  public ResponseEntity<?> browseTickets(BrowseTickets query) throws TicketTrackerException {
    Page<TicketDto> tickets = ticketService.browseTickets(query);

    return ResponseEntity.ok(tickets);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTicket(@PathVariable Long id) throws TicketTrackerException {
    TicketDto ticket = ticketService.getTicket(id);

    return ResponseEntity.ok(ticket);
  }

  @PostMapping()
  public ResponseEntity<?> createTicket(@RequestBody CreateTicketItem createTicketItem)
      throws TicketTrackerException {
    TicketDto ticket = ticketService.createTicket(createTicketItem);

    return ResponseEntity.ok(ticket);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> modifyTicket(
      @PathVariable Long id, @RequestBody ModifyTicketItem modifyTicketItem)
      throws TicketTrackerException {
    TicketDto ticket = ticketService.modifyTicket(id, modifyTicketItem);

    return ResponseEntity.ok(ticket);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTicket(@PathVariable Long id) throws TicketTrackerException {
    ticketService.deleteTicket(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/confirm")
  public ResponseEntity<?> confirmTicket(@PathVariable Long id) throws TicketTrackerException {
    ticketService.confirmTicket(id);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/attachments")
  public ResponseEntity<?> getAttachment(@PathVariable Long id) throws TicketTrackerException {
    Attachment attachment = ticketService.getAttachment(id);

    byte[] attachmentBytes = Base64.getDecoder().decode(attachment.getData());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename(attachment.getFileName()).build());

    return new ResponseEntity<>(attachmentBytes, headers, HttpStatus.OK);
  }

  @DeleteMapping("/{id}/attachments")
  public ResponseEntity<?> deleteAttachment(@PathVariable Long id) throws TicketTrackerException {
    ticketService.deleteAttachment(id);

    return ResponseEntity.ok().build();
  }
}
