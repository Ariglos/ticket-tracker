import {Component, inject, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {TicketListComponent} from "../components/ticket-list.component";
import {TicketsApiService} from "../services/tickets.api.service";
import {DestroyedDirective} from "../../../utils/directives/destroyed.directive";
import {BehaviorSubject, Observable, switchMap, takeUntil} from "rxjs";
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
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";
import {Button} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {TicketFormComponent, TicketFormValue} from "../components/ticket-form.component";
import {CreateTicketRequest} from "../models/create-ticket.request";
import {ModifyTicketRequest} from "../models/modify-ticket.request";

@Component({
  selector: 'app-tickets.page',
  standalone: true,
  hostDirectives: [DestroyedDirective],
  imports: [
    TicketListComponent,
    AsyncPipe,
    ToastModule,
    Button,
    TranslateModule,
    DialogModule
  ],
  providers: [
    MessageService
  ],
  template: `
    <p-toast/>
    <div style="margin: 16px 0">
      <p-button
        severity="success"
        icon="pi pi-plus"
        label="{{'ticket.add' | translate}}"
        (onClick)="openAddTicketDialog()"
      />
    </div>
    <app-ticket-list [data]="tickets$ | async" [query]="query" [companies]="companies$ | async"
                     [employees]="employees$ | async" [offences]="offences$ | async" (reloadData)="reloadData($event)"
                     (confirm)="confirmTicket($event)" (delete)="deleteTicket($event)"
                     (deleteAttachment)="deleteAttachment($event)" (displayAttachment)="displayAttachment($event)"
                     (edit)="openEditTicketDialog($event)" (navigate)="navigateToTicketDetails($event)"/>
    <p-dialog
      [(visible)]="displayTicketForm"
      header="{{'ticket' | translate}}"
      [style]="{width: '30vw'}"
      [modal]="true"
      [draggable]="false"
      [resizable]="false">
      <ng-template #ticketForm></ng-template>
    </p-dialog>
  `,
  styles: `
    .btn {
      margin-bottom: 12px;
    }

  `
})
export class TicketsPage implements OnInit {

  @ViewChild('ticketForm', {read: ViewContainerRef})
  ticketFormContainer: ViewContainerRef;

  private service = inject(TicketsApiService);
  private companyService = inject(CompanyApiService);
  private employeeService = inject(EmployeeApiService);
  private offenceService = inject(OffenceApiService);
  private messageService = inject(MessageService);
  private translateService = inject(TranslateService);
  private router = inject(Router);

  private destroyed$ = inject(DestroyedDirective).destroyed$;

  refresh$: BehaviorSubject<BrowseTickets>;
  tickets$: Observable<PageResult<TicketModel>>;
  companies$: Observable<CompanyModel[]>;
  employees$: Observable<EmployeeModel[]>;
  offences$: Observable<OffenceModel[]>;

  query: BrowseTickets;

  displayTicketForm: boolean;

  ngOnInit(): void {
    this.query = new BrowseTickets();
    this.query.sortBy = 'id';

    this.refresh$ = new BehaviorSubject<BrowseTickets>(this.query);

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
          detail: this.translateService.instant('ticket.confirm.success')
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

  navigateToTicketDetails(ticket: TicketModel) {
    this.router.navigate(["tickets", ticket.id]);
  }

  openAddTicketDialog() {
    this.ticketFormContainer.clear();
    const formComponent = this.ticketFormContainer.createComponent(
      TicketFormComponent
    );

    const instance = formComponent.instance;
    instance.formSubmit.pipe(takeUntil(this.destroyed$)).subscribe((formValue: TicketFormValue) => {
      this.createTicket(formValue);
    })
    instance.cancel.pipe(takeUntil(this.destroyed$)).subscribe(() => this.displayTicketForm = false)

    this.displayTicketForm = true;
  }

  createTicket(ticket: CreateTicketRequest) {
    this.service.create(ticket).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          detail: this.translateService.instant('ticket.create.success')
        });
        this.refreshView();
        this.displayTicketForm = false;
      },
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: err.error
      }),
    })
  }

  openEditTicketDialog(ticket: TicketModel) {
    this.ticketFormContainer.clear();
    const formComponent = this.ticketFormContainer.createComponent(
      TicketFormComponent
    );

    const instance = formComponent.instance;
    instance.ticket = ticket;
    instance.formSubmit.pipe(takeUntil(this.destroyed$)).subscribe((formValue: TicketFormValue) => {
      this.editTicket(ticket.id, formValue);
    })
    instance.cancel.pipe(takeUntil(this.destroyed$)).subscribe(() => this.displayTicketForm = false)

    this.displayTicketForm = true;
  }

  editTicket(ticketId: number, ticket: ModifyTicketRequest) {
    this.service.modify(ticketId, ticket).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          detail: this.translateService.instant('ticket.edit.success')
        });
        this.refreshView();
        this.displayTicketForm = false;
      },
      error: (err) => this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('general.error'),
        detail: err.error
      }),
    })
  }
}
