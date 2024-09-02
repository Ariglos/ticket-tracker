import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {TicketModel} from "../models/ticket.model";
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {EmployeeModel} from "../../employees/models/employee.model";
import {CURRENCY, Currency} from "../enums/currency.enum";
import {Attachment} from "../models/attachment";
import {FormValue} from "../../../utils/form-value.type";
import {markFromGroupTouched} from "../../../utils/mark-form-group-touched";
import {Button} from "primeng/button";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {InputTextModule} from "primeng/inputtext";
import {FloatLabelModule} from "primeng/floatlabel";
import {PaginatorModule} from "primeng/paginator";
import {JsonPipe} from "@angular/common";
import {CalendarModule} from "primeng/calendar";
import {EmployeeApiService} from "../../employees/services/employee.api.service";
import {OffenceApiService} from "../services/offence-api.service";
import {DestroyedDirective} from "../../../utils/directives/destroyed.directive";
import {takeUntil, tap} from "rxjs";

type TicketForm = FormGroup<{
  signature: FormControl<string>,
  fineAmount: FormControl<number>,
  currency: FormControl<Currency>,
  offenceDate: FormControl<string>,
  dueDate: FormControl<string>,
  customOffence: FormControl<string>,
  offenceId: FormControl<number>,
  employeeId: FormControl<number>,
  attachment: FormControl<Attachment>
}>

export type TicketFormValue = FormValue<TicketForm>;

@Component({
  selector: 'app-ticket-form',
  standalone: true,
  hostDirectives: [DestroyedDirective],
  imports: [
    ReactiveFormsModule,
    Button,
    TranslateModule,
    InputTextModule,
    FloatLabelModule,
    PaginatorModule,
    JsonPipe,
    CalendarModule
  ],
  template: `
    <form class="form-container" [formGroup]="form" (ngSubmit)="submitForm()">
      <div class="field-container" [style.width]="'100%'">
        <label for="signature">{{ 'ticket.signature' | translate }}*</label>
        <input id="signature" pInputText formControlName="signature"/>
      </div>
      <div class="field-container" [style.width]="'50%'">
        <label for="fineAmount">{{ 'ticket.fine-amount' | translate }}*</label>
        <p-inputNumber id="fineAmount" formControlName="fineAmount"/>
      </div>
      <div class="field-container" [style.width]="'50%'">
        <label for="currency">{{ 'ticket.currency' | translate }}*</label>
        <p-dropdown class="p-fluid" id="currency" appendTo="body" formControlName="currency" [options]="currencyOptions"/>
      </div>
      <div class="field-container" [style.width]="'50%'">
        <label for="offenceDate">{{ 'ticket.offence-date' | translate }}*</label>
        <p-calendar id="offenceDate" appendTo="body" formControlName="offenceDate" [showIcon]="true" dataType="string"
                    dateFormat="yy-mm-dd"/>
      </div>
      <div class="field-container" [style.width]="'50%'">
        <label for="dueDate">{{ 'ticket.due-date' | translate }}*</label>
        <p-calendar id="dueDate" appendTo="body" formControlName="dueDate" [showIcon]="true" dataType="string" dateFormat="yy-mm-dd"
                    [minDate]="today"/>
      </div>
      <div class="field-container" [style.width]="'50%'">
        <label for="offenceId">{{ 'ticket.offence' | translate }}*</label>
        <p-dropdown class="p-fluid" id="offenceId" appendTo="body" formControlName="offenceId" [options]="offenceOptions"/>
      </div>
      @if (form.value.offenceId === 4) {
        <div class="field-container" [style.width]="'50%'">
          <label for="customOffence">{{ 'ticket.custom-offence' | translate }}</label>
          <input pInputText id="customOffence" formControlName="customOffence"/>
        </div>
      }
      <div class="field-container" [style.width]="'100%'">
        <label for="employeeId">{{ 'ticket.employee' | translate }}*</label>
        <p-dropdown class="p-fluid" id="employeeId" appendTo="body" formControlName="employeeId" [options]="employeeOptions" [filter]="true"/>
      </div>
      @if (!isEdit) {
        <div class="field-container" [style.width]="'100%'">
          <input
            #fileUpload
            class="hidden"
            type="file"
            (change)="onFileSelected($event)"
          />
          <p-button
            class="p-fluid"
            label="{{'ticket.attachment' | translate}}*"
            (onClick)="fileUpload.click()"
          />
        </div>
      }
      @if (form.invalid && formSubmitted) {
        <div class="invalid-form-notification">
          {{'ticket.form.invalid' | translate}}
        </div>
      }
      <div class="btn-container" [style.width]="'100%'">
        <p-button
          [style.width]="'100%'"
          class="p-fluid"
          type="submit"
          [label]="btnLabel"
          severity="success"
        />
        <p-button
          [style.width]="'100%'"
          class="p-fluid"
          severity="secondary"
          label="{{'general.cancel' | translate}}"
          (onClick)="cancel.emit()"
        />
      </div>
    </form>
  `,
  styles: `
    .form-container {
      display: flex;
      flex-wrap: wrap;
    }

    .field-container {
      padding: 10px 6px;
      display: flex;
      flex-direction: column;
    }

    .hidden {
      display: none;
    }

    .btn-container {
      display: flex;
      gap: 8px;
    }

    .invalid-form-notification {
      margin: 12px 0;
      font-size: 1.1rem;
      color: red;
    }
  `
})
export class TicketFormComponent implements OnInit {
  _ticket: TicketModel;
  @Input() set ticket(ticket: TicketModel) {
    this.form.reset();
    this._ticket = ticket;
    this.isEdit = !!ticket;
    if (ticket) {
      this.form.patchValue(ticket);
      if (ticket.offenceId === 4) {
        this.form.controls.customOffence.patchValue(ticket.offence);
      }
    }
  }

