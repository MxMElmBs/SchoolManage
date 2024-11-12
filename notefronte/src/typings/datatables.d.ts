import 'datatables.net';

declare global {
  interface JQuery {
    isDataTable(): boolean;
  }
}
