import {Component, inject, OnInit} from '@angular/core';
import {TicketListComponent} from "../components/ticket-list.component";
import {TicketsApiService} from "../services/tickets.api.service";
import {DestroyedDirective} from "../../../utils/directives/destroyed.directive";
import {BehaviorSubject, Observable, switchMap} from "rxjs";
import {TicketModel} from "../models/ticket.model";
import {PageResult} from "../../../shared/api/page-result";
import {BrowseTickets} from "../queries/browse-tickets";
import {AsyncPipe} from "@angular/common";
import {CompanyModel} from "../../employees/models/company.model";
import {EmployeeModel} from "../../employees/models/employee.model";
import {CompanyApiService} from "../../employees/services/company.api.service";
import {EmployeeApiService} from "../../employees/services/employee.api.service";
import {OffenceModel} from "../models/offence.model";
import {OffenceApiService} from "../services/offence-api.service";

@Component({
  selector: 'app-tickets.page',
  standalone: true,
  hostDirectives: [DestroyedDirective],
  imports: [
    TicketListComponent,
    AsyncPipe
  ],
  template: `
    <app-ticket-list [data]="tickets$ | async" [query]="query" [companies]="companies$ | async"
                     [employees]="employees$ | async" [offences]="offences$ | async" (reloadData)="reloadData($event)"/>
  `,
  styles: ``
})
export class TicketsPage implements OnInit {
  private service = inject(TicketsApiService);
  private companyService = inject(CompanyApiService);
  private employeeService = inject(EmployeeApiService);
  private offenceService = inject(OffenceApiService);

  private destroyed$ = inject(DestroyedDirective).destroyed$;

  refresh$ = new BehaviorSubject<BrowseTickets>(null);
  tickets$: Observable<PageResult<TicketModel>>;
  companies$: Observable<CompanyModel[]>;
  employees$: Observable<EmployeeModel[]>;
  offences$: Observable<OffenceModel[]>;

  query: BrowseTickets;

  ngOnInit(): void {
    this.query = new BrowseTickets();

    this.tickets$ = this.refresh$.pipe(
      switchMap((query) => this.service.browse(query))
    );

    this.companies$ = this.companyService.getAll();
    this.employees$ = this.employeeService.getAll();
    this.offences$ = this.offenceService.getAll();
  }

  refreshView(query?: BrowseTickets) {
    const browseQuery = query ?? this.refresh$.value;
    this.refresh$.next(browseQuery);
  }

  reloadData(query: BrowseTickets) {
    this.refreshView(query);
  }
}