  @Output() formSubmit = new EventEmitter<TicketFormValue>;
  @Output() cancel = new EventEmitter<void>;

  private destroyed$ = inject(DestroyedDirective).destroyed$;

  private employeeService = inject(EmployeeApiService);
  private offenceService = inject(OffenceApiService);

  employeeOptions: { label: string, value: number }[];
  offenceOptions: { label: string, value: number }[];
  currencyOptions: { label: string, value: Currency }[];

  today = new Date();
  btnLabel: string;
  formSubmitted: boolean;
  isEdit: boolean;

  private fb = inject(NonNullableFormBuilder);
  private translateService = inject(TranslateService);

  form: TicketForm = this.fb.group({
    signature: this.fb.control<string>("", Validators.required),
    fineAmount: this.fb.control<number>(null, Validators.required),
    currency: this.fb.control<Currency>("PLN", Validators.required),
    offenceDate: this.fb.control<string>(null, Validators.required),
    dueDate: this.fb.control<string>(null, Validators.required),
    customOffence: this.fb.control<string>(null),
    offenceId: this.fb.control<number>(null, Validators.required),
    employeeId: this.fb.control<number>(null, Validators.required),
    attachment: this.fb.control<Attachment>(null, Validators.required),
  })

  ngOnInit(): void {
    if (this.isEdit) {
      this.btnLabel = this.translateService.instant('ticket.edit')
      this.form.removeControl('attachment');
    } else {
      this.btnLabel = this.translateService.instant('ticket.add');
    }

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

    this.offenceService.getAll().subscribe(offences => {
      this.offenceOptions = offences?.map(offence => ({
        label: offence.description,
        value: offence.id
      }));
    })

    this.employeeService.getAll().subscribe(employees => {
      this.employeeOptions = employees?.map(employee => ({
        label: employee.name + ' ' + employee.surname,
        value: employee.id
      }));
    })

    this.form.controls.offenceId.valueChanges.pipe(
      tap((value) => {
        if (value !== 4) {
          this.form.controls.customOffence.setValue('');
        }
      }),
      takeUntil(this.destroyed$)
    ).subscribe();
  }

  submitForm() {
    this.formSubmitted = true;
    if (!this.form.valid) {
      markFromGroupTouched(this.form);
      return;
    }
    this.formSubmit.emit(this.form.getRawValue());
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file: File = input.files[0];
    const reader = new FileReader();
    reader.onloadend = () => {
      const fileName = file?.name;
      const base64File = reader.result as string;
      const pureBase64File = base64File.slice(base64File.indexOf(",") + 1);
      this.form.controls.attachment.setValue({ fileName: fileName, base64File: pureBase64File });
    };

    if (file) {
      reader.readAsDataURL(file);
    }
  }

}
