export interface PageResult<TType> {
  content: TType[];
  last: boolean;
  first: boolean;
  number: number;
  totalPages: number;
  totalElements: number;
  size: number;
  empty: boolean;
}
