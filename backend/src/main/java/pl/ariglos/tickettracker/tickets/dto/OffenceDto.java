package pl.ariglos.tickettracker.tickets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OffenceDto {
  private long id;
  private String description;
}
