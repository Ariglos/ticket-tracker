package pl.ariglos.tickettracker.tickets.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;

    @Lob
    private String data;

}
