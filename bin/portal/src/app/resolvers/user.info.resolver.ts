import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';

import { UserInfoService } from "../services/user-info.service";

@Injectable()
export class UserInfoResolver implements Resolve<string> {

    defaultLocale: string ='en';

    constructor(private userInfoService: UserInfoService) { }

    resolve() {
        return this.userInfoService.getUserFromSession().toPromise().
            then((result: any) => {
                if (result.success && result.responseEntity.userLocale) {
                    return result.responseEntity.userLocale;
                } else {
                    return this.defaultLocale;
                }
            }).catch(() => {
                    return this.defaultLocale;
                }

            );
    }
}