import {inject, Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {BrowseTickets} from "../queries/browse-tickets";
import {PageResult} from "../../../shared/api/page-result";
import {TicketModel} from "../models/ticket.model";
import {CreateTicketRequest} from "../models/create-ticket.request";
import {ModifyTicketRequest} from "../models/modify-ticket.request";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TicketsApiService {
  private URL = environment.apiUrl + "/tickets";

  private http = inject(HttpClient);

  browse(query: BrowseTickets) {
    return this.http.get<PageResult<TicketModel>>(this.URL, {
      params: {...query}
    });
  }

  getById(ticketId: number) {
    return this.http.get<TicketModel>(`${this.URL}/${ticketId}`);
  }

  create(ticket: CreateTicketRequest) {
    return this.http.post(`${this.URL}`, ticket);
  }

  modify(ticketId: number, ticket: ModifyTicketRequest) {
    return this.http.put(`${this.URL}/${ticketId}`, ticket);
  }

  deleteTicket(ticketId: number) {
    return this.http.delete(`${this.URL}/${ticketId}`);
  }

  confirm(ticketId: number) {
    return this.http.put(`${this.URL}/${ticketId}/confirm`, null);
  }

  getAttachment(ticketId: number) {
    return this.http.get(`${this.URL}/${ticketId}/attachments`, {
      responseType: 'blob'
    }).pipe(
      tap((pdfBlob: Blob) => {
        if (pdfBlob != null) {
          const fileURL = URL.createObjectURL(pdfBlob);
          window.open(fileURL);
        }
      })
    );
  }

  deleteAttachments(ticketId: number) {
    return this.http.delete(`${this.URL}/${ticketId}/attachments`);
  }
}
