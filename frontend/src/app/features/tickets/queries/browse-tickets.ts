import {BrowseQuery} from "../../../shared/api/browse-query";
import {Currency} from "../enums/currency.enum";
import {TicketStatus} from "../enums/ticket-status.enum";

export class BrowseTickets extends BrowseQuery {
  name: string;
  surname: string;
  phoneNo: string;
  signature: string;
  status: TicketStatus;
  currency: Currency
  offenceDate: string;
  dueDate: string;
  companyId: number;
  offenceId: number;
}
