package pl.ariglos.tickettracker.tickets.services.implementations;

import java.util.List;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.dto.OffenceDto;
import pl.ariglos.tickettracker.tickets.repositories.OffenceRepository;
import pl.ariglos.tickettracker.tickets.services.OffenceService;

@Service
public class OffenceServiceImpl implements OffenceService {

  private final OffenceRepository offenceRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public OffenceServiceImpl(
      OffenceRepository offenceRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.offenceRepository = offenceRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public List<OffenceDto> getOffences() throws TicketTrackerException {
    List<Offence> offences = this.offenceRepository.findAll();

    try {
      return offences.stream()
              .map(offence -> conversionService.convert(offence, OffenceDto.class))
              .toList();
    } catch (Exception e) {
      String errorCode = "EXC_002";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }
}
