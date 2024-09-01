package pl.ariglos.tickettracker.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ariglos.tickettracker.common.api.enumerations.SortDirection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BrowseQuery {
    private int limit = 10;
    private int page = 0;
    private String sortBy;
    private SortDirection sortDirection = SortDirection.DESC;
}

