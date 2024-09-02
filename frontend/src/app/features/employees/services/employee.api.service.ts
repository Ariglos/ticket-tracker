import {inject, Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {CompanyModel} from "../models/company.model";

@Injectable({
  providedIn: 'root'
})
export class EmployeeApiService {
  private URL = environment.apiUrl + "/companies";

  private http = inject(HttpClient);

  getAll() {
    return this.http.get<CompanyModel[]>(this.URL);
  }
}
