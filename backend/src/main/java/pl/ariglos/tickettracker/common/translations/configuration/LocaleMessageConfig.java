package pl.ariglos.tickettracker.common.translations.configuration;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleMessageConfig {

  private static final String MESSAGE_SOURCE_ENCODING = "UTF-8";
  private static final String RESOURCE_BUNDLE_BASE_NAME = "classpath:i18n/messages";
  private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("pl-PL");

  @Bean
  public MessageSource messageSource() {

    ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
    source.setBasename(RESOURCE_BUNDLE_BASE_NAME);
    source.setDefaultEncoding(MESSAGE_SOURCE_ENCODING);
    source.setDefaultLocale(DEFAULT_LOCALE);
    return source;
  }
}
