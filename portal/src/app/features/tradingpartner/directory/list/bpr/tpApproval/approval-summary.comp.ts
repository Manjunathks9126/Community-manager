import { Component, OnInit, Input } from "@angular/core";
import { Workflow } from "../../../createTPMSG/entity/workflow.entity";
import { TradingParnterService } from "../../../../../../services/trading-partner.service";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { NotificationHandler } from "../../../../../../util/exception/notfication.handler";
import { Router } from "@angular/router";
import { TranslateService } from '@ngx-translate/core';
import { PERMISSSION } from '../../../createTPMSG/entity/permission.constants';

@Component({
    templateUrl: './approval-summary.html'
})

export class ApprovalSummaryComp implements OnInit {

    approveBtnFlag: boolean = false;
    approveCheckBoxFlag: boolean = false;
    approveFlag: boolean = false;
    isFilterApplied: boolean = false;
    scrollConfig = PSscrollUtils.scrollY();
    workFlow: Workflow = new Workflow();
    permissions:any;
    currentLanguage:string="en";
    constructor(private tpService: TradingParnterService, private notificationHandler: NotificationHandler,
        private router: Router, private translate: TranslateService) { }

    ngOnInit(): void {
        this.permissions=PERMISSSION;
        this.workFlow = this.tpService.getWorkFlow();
        if (this.workFlow.provisioningRequestData.tpRequestType == "APPROVED") {
            this.approveCheckBoxFlag = true;
        }
        this.currentLanguage=this.translate.currentLang;
    }
    approve() {
        let bprid = this.tpService.getBprId();
        this.tpService.approveTp(this.workFlow, bprid).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.showmessage();
                this.router.navigate(['tpdir']);
            }
            else {
                this.showvalidationmessage();
            }
        })
    }
    enableApproveBtn() {
        if (this.workFlow.provisioningRequestData.tpRequestType == "APPROVED") {
            this.approveBtnFlag = this.approveFlag;
        }
    }

    showmessage() {
        let message = {
            "title": this.translate.instant("common.success"),
            "severity": "success",
            "userMessage": this.translate.instant("tpmsg.messages.success")
        }
        this.notificationHandler.notify(message);
    }
    showvalidationmessage() {
        let message = {
            "title": this.translate.instant("tpmsg.messages.validationError"),
            "severity": "error",
            "userMessage": this.translate.instant("tpmsg.messages.tprExists")
        }
    this.notificationHandler.notify(message);
    }
}