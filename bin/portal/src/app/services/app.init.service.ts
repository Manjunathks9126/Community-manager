import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class AppInitService {

    constructor(private http: HttpClient) { }

    load() {
        return this.http.get('permissions').toPromise();
    }
}