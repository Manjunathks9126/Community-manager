import { Component, OnDestroy } from "@angular/core";
import { TPMSGService } from "../../../../services/tpmsg.service";


@Component({
    templateUrl: "./tpMsgBase.component.html"
})
export class TpMsgBaseComponent implements OnDestroy {
    ngOnDestroy(): void {
        this.tpmgsService.clearCache();
    }
    homePage: {};
    vanProvidersList: any;
    ediInfo: any = {};
    hideStepper: boolean = false;
    constructor(private tpmgsService: TPMSGService) {
        tpmgsService.hideStepperSUB.subscribe(data => {
            this.hideStepper = data;
        })
    }
}