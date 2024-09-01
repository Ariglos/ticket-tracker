package pl.ariglos.tickettracker.tickets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AttachmentDto {
  private String fileName;
  private String base64File;
}
