import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { TPDirectoryListFilterOptions } from "../features/tradingpartner/directory/list/bpr/tplist.filter.options";
import { TradingParnterService } from "../services/trading-partner.service";
import { Observable } from "rxjs";



@Injectable()

export class TPFilterOptionsResolver implements Resolve<TPDirectoryListFilterOptions> {

  constructor(private tpService: TradingParnterService) { }

  resolve(route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
    return this.tpService.tpDirectoryFilterOptions().toPromise().
      then((values: TPDirectoryListFilterOptions) => {
        return values;
      }).catch((reason: any) => {
        let errorResponse = {
          responseDetails: {
            responseEntity: new TPDirectoryListFilterOptions()
          }
        };

        return errorResponse;
      }

      );
  }
}