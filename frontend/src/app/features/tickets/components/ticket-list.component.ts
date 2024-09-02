import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {Button} from "primeng/button";
import {TableModule} from "primeng/table";
import {PageResult} from "../../../shared/api/page-result";
import {TicketModel} from "../models/ticket.model";
import {TranslateModule} from "@ngx-translate/core";
import {JsonPipe, LowerCasePipe} from "@angular/common";
import {BrowseTickets} from "../queries/browse-tickets";
import {debounceTime, map, Subject, takeUntil} from "rxjs";
import {DestroyedDirective} from "../../../utils/directives/destroyed.directive";
import {InputTextModule} from "primeng/inputtext";
import {CompanyModel} from "../../employees/models/company.model";
import {EmployeeModel} from "../../employees/models/employee.model";
import {DropdownModule} from "primeng/dropdown";
import {CalendarModule} from "primeng/calendar";
import {OffenceModel} from "../models/offence.model";
import {InputNumberModule} from "primeng/inputnumber";
import {Currency, CURRENCY} from "../enums/currency.enum";
import {TICKET_STATUS, TicketStatus} from "../enums/ticket-status.enum";
import {TooltipModule} from "primeng/tooltip";

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  hostDirectives: [DestroyedDirective],
  imports: [
    Button,
    TableModule,
    TranslateModule,
    JsonPipe,
    LowerCasePipe,
    InputTextModule,
    DropdownModule,
    CalendarModule,
    InputNumberModule,
    TooltipModule
  ],
  template: `
    @if (_data) {
      <p-table [value]="_data.content" [paginator]="true" [first]="first" [rows]="rows" [totalRecords]="totalRecords"
               [lazy]="true" [resizableColumns]="true"
               [autoLayout]="true" styleClass="p-datatable-striped" [rowsPerPageOptions]="[10, 15, 25]"
               (onPage)="pageChange($event)" (onSort)="onSort($event)">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="employee.name">{{ 'ticket.full-name' | translate }}
              <p-sortIcon field="employee.name"/>
            </th>
            <th pSortableColumn="employee.company.name">{{ 'ticket.company' | translate }}
              <p-sortIcon field="employee.company.name"/>
            </th>
            <th pSortableColumn="employee.phoneNo">{{ 'ticket.phone-no' | translate }}
              <p-sortIcon field="employee.phoneNo"/>
            </th>
            <th pSortableColumn="signature">{{ 'ticket.signature' | translate }}
              <p-sortIcon field="signature"/>
            </th>
            <th pSortableColumn="offenceDate">{{ 'ticket.offence-date' | translate }}
              <p-sortIcon field="offenceDate"/>
            </th>
            <th pSortableColumn="offence.description">{{ 'ticket.offence' | translate }}
              <p-sortIcon field="offence.description"/>
            </th>
            <th pSortableColumn="fineAmount">{{ 'ticket.fine-amount' | translate }}
              <p-sortIcon field="fineAmount"/>
            </th>
            <th pSortableColumn="administrationFee">{{ 'ticket.administration-fee' | translate }}
              <p-sortIcon field="administrationFee"/>
            </th>
            <th pSortableColumn="dueDate">{{ 'ticket.due-date' | translate }}
              <p-sortIcon field="dueDate"/>
            </th>
            <th pSortableColumn="status">{{ 'ticket.status' | translate }}
              <p-sortIcon field="status"/>
            </th>
            <th>{{ 'ticket.actions' | translate }}</th>
          </tr>
          <tr>
            <th>
              <input
                class="w-full"
                pInputText
                placeholder="{{'ticket.name' | translate}}"
                (input)="onFilter('name', $any($event.target).value)"
              />
              <input
                class="w-full"
                pInputText
                placeholder="{{'ticket.surname' | translate}}"
                (input)="onFilter('surname', $any($event.target).value)"
              />
            </th>
            <th>
              <p-dropdown
                appendTo="body"
                [options]="companyOptions"
                placeholder="Wybierz spółkę"
                [showClear]="true"
                (onChange)="onFilter('companyId', $event.value)"
              />
            </th>
            <th>
              <input
                class="w-full"
                pInputText
                placeholder="{{'ticket.phone-no' | translate}}"
                (input)="onFilter('phoneNo', $any($event.target).value)"
              />
            </th>
            <th>
              <input
                class="w-full"
                pInputText
                placeholder="{{'ticket.signature' | translate}}"
                (input)="onFilter('signature', $any($event.target).value)"
              />
            </th>
            <th>
              <p-calendar
                appendTo="body"
                [showIcon]="true"
                [showClear]="true"
                placeholder="{{'choose.date' | translate}}"
                (onSelect)="onFilter('offenceDate', $event)"
                (onClear)="onFilter('offenceDate', null)"
              />
            </th>
            <th>
              <p-dropdown
                appendTo="body"
                [options]="offenceOptions"
                placeholder="Wybierz przewinienie"
                [showClear]="true"
                (onChange)="onFilter('offenceId', $event.value)"
              />
            </th>
            <th colspan="2">
              <p-dropdown
                appendTo="body"
                [options]="currencyOptions"
                placeholder="Wybierz walutę"
                [showClear]="true"
                (onChange)="onFilter('currency', $event.value)"
              />
            </th>
            <th>
              <p-calendar
                appendTo="body"
                [showIcon]="true"
                [showClear]="true"
                placeholder="{{'choose.date' | translate}}"
                (onSelect)="onFilter('dueDate', $event)"
                (onClear)="onFilter('dueDate', null)"
              />
            </th>
            <th>
              <p-dropdown
                appendTo="body"
                [options]="statusOptions"
                placeholder="Wybierz status"
                [showClear]="true"
                (onChange)="onFilter('status', $event.value)"
              />
            </th>
            <th></th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-ticket>
          <tr>
            <td>{{ ticket.employee }}</td>
            <td>{{ ticket.company }}</td>
            <td>{{ ticket.employeePhoneNo }}</td>
            <td>{{ ticket.signature }}</td>
            <td>{{ ticket.offenceDate }}</td>
            <td>{{ ticket.offence }}</td>
            <td>{{ ticket.fineAmount }} {{ ticket.currency }}</td>
            <td>{{ ticket.administrationFee }} {{ ticket.currency }}</td>
            <td>{{ ticket.dueDate }}</td>
            <td [class]="'status ' + ticket.status | lowercase">{{ ('ticket.status.' + ticket.status) | translate }}
            </td>
            <td>
              <div>
                <div>
                  <p-button
                    size="small"
                    icon="pi pi-search"
                    pTooltip="{{'action.display-attachment' | translate}}"
                    (onClick)="displayAttachment.emit(ticket)"
                  />
                  <p-button
                    size="small"
                    icon="pi pi-sign-in"
                    severity="secondary"
                    pTooltip="{{'action.go-to-details' | translate}}"
                    (onClick)="navigate.emit(ticket)"
                  />
                  <p-button
                    size="small"
                    icon="pi pi-times"
                    severity="danger"
                    pTooltip="{{'action.delete-attachment' | translate}}"
                    (onClick)="deleteAttachment.emit(ticket)"
                  />
                </div>
                <div>
                  <p-button
                    size="small"
                    icon="pi pi-pencil"
                    pTooltip="{{'action.edit' | translate}}"
                    (onClick)="edit.emit(ticket)"
                  />
                  <p-button
                    [disabled]="ticket.status === 'PAID'"
                    size="small"
                    icon="pi pi-check"
                    severity="success"
                    pTooltip="{{'action.confirm' | translate}}"
                    (onClick)="confirm.emit(ticket)"
                  />
                  <p-button
                    [disabled]="ticket.status === 'PAID'"
                    size="small"
                    icon="pi pi-trash"
                    severity="danger"
                    pTooltip="{{'action.delete' | translate}}"
                    (onClick)="delete.emit(ticket)"
                  />
                </div>
              </div>
            </td>
          </tr>
        </ng-template>
      </p-table>
    }
  `,
  styles: `

    th, td {
      font-size: 16px;
    }

    td.status {
      font-weight: bold;
    }

    td.pending {
      color: red;
    }

    td.paid {
      color: green;
    }

  `
})
export class TicketListComponent implements OnInit {

  _data: PageResult<TicketModel>
  @Input({required: true}) set data(data: PageResult<TicketModel>) {
    this._data = data;
    this.totalRecords = data?.totalElements;
    this.rows = data?.size;
    this.first = (data?.number * data?.size);
  }

  @Input({required: true}) query: BrowseTickets;

  @Input({required: true}) set companies(companies: CompanyModel[]) {
    this.companyOptions = companies?.map(company => ({
      label: company.name,
      value: company.id
    }));
  }

  @Input({required: true}) set employees(employees: EmployeeModel[]) {
    this.employeeOptions = employees?.map(employee => ({
      label: employee.name + ' ' + employee.surname,
      value: employee.id
    }));
  }

  @Input({required: true}) set offences(offences: OffenceModel[]) {
    this.offenceOptions = offences?.map(offence => ({
      label: offence.description,
      value: offence.id
    }));
  }

  @Output() reloadData = new EventEmitter<BrowseTickets>;
  @Output() edit = new EventEmitter<TicketModel>;
  @Output() delete = new EventEmitter<TicketModel>;
  @Output() confirm = new EventEmitter<TicketModel>;
  @Output() deleteAttachment = new EventEmitter<TicketModel>;
  @Output() displayAttachment = new EventEmitter<TicketModel>;
  @Output() navigate = new EventEmitter<TicketModel>;

