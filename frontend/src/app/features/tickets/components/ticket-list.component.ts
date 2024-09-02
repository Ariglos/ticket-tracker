import { Component } from '@angular/core';
import {Button} from "primeng/button";
import {TableModule} from "primeng/table";

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [
    Button,
    TableModule
  ],
  template: `
    <p-table>
      <ng-template pTemplate="header"></ng-template>
      <ng-template pTemplate="body"></ng-template>
    </p-table>

  `,
  styles: ``
})
export class TicketListComponent {

}
