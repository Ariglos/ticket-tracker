import {EmployeeStatus} from "../enums/employee-status.enum";

export interface EmployeeModel {
  id: number;
  name: string;
  surname: string;
  phoneNo: string;
  email: string;
  status: EmployeeStatus;
  companyName: string;
}
