package pl.ariglos.tickettracker.employees.converters;

import org.springframework.stereotype.Component;
import pl.ariglos.tickettracker.common.converters.AutoRegisteringConverter;
import pl.ariglos.tickettracker.employees.domain.Company;
import pl.ariglos.tickettracker.employees.dto.CompanyDto;

@Component
public class CompanyDtoConverter extends AutoRegisteringConverter<Company, CompanyDto> {

  @Override
  public CompanyDto convert(Company source) {
    return CompanyDto.builder().id(source.getId()).name(source.getName()).build();
  }
}
