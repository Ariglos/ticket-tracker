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
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-tickets.page',
  standalone: true,
  hostDirectives: [DestroyedDirective],
  imports: [
    TicketListComponent,
    AsyncPipe,
    ToastModule
  ],
  providers: [
    MessageService
  ],
  template: `
    <p-toast/>
    <app-ticket-list [data]="tickets$ | async" [query]="query" [companies]="companies$ | async"
                     [employees]="employees$ | async" [offences]="offences$ | async" (reloadData)="reloadData($event)"
                     (confirm)="confirmTicket($event)" (delete)="deleteTicket($event)"
                     (deleteAttachment)="deleteAttachment($event)" (displayAttachment)="displayAttachment($event)"
                     (edit)="editTicket($event)" (navigate)="navigateToTicketDetails($event)"/>
  `,
  styles: ``
})
export class TicketsPage implements OnInit {
  private service = inject(TicketsApiService);
  private companyService = inject(CompanyApiService);
  private employeeService = inject(EmployeeApiService);
  private offenceService = inject(OffenceApiService);
  private messageService = inject(MessageService);
  private translateService = inject(TranslateService);
  private router = inject(Router);

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

  confirmTicket(ticket: TicketModel) {
    this.service.confirm(ticket.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          detail: this.translateService.instant('ticket.=.confirm.success')
        });
        this.refreshView();
      },
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: err.error
      }),
    })
  }

  deleteTicket(ticket: TicketModel) {
    this.service.deleteTicket(ticket.id).subscribe({
      next: () => {
        this.messageService.add({severity: 'success', detail: this.translateService.instant('ticket.delete.success')});
        this.refreshView();
      },
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: err.error
      }),
    })
  }

  deleteAttachment(ticket: TicketModel) {
    this.service.deleteAttachments(ticket.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          detail: this.translateService.instant('ticket.attachment.delete.success')
        });
        this.refreshView();
      },
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: err.error
      }),
    })
  }

  displayAttachment(ticket: TicketModel) {
    this.service.getAttachment(ticket.id).subscribe({
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: this.translateService.instant('action.display-attachment.error')
      }),
    })
  }

  editTicket(ticket: TicketModel) {
    // TODO
  }

  navigateToTicketDetails(ticket: TicketModel) {
    this.router.navigate(["tickets", ticket.id]);
  }
}
