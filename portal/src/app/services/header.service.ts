import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { ExceptionWrapper } from '../util/exception/exception-wrapper';

@Injectable()
export class HeaderService{

    constructor(private httpClient:HttpClient){}

    // Get Homepage Tiles
    public getHeaderFeatures(): Observable<any> {
        return this.httpClient.get("tiles").pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    
}