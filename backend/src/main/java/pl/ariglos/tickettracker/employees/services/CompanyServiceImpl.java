package pl.ariglos.tickettracker.employees.services;

import java.util.List;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import pl.ariglos.tickettracker.employees.domain.Company;
import pl.ariglos.tickettracker.employees.dto.CompanyDto;
import pl.ariglos.tickettracker.employees.repositories.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;
  private final ConversionService conversionService;
  private final LanguageController languageController;

  public CompanyServiceImpl(
      CompanyRepository companyRepository,
      ConversionService conversionService,
      LanguageController languageController) {
    this.companyRepository = companyRepository;
    this.conversionService = conversionService;
    this.languageController = languageController;
  }

  @Override
  public List<CompanyDto> getCompanies() throws TicketTrackerException {
    List<Company> companies = this.companyRepository.findAll();

    try {
      return companies.stream()
          .map(company -> conversionService.convert(company, CompanyDto.class))
          .toList();
    } catch (Exception e) {
      String errorCode = "EXC_002";
      String message = languageController.get(errorCode);
      throw new TicketTrackerException(errorCode, message);
    }
  }
}
