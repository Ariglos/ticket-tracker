package pl.ariglos.tickettracker.employees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ariglos.tickettracker.employees.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {}
