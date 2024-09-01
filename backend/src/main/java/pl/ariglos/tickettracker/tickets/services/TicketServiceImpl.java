package pl.ariglos.tickettracker.tickets.services;

import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.repositories.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

  private final TicketSpecificationService ticketSpecificationService;
  private final TicketRepository ticketRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public TicketServiceImpl(
      TicketSpecificationService ticketSpecificationService,
      TicketRepository ticketRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.ticketSpecificationService = ticketSpecificationService;
    this.ticketRepository = ticketRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public Page<TicketDto> browseTickets(BrowseTickets query) throws TicketTrackerException {

    Page<TicketDto> ticketDtos = Page.empty();

    try {
      Specification<Ticket> specification = ticketSpecificationService.createSpecification(query);
      Pageable pageable = ticketSpecificationService.createPageable(query);

      ticketDtos = ticketRepository
              .findAll(specification, pageable)
              .map(ticket -> conversionService.convert(ticket, TicketDto.class));
    } catch (Exception e) {
      String errorCode = "EXC_003";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return ticketDtos;
  }

  @Override
  public TicketDto getTicket(Long id) throws TicketTrackerException {
    TicketDto ticketDto;

    try {
      Ticket ticket = retrieveTicketById(id);
      ticketDto = conversionService.convert(ticket, TicketDto.class);
    } catch (DataAccessException e) {
      String errorCode = "EXC_004";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return ticketDto;
  }

  private Ticket retrieveTicketById(Long id) throws TicketTrackerException {
    if (id == null) {
      String errorCode = "EXC_005";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    Optional<Ticket> optionalTicket = ticketRepository.findById(id);

    if (optionalTicket.isEmpty()) {
      String errorCode = "EXC_006";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return optionalTicket.get();
  }
}
