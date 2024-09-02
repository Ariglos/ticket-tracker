import { Component } from '@angular/core';
import {TicketListComponent} from "../components/ticket-list.component";

@Component({
  selector: 'app-tickets.page',
  standalone: true,
  imports: [
    TicketListComponent
  ],
  template: `
    <p>
      tickets.page works!
    </p>
    <app-ticket-list/>
  `,
  styles: ``
})
export class TicketsPage {

}