  private destroyed$ = inject(DestroyedDirective).destroyed$;
  filterSubject = new Subject<{ fieldName: string; value: any }>();

  companyOptions: { label: string, value: number }[];
  employeeOptions: { label: string, value: number }[];
  offenceOptions: { label: string, value: number }[];
  currencyOptions: { label: string, value: Currency }[];
  statusOptions: { label: string, value: TicketStatus }[];

  first = 0;
  rows = 10;
  totalRecords = 10;

  ngOnInit(): void {
    this.filterSubject
      .pipe(
        debounceTime(250),
        takeUntil(this.destroyed$),
        map((filterData) => {
          if (filterData.value == null) {
            filterData.value = "";
          }
          return filterData;
        }),
      )
      .subscribe((filterData) => this.performFiltering(filterData));

    this.currencyOptions = [
      {
        label: "PLN",
        value: CURRENCY.PLN
      },
      {
        label: "EUR",
        value: CURRENCY.EUR
      }
    ];

    this.statusOptions = [
      {
        label: "Do zapłaty",
        value: TICKET_STATUS.PENDING
      },
      {
        label: "Opłacony",
        value: TICKET_STATUS.PAID
      }
    ];
  }

  pageChange(event: any) {
    this.query.page = (event.first / event.rows);
    this.query.limit = event.rows;
    this.reloadData.emit(this.query);
  }

  onSort(event: any) {
    this.query.sortBy = event.field;
    this.query.sortDirection = event.order === 1 ? "DESC" : "ASC";
    this.reloadData.emit(this.query);
  }

  onFilter(fieldName: string, value: any) {
    if (value instanceof Date) {
      const year = value.getFullYear();
      const month = String(value.getMonth() + 1).padStart(2, "0");
      const day = String(value.getDate()).padStart(2, "0");
      value = `${year}-${month}-${day}`;
    }
    this.filterSubject.next({fieldName, value});
  }

  private performFiltering(filterData: { fieldName: string; value: any }) {
    this.query[filterData.fieldName] = filterData.value;
    this.reloadData.emit(this.query);
  }
}
