package pl.ariglos.tickettracker.tickets.converters;

import org.springframework.stereotype.Component;
import pl.ariglos.tickettracker.common.converters.AutoRegisteringConverter;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.dto.OffenceDto;

@Component
public class OffenceDtoConverter extends AutoRegisteringConverter<Offence, OffenceDto> {

  @Override
  public OffenceDto convert(Offence source) {
    return OffenceDto.builder().id(source.getId()).description(source.getDescription()).build();
  }
}
