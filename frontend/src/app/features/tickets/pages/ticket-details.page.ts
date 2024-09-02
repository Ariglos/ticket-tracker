import {Component, inject, OnInit} from '@angular/core';
import {TicketsApiService} from "../services/tickets.api.service";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {TicketModel} from "../models/ticket.model";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-ticket-details.page',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    TranslateModule
  ],
  template: `
    @if (ticket$ | async; as ticket) {
      <div class="container">
        <div class="item">
          <label for="employee">{{'ticket.employee' | translate}}</label>
          <div id="employee">{{ticket.employee}}</div>
        </div>
        <div class="item">
          <label for="company">{{'ticket.company' | translate}}</label>
          <div id="company">{{ticket.company}}</div>
        </div>
        <div class="item">
          <label for="phoneNo">{{'ticket.phone-no' | translate}}</label>
          <div id="phoneNo">{{ticket.employeePhoneNo}}</div>
        </div>
        <div class="item">
          <label for="signature">{{'ticket.signature' | translate}}</label>
          <div id="signature">{{ticket.signature}}</div>
        </div>
        <div class="item">
          <label for="offence">{{'ticket.offence' | translate}}</label>
          <div id="offence">{{ticket.offence}}</div>
        </div>
        <div class="item">
          <label for="fineAmount">{{'ticket.fine-amount' | translate}}</label>
          <div id="fineAmount">{{ticket.fineAmount}} {{ticket.currency}}</div>
        </div>
        <div class="item">
          <label for="administrationFee">{{'ticket.administration-fee' | translate}}</label>
          <div id="administrationFee">{{ticket.administrationFee}} {{ticket.currency}}</div>
        </div>
        <div class="item">
          <label for="status">{{'ticket.status' | translate}}</label>
          <div id="status">{{('ticket.status.' + ticket.status) | translate}}</div>
        </div>
        <div class="item">
          <label for="offenceDate">{{'ticket.offence-date' | translate}}</label>
          <div id="offenceDate">{{ticket.offenceDate}}</div>
        </div>
        <div class="item">
          <label for="dueDate">{{'ticket.due-date' | translate}}</label>
          <div id="dueDate">{{ticket.dueDate}}</div>
        </div>
      </div>
    }
  `,
  styles: `
    .container {
      margin-top: 1rem;
      margin-left: 20rem;
      display: flex;
      flex-direction: column;
    }

  .item {
    margin: 8px 0;
    display: flex;
    font-size: 1.6rem;
  }

  label {
    min-width: 19rem;
  }
  label::after {
    content: ':';
  }
  `
})
export class TicketDetailsPage implements OnInit {

  private service: TicketsApiService = inject(TicketsApiService);
  private route = inject(ActivatedRoute);

  ticket$: Observable<TicketModel>;

  ticketId: number;

  ngOnInit(): void {
    this.ticketId = +this.route.snapshot.paramMap.get('id');

    this.ticket$ = this.service.getById(this.ticketId);
  }



}
