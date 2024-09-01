package pl.ariglos.tickettracker.tickets.services;

import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.repositories.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

  private final TicketRepository ticketRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public TicketServiceImpl(
      TicketRepository ticketRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.ticketRepository = ticketRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public Page<TicketDto> browseTickets() throws TicketTrackerException {
    return null;
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
