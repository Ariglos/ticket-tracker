package pl.ariglos.tickettracker.tickets.services;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.employees.repositories.EmployeeRepository;
import pl.ariglos.tickettracker.tickets.domain.Attachment;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.dto.AttachmentDto;
import pl.ariglos.tickettracker.tickets.dto.CreateTicketItem;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.enumerations.TicketStatus;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.repositories.OffenceRepository;
import pl.ariglos.tickettracker.tickets.repositories.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

  private final TicketSpecificationService ticketSpecificationService;
  private final TicketRepository ticketRepository;
  private final EmployeeRepository employeeRepository;
  private final OffenceRepository offenceRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public TicketServiceImpl(
      TicketSpecificationService ticketSpecificationService,
      TicketRepository ticketRepository,
      EmployeeRepository employeeRepository,
      OffenceRepository offenceRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.ticketSpecificationService = ticketSpecificationService;
    this.ticketRepository = ticketRepository;
    this.employeeRepository = employeeRepository;
    this.offenceRepository = offenceRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public Page<TicketDto> browseTickets(BrowseTickets query) throws TicketTrackerException {

    Page<TicketDto> ticketDtos;

    try {
      Specification<Ticket> specification = ticketSpecificationService.createSpecification(query);
      Pageable pageable = ticketSpecificationService.createPageable(query);

      ticketDtos =
          ticketRepository
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

  @Override
  public TicketDto createTicket(CreateTicketItem createItem) throws TicketTrackerException {
    try {
      validateTicket(createItem);

      Optional<Offence> optionalOffence = offenceRepository.findById(createItem.getOffenceId());
      if (optionalOffence.isEmpty()) {
        String errorCode = "EXC_008";
        String message = languageController.get(errorCode);
        throw new TicketTrackerException(errorCode, message);
      }

      Optional<Employee> optionalEmployee = employeeRepository.findById(createItem.getEmployeeId());
      if (optionalEmployee.isEmpty()) {
        String errorCode = "EXC_009";
        String message = languageController.get(errorCode);
        throw new TicketTrackerException(errorCode, message);
      }

      Ticket ticket =
          convertToCrateEntity(createItem, optionalOffence.get(), optionalEmployee.get());

      Ticket savedTicket = ticketRepository.save(ticket);

      return conversionService.convert(savedTicket, TicketDto.class);
    } catch (DataAccessException e) {
      String errorCode = "EXC_010";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
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

  private void validateTicket(CreateTicketItem ticketItem) throws TicketTrackerException {
    Optional<Ticket> ticketBySignature =
        ticketRepository.findBySignature(ticketItem.getSignature());
    if (ticketBySignature.isPresent()) {
      String errorCode = "EXC_007";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }

  private Ticket convertToCrateEntity(
      CreateTicketItem createItem, Offence offence, Employee employee) {

    AttachmentDto attachment = createItem.getAttachment();

    return Ticket.builder()
        .signature(createItem.getSignature())
        .fineAmount(createItem.getFineAmount())
        .administrationFee(BigDecimal.valueOf(100))
        .currency(createItem.getCurrency())
        .status(TicketStatus.PENDING)
        .offenceDate(createItem.getOffenceDate())
        .dueDate(createItem.getDueDate())
        .customOffence(createItem.getCustomOffence())
        .offence(offence)
        .employee(employee)
        .attachment(
            Attachment.builder()
                .fileName(attachment.getFileName())
                .data(attachment.getBase64File())
                .build())
        .build();
  }
}
