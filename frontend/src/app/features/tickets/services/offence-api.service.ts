import {inject, Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {OffenceModel} from "../models/offence.model";

@Injectable({
  providedIn: 'root'
})
export class OffenceApiService {
  private URL = environment.apiUrl + "/offences";

  private http = inject(HttpClient);

  getAll() {
    return this.http.get<OffenceModel[]>(this.URL);
  }
}
