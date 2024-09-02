package pl.ariglos.tickettracker.tickets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ariglos.tickettracker.tickets.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {}
