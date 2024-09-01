package pl.ariglos.tickettracker.common.api.enumerations;

import lombok.Getter;

@Getter
public enum SortDirection {
    ASC("ASC"),
    DESC("DESC");

    private final String value;

    SortDirection(String value) {
        this.value = value;
    }
}