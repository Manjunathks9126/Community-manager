import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';

@Injectable()
export class UserInfoService {
    constructor(private httpClient: HttpClient) { }

    getUserFromSession(): Observable<any> {
        return this.httpClient.get("session/user")
        .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));

    }

    // To be moved to Custom Pipe
    changeDateFormat(date: string): string {
        let formattedDate = "";

        if (date) {
            let dateArray = date.split(" ");

            if (dateArray.length == 6) {
                dateArray[dateArray.length - 2] = dateArray[dateArray.length - 2].replace(":", "");
                formattedDate = dateArray.join(" ");
            } else {
                formattedDate = date;
            }
        }
        return formattedDate;

    }
}

