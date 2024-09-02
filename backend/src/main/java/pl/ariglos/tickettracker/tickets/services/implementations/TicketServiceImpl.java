package pl.ariglos.tickettracker.tickets.services.implementations;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.employees.repositories.EmployeeRepository;
import pl.ariglos.tickettracker.tickets.domain.Attachment;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.dto.AttachmentDto;
import pl.ariglos.tickettracker.tickets.dto.CreateTicketItem;
import pl.ariglos.tickettracker.tickets.dto.ModifyTicketItem;
import pl.ariglos.tickettracker.tickets.dto.TicketDto;
import pl.ariglos.tickettracker.tickets.enumerations.TicketStatus;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.repositories.AttachmentRepository;
import pl.ariglos.tickettracker.tickets.repositories.OffenceRepository;
import pl.ariglos.tickettracker.tickets.repositories.TicketRepository;
import pl.ariglos.tickettracker.tickets.services.TicketMailService;
import pl.ariglos.tickettracker.tickets.services.TicketService;
import pl.ariglos.tickettracker.tickets.services.TicketSpecificationService;

@Service
public class TicketServiceImpl implements TicketService {

  private final TicketSpecificationService ticketSpecificationService;
  private final TicketMailService ticketMailService;
  private final TicketRepository ticketRepository;
  private final EmployeeRepository employeeRepository;
  private final OffenceRepository offenceRepository;
  private final AttachmentRepository attachmentRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public TicketServiceImpl(
      TicketSpecificationService ticketSpecificationService,
      TicketMailService ticketMailService,
      TicketRepository ticketRepository,
      EmployeeRepository employeeRepository,
      OffenceRepository offenceRepository,
      AttachmentRepository attachmentRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.ticketSpecificationService = ticketSpecificationService;
    this.ticketMailService = ticketMailService;
    this.ticketRepository = ticketRepository;
    this.employeeRepository = employeeRepository;
    this.offenceRepository = offenceRepository;
    this.attachmentRepository = attachmentRepository;
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
  @Transactional
  public TicketDto createTicket(CreateTicketItem createTicketItem) throws TicketTrackerException {
    TicketDto savedTicketDto;

    verifyIfSignatureIsUnique(createTicketItem.getSignature());

    Offence offence = findOffenceById(createTicketItem.getOffenceId());
    Employee employee = findEmployeeById(createTicketItem.getEmployeeId());
    Ticket ticket = convertToCrateEntity(createTicketItem, offence, employee);

    try {
      Ticket savedTicket = ticketRepository.save(ticket);
      savedTicketDto = conversionService.convert(savedTicket, TicketDto.class);

      ticketMailService.notifyEmployeeAboutTicket(
          savedTicket.getId(), savedTicket.getSignature(), savedTicket.getDueDate(), savedTicket.getFineAmount(), savedTicket.getCurrency());
    } catch (DataAccessException e) {
      String errorCode = "EXC_010";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return savedTicketDto;
  }

  @Override
  public TicketDto modifyTicket(Long id, ModifyTicketItem modifyTicketItem)
      throws TicketTrackerException {
    TicketDto modifiedTicketDto;

    Ticket ticketById = retrieveTicketById(id);

    if (ticketById.getStatus().equals(TicketStatus.PAID)) {
      String errorCode = "EXC_012";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    if (!modifyTicketItem.getSignature().equals(ticketById.getSignature())) {
      verifyIfSignatureIsUnique(modifyTicketItem.getSignature());
    }

    Offence offence = findOffenceById(modifyTicketItem.getOffenceId());
    Employee employee = findEmployeeById(modifyTicketItem.getEmployeeId());

    Ticket ticketToModify =
        ticketById.toBuilder()
            .signature(modifyTicketItem.getSignature())
            .fineAmount(modifyTicketItem.getFineAmount())
            .currency(modifyTicketItem.getCurrency())
            .offenceDate(modifyTicketItem.getOffenceDate())
            .dueDate(modifyTicketItem.getDueDate())
            .customOffence(modifyTicketItem.getCustomOffence())
            .offence(offence)
            .employee(employee)
            .build();

    try {
      Ticket modifiedTicket = ticketRepository.save(ticketToModify);
      modifiedTicketDto = conversionService.convert(modifiedTicket, TicketDto.class);
    } catch (DataAccessException e) {
      String errorCode = "EXC_011";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return modifiedTicketDto;
  }

  @Override
  public void deleteTicket(Long id) throws TicketTrackerException {
    Ticket ticketToDelete = retrieveTicketById(id);

    if (ticketToDelete.getStatus().equals(TicketStatus.PAID)) {
      String errorCode = "EXC_013";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    try {
      ticketRepository.delete(ticketToDelete);
    } catch (DataAccessException e) {
      String errorCode = "EXC_014";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }

  @Override
  public void confirmTicket(Long id) throws TicketTrackerException {
    Ticket ticketToConfirm = retrieveTicketById(id);

    if (ticketToConfirm.getStatus().equals(TicketStatus.PAID)) {
      String errorCode = "EXC_015";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    ticketToConfirm.setStatus(TicketStatus.PAID);

    try {
      ticketRepository.save(ticketToConfirm);
    } catch (DataAccessException e) {
      String errorCode = "EXC_016";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }

  @Override
  public Attachment getAttachment(Long ticketId) throws TicketTrackerException {
    Ticket ticket = retrieveTicketById(ticketId);

    Attachment attachment = ticket.getAttachment();

    if (attachment == null) {
      String errorCode = "EXC_018";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return attachment;
  }

  @Override
  @Transactional
  public void deleteAttachment(Long ticketId) throws TicketTrackerException {
    Ticket ticket = retrieveTicketById(ticketId);

    Attachment attachment = ticket.getAttachment();

    if (attachment == null) {
      String errorCode = "EXC_018";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    try {
      ticket.setAttachment(null);
      ticketRepository.save(ticket);
      attachmentRepository.delete(attachment);

    } catch (DataAccessException e) {
      String errorCode = "EXC_017";
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

  private Employee findEmployeeById(Long id) throws TicketTrackerException {
    Optional<Employee> optionalEmployee = employeeRepository.findById(id);
    if (optionalEmployee.isEmpty()) {
      String errorCode = "EXC_009";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return optionalEmployee.get();
  }

  private Offence findOffenceById(Long id) throws TicketTrackerException {
    Optional<Offence> optionalOffence = offenceRepository.findById(id);
    if (optionalOffence.isEmpty()) {
      String errorCode = "EXC_008";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }

    return optionalOffence.get();
  }

  private void verifyIfSignatureIsUnique(String signature) throws TicketTrackerException {
    Optional<Ticket> ticketBySignature = ticketRepository.findBySignature(signature);

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
