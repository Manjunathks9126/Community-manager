import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { TPRSearchQuery } from "../features/tradingpartner/tpr.search.entity";

@Injectable()
export class TradingParnterDetailsService {
    constructor(private http: HttpClient) { }

    searchPartnerEDIAddresses(searchQuery: TPRSearchQuery): Observable<any> {
        return this.http.post("partner/ediaddresses", searchQuery)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getPartnerEDIAddressesComms(ediaddress: string): Observable<any> {
        return this.http.get("ediaddresses/comms?id=" + ediaddress)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    searchPartnerMergedEDIAddresses(searchQuery: TPRSearchQuery): Observable<any> {
        return this.http.post("partner/merge/ediaddresses", searchQuery)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getAuditLogs(buId: string, countOnly, limit, startFrom): Observable<any> {
        return this.http.get("auditlog?buId=" + buId + "&countOnly=" + countOnly + "&limit=" + limit + "&startingFrom=" + startFrom)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getOnboardingHistory(hideLoader: boolean, companyName: string, companyCity: string, companyCountry: string, companyId: string): Observable<any> {
        let params = new HttpParams().set('hideLoader', hideLoader ? 'true' : 'false');
        let query = {
            "companyName": companyName,
            "companyCity": companyCity,
            "companyCountry": companyCountry,
            "companyId": companyId
        }
        return this.http.post("onboarding/history", query, { params: params })
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getEdiAddressList(companyId): Observable<any> {
        return this.http.get("ediList/" + companyId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
}

