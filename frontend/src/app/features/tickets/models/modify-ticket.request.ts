import {Currency} from "../enums/currency.enum";
import {Attachment} from "./attachment";

export interface ModifyTicketRequest {
  signature: string;

  fineAmount: number;
  currency: Currency;

  offenceDate: string;
  dueDate: string;

  customOffence: string;
  offenceId: number;

  employeeId: number;
}
