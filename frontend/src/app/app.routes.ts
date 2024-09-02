import {Routes} from '@angular/router';

export const routes: Routes = [
  {path: "", redirectTo: "tickets", pathMatch: "full"},
  {path: "tickets", loadComponent: () => import("./features/tickets/pages/tickets.page").then((x) => x.TicketsPage)}
];
