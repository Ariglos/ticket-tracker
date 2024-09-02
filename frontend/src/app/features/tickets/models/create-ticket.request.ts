import {Currency} from "../enums/currency.enum";
import {Attachment} from "./attachment";

export interface CreateTicketRequest {
  signature: string;

  fineAmount: number;
  currency: Currency;

  offenceDate: string;
  dueDate: string;

  customOffence: string;
  offenceId: string;

  employeeId: number;

  attachment: Attachment;
}
