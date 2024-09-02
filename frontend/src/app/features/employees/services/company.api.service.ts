import {inject, Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {EmployeeModel} from "../models/employee.model";

@Injectable({
  providedIn: 'root'
})
export class CompanyApiService {
  private URL = environment.apiUrl + "/companies";

  private http = inject(HttpClient);

  getAll() {
    return this.http.get<EmployeeModel[]>(this.URL);
  }
}
