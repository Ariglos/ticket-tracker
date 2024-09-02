export const TICKET_STATUS = {
  PENDING: "PENDING",
  PAID: "PAID"
} as const;

export type TicketStatus = keyof typeof TICKET_STATUS;
