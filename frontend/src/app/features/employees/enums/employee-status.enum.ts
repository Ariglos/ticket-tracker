export const EMPLOYEE_STATUS = {
  EMPLOYED: "EMPLOYED",
  DISMISSED: "DISMISSED"
} as const;

export type EmployeeStatus = keyof typeof EMPLOYEE_STATUS;
