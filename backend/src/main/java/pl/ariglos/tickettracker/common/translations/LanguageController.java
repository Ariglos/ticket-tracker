package pl.ariglos.tickettracker.common.translations;

public interface LanguageController {
    String get(String key);

    String getString(String key, String... text);
}
