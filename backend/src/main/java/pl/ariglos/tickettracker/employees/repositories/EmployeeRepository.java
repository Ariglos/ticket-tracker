package pl.ariglos.tickettracker.employees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ariglos.tickettracker.employees.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
