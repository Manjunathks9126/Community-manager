import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";

import { ExceptionWrapper } from '../util/exception/exception-wrapper';

@Injectable()
export class TPCreateMsgService {
    constructor(private http: HttpClient) { }


    getVanProvidersList(): Observable<any> {
        return this.http.get("vanProviders")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    verifyEdiInfo(ediAddress: any): Observable<any> {
        return this.http.post("ediInfo", ediAddress)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }


    retriveCompanyDetails(companyId: any): Observable<any> {
        return this.http.get("companies/" + companyId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getTprList(buId, tradingAddress): Observable<any> {
        return this.http.get("tprList?ownerBuId=" + buId + "&tradingAddress=" + tradingAddress)
        .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
}
}

