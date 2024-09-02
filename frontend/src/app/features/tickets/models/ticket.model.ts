import {Currency} from "../enums/currency.enum";
import {TicketStatus} from "../enums/ticket-status.enum";

export interface TicketModel {
  id: number;

  employee: string;
  company: string;
  employeePhoneNo: string;
  signature: string;
  offence: string;

  fineAmount: number;
  administrationFee: number;
  currency: Currency

  status: TicketStatus;

  offenceDate: string;
  dueDate: string;
}
