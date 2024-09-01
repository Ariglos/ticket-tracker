package pl.ariglos.tickettracker.common.converters;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

public abstract class AutoRegisteringConverter<S, T> implements Converter<S, T> {

  private ConversionService conversionService;

  @Autowired
  private void setConversionService(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @PostConstruct
  private void register() {
    if (conversionService instanceof GenericConversionService) {
      ((GenericConversionService) conversionService).addConverter(this);
    }
  }
}
