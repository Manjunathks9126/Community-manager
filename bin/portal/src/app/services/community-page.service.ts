
import { Injectable } from "@angular/core";
import { Subject ,  Observable } from "rxjs";
import { TileContentDetail, TileContentElement, TileContentMetaData, DisplayText } from "../features/landingPage/tile/entity/TileContentDetail.entity";
import { FilterEntity } from "../features/tradingpartner/filter-entity";
import { HttpClient } from "@angular/common/http";
import { TradingParnterService } from "./trading-partner.service";
import { NotificationHandler } from "../util/exception/notfication.handler";


@Injectable()
export class CommunityPageService {
    tpListDataSubject = new Subject<any>();
    bprCountSubject = new Subject<any>();
    toolTipSubject = new Subject<any>();
    tileContentDetails: TileContentDetail;
    selectedFilters: FilterEntity = new FilterEntity();


    constructor(private http: HttpClient, private tpService: TradingParnterService,
        private notificationHandler: NotificationHandler) {
    }

    updateBprCountToTiles(data) {
        this.bprCountSubject.next(data);
    }

    listCommunityPageTiles(): Observable<any> {
        return this.http.get("tiles/communitypage");
    }

    listTradingPartners(after: number) {
        this.selectedFilters.limit = 20;
        this.selectedFilters.after = after;
        this.selectedFilters.countOnly = false;
        this.tpService.listBusinessPartners(this.selectedFilters).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.formatTpListJson(data['responseDetails']['responseEntity'].itemList);
                } else {
                    return [];
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'info', title: error.userMessage, details: error.restErrorDetails });
            });
    }

    private formatTpListJson(tpList: any[]) {
        this.tileContentDetails = new TileContentDetail();
        this.tileContentDetails.elements = [];
        for (let i = 0; i < tpList.length; i++) {
            let tileContent = new TileContentElement()
            let bprMeta = new TileContentMetaData();
            bprMeta.buid = tpList[i].partnerCompanyId;
            bprMeta.bprId = tpList[i].bprId;
            tileContent.lines = [];
            let displayContentLeft = new DisplayText();
            displayContentLeft.headerText = tpList[i].partnerCompanyDisplayName;
            displayContentLeft.subtext = tpList[i].partnerCompanyName;

            let displayContentRight = new DisplayText();
            displayContentRight.headerText = tpList[i].bprStatus;
            displayContentRight.subtext = tpList[i].partnerShipActivationDate;
            // Line 1
            tileContent.lines[0] = [];
            tileContent.lines[0][0] = displayContentLeft;
            tileContent.lines[0][1] = displayContentRight;
            tileContent.contentMeta = bprMeta;

            this.tileContentDetails.elements[i] = tileContent;
        }
        this.tpListDataSubject.next("ok");
    }

    showTooltip(eventData) {
        this.toolTipSubject.next(eventData);
    }


    getDataBPRChart(statusArray) {
        let countArray_final = [];
        for (let i = 0; i < statusArray.length; i++) {
            countArray_final[i] = statusArray[i];
        }
        countArray_final[countArray_final.length - 1] = 0;
        let graphData = {
            labels: [
                statusArray[0] + ' - Approved Partners',
                statusArray[1] + ' - Testing Partners',
                statusArray[2] + ' - Requested Partners',
                 statusArray[3] + ' - Rejected Partners',
                 statusArray[4] + ' - Error Partners',
                statusArray[5] + ' - Total Partners'],

            datasets: [
                {
                    data: countArray_final
                }]
        };
        return graphData;
    }
}