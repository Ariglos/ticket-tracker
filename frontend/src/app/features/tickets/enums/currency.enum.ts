export const CURRENCY = {
  PLN: "PLN",
  EUR: "EUR"
} as const;

export type Currency = keyof typeof CURRENCY;
