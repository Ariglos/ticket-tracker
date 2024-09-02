export abstract class BrowseQuery {
  limit = 10;
  page = 0;
  sortBy: string;
  sortDirection: "ASC" | "DESC";
}
