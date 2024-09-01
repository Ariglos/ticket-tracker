package pl.ariglos.tickettracker.common.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LanguageControllerImpl implements LanguageController {

    private final MessageSource messageSource;

    @Autowired
    public LanguageControllerImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(key, null, locale);
    }

    public String getString(String key, String... text) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(key, text, locale);
    }
}