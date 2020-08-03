import { Injectable } from "@angular/core";
import { Subject ,  Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class TPGraphService {
    public graphSubject = new Subject<any>();
    public graphUrl = "";
    public graphFlag = false;


    constructor(private httpClient: HttpClient) {

    }


    openGraph(data) {
        this.graphSubject.next(data);
    }

    setGraphUrl(url: any) {
        this.graphUrl = url;
    }

    getTpGraphSubscription(): Observable<any>{
        return this.httpClient.get("tpGraphSubscription");
    }
}