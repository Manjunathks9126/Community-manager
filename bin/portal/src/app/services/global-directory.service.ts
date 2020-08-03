import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GlobalSearchRequest } from '../features/globalDirectory/globalSearchRequest.entity';

@Injectable()
export class GlobalDirectoryService {

  public cached_term = "";
  public cached_search_attribute = "";
  public cached_companies = [];
  public cached_numFound = 0;
  public cached_first = 0;
  public cached_SearchPanelFlag = false;
  public cached_globalFilterReq: GlobalSearchRequest = new GlobalSearchRequest();
  public refineOptions_cache = [];
  public selectedFilterType_cache = {};
  public FIELD_VALUES_cache = [];
  public selectedFilterReq_cache: GlobalSearchRequest = new GlobalSearchRequest();
  public selected_INNER_FilterReq_cache = {};
  public sel_refineOption = {};
  public cached_filterOptions: {};
  public cached_nestedFilterOption: {};
  public cached_activePage = 1;
  public cached_row = 30;
  public cached_start = 1;
  public cached_sortField = "";
  public cached_sortColumn: any;
  public cached_isGroupBy: boolean;
  public cached_selected_refineNodes: any;
  constructor(private http: HttpClient) { }

  getSuggestions(globalSearchRequest: GlobalSearchRequest, suggestion): Observable<any> {
    return this.http.post("globalsearch/suggestions/" + suggestion, globalSearchRequest)
      .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getLoggedInCompanyDetails(): Observable<any> {
    return this.http.get("session/company");
  }

  getGXScompnay():Observable<any>{
    return this.http.get("util/internalCompanyDetails")
        .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getLoggedInUserDetails():Observable<any>{
    return this.http.get("users/details")
        .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  getRefineOptions(): Observable<any> {
    return this.http.get("globalsearch/refineOptions").pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }

  sendMessageToAllBu(buList): Observable<any>{
    return this.http.post("notification/sendMessage",buList).pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
}