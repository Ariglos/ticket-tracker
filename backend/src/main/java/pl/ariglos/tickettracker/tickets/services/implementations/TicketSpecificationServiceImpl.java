package pl.ariglos.tickettracker.tickets.services.implementations;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.BrowseQuery;
import pl.ariglos.tickettracker.employees.domain.Company;
import pl.ariglos.tickettracker.employees.domain.Employee;
import pl.ariglos.tickettracker.tickets.domain.Offence;
import pl.ariglos.tickettracker.tickets.domain.Ticket;
import pl.ariglos.tickettracker.tickets.queries.BrowseTickets;
import pl.ariglos.tickettracker.tickets.services.TicketSpecificationService;

@Service
public class TicketSpecificationServiceImpl implements TicketSpecificationService {
  @Override
  public Specification<Ticket> createSpecification(BrowseTickets query) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicateList = new ArrayList<>();

      if (StringUtils.isNotBlank(query.getName())) {
        Join<Ticket, Employee> ticketEmployeeJoin = root.join("employee");

        Predicate likeName =
            criteriaBuilder.like(
                criteriaBuilder.lower(ticketEmployeeJoin.get("name")),
                ("%" + query.getName() + "%").toLowerCase());
        predicateList.add(likeName);
      }

      if (StringUtils.isNotBlank(query.getSurname())) {
        Join<Ticket, Employee> ticketEmployeeJoin = root.join("employee");

        Predicate likeSurname =
                criteriaBuilder.like(
                        criteriaBuilder.lower(ticketEmployeeJoin.get("surname")),
                        ("%" + query.getSurname() + "%").toLowerCase());
        predicateList.add(likeSurname);
      }

      if (StringUtils.isNotBlank(query.getPhoneNo())) {
        Join<Ticket, Employee> ticketEmployeeJoin = root.join("employee");

        Predicate likePhoneNo =
                criteriaBuilder.like(
                        criteriaBuilder.lower(ticketEmployeeJoin.get("phoneNo")),
                        ("%" + query.getPhoneNo() + "%").toLowerCase());
        predicateList.add(likePhoneNo);
      }

      if (StringUtils.isNotBlank(query.getSignature())) {
        Predicate likeSignature =
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("signature")),
                ("%" + query.getSignature() + "%").toLowerCase());
        predicateList.add(likeSignature);
      }

      if (query.getStatus() != null) {
        Predicate equalsStatus = criteriaBuilder.equal(root.get("status"), query.getStatus());
        predicateList.add(equalsStatus);
      }

      if (query.getCurrency() != null) {
        Predicate equalsCurrency = criteriaBuilder.equal(root.get("currency"), query.getCurrency());
        predicateList.add(equalsCurrency);
      }

      if (query.getOffenceDate() != null) {
        Predicate equalsOffenceDate =
            criteriaBuilder.equal(root.get("offenceDate"), query.getOffenceDate());
        predicateList.add(equalsOffenceDate);
      }

      if (query.getDueDate() != null) {
        Predicate equalsDueDate = criteriaBuilder.equal(root.get("dueDate"), query.getDueDate());
        predicateList.add(equalsDueDate);
      }

      if (query.getCompanyId() != null && query.getCompanyId() > 0) {
        Join<Ticket, Employee> ticketEmployeeJoin = root.join("employee");
        Join<Employee, Company> employeeCompanyJoin = ticketEmployeeJoin.join("company");

        Predicate equalsCompany =
            criteriaBuilder.equal(employeeCompanyJoin.get("id"), query.getCompanyId());
        predicateList.add(equalsCompany);
      }

      if (query.getOffenceId() != null && query.getOffenceId() > 0) {
        Join<Ticket, Offence> ticketOffenceJoin = root.join("offence");

        Predicate equalsOffence =
            criteriaBuilder.equal(ticketOffenceJoin.get("id"), query.getOffenceId());
        predicateList.add(equalsOffence);
      }

      return criteriaBuilder.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
    };
  }

  @Override
  public Pageable createPageable(BrowseQuery query) {
    int pageNumber = query.getPage();
    int pageSize = query.getLimit();

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

    if (query.getSortBy() != null) {
      pageRequest =
          pageRequest.withSort(
              Sort.Direction.fromString(query.getSortDirection().getValue()), query.getSortBy());
    }

    return pageRequest;
  }
}
