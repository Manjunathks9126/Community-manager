import { Component } from "@angular/core";
import { Workflow } from "../entity/workflow.entity";
import { TPMSGService } from "../../../../../services/tpmsg.service";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { TradingParnterService } from "../../../../../services/trading-partner.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { Router, ActivatedRoute } from "@angular/router";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { TPREQUESTTYPE } from '../entity/tptype.constants';
import { TranslateService } from '@ngx-translate/core';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';

@Component({
    templateUrl: "./summary.component.html"
})
export class SummaryComponent {

    isTGMSClient: boolean = false;
    workFlow: Workflow;
    isGDFlag: boolean = false;
    isFilterApplied: boolean = false;
    selectedEDI: any[] = [];
    billingInfo: any[] = [];
    scrollConfig = PSscrollUtils.scrollY();
    constructor(private notficationHandler: NotificationHandler,private dialogueboxService: DialogueboxService, private onbaordingService: OnboardingService,
        private route: ActivatedRoute, private tpmsgService: TPMSGService, private tpService: TradingParnterService,
        private router: Router, private translate: TranslateService) {

    }
    ngOnInit() {
        if (this.tpmsgService.isTGMSworkflow()) {
            this.isTGMSClient = this.tpmsgService.isTGMSworkflow();
            this.selectedEDI = this.tpmsgService.getSelectedEdi();
            if (this.tpmsgService.getCachedBillingInfo().billingOption == 'SPLIT') {
                this.billingInfo.push(this.translate.instant("tpmsg.legends.sendingPaidBy").replace("{0}", this.tpmsgService.getCachedBillingInfo().sendingPaidBy));
                this.billingInfo.push(this.translate.instant("tpmsg.legends.receivingPaidBy").replace("{0}", this.tpmsgService.getCachedBillingInfo().receivingPaidBy));
                this.billingInfo.push(this.translate.instant("tpmsg.legends.mailboxPaidBy").replace("{0}", this.tpmsgService.getCachedBillingInfo().receiverMailbxStoragePaidBy));
            } else {
                this.billingInfo.push(this.translate.instant("tpmsg.legends.allCharges").replace("{0}", this.tpmsgService.getCachedBillingInfo().allChargesPaidBy));
            }
        }
        this.isGDFlag = this.tpmsgService.isGDFlag;
        this.tpmsgService.activateRoute("SUMM");
        this.workFlow = this.tpmsgService.getCachedWorkFlow();
        this.tpmsgService.moveToStepSUB.subscribe(data => {
            if (data == "msgDetails") {
                if (!this.tpmsgService.isTGMSworkflow())
                    this.router.navigate(['/tpdir/tpmsgbase/msgDetails'], {skipLocationChange: true});
                else
                    this.router.navigate(['/tpdir/tgmsbase/billingSplit'], {skipLocationChange: true});
            }
        });
    }

    submit() {
        const cachedReqType = this.workFlow.provisioningRequestData.tpRequestType;
        const cachedBuid = this.workFlow.provisioningRequestData.registrationData.businessUnit.buId;
        const cachedCompanyVanMap = this.tpmsgService.getCompanyIdVanProviderMap();
        if (cachedCompanyVanMap['companyId'] && cachedCompanyVanMap['companyId'].length > 0 &&
            cachedCompanyVanMap['vanProvider'].includes(this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanProvider)) {
            this.workFlow.provisioningRequestData.registrationData.businessUnit.buId = cachedCompanyVanMap['companyId'];
            if (this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.new_ic_edi) {
                this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.new_edi_existing_interconnect_tp;
            } else if (this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_ic_edi) {
                this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.existing_ic_edi_exisisting_tp;
            } else if (this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_service_provider_edi) {
                this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.existing_sp_edi_exisisting_tp;
            }
        }

        // Below line for setting only code to the workFlow object
        let country = this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode;
        if (this.tpmsgService.isTGMSworkflow()) {
            this.tpmsgService.postTGMSworkFlow(this.onbaordingService.getInvIdForOnboarding(), JSON.parse(JSON.stringify(this.workFlow))).subscribe(data => {
                this.onbaordingService.setInvIdForOnboarding("");
                this.showmessage(data['responseDetails']['responseEntity'].targetResourceRefId);
                if (!this.isGDFlag)
                    this.router.navigate(['tpdir']);
                else
                    this.router.navigate(['globalDirectory'], { queryParams: { gd: 't' }, skipLocationChange: true });
            }, error => {
                this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = country;
                this.workFlow.provisioningRequestData.registrationData.businessUnit.buId = cachedBuid;
                this.workFlow.provisioningRequestData.tpRequestType = cachedReqType;
                this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        } else {
            this.PrepareCPSNodeData();
            this.tpmsgService.postWorkflow(this.onbaordingService.getInvIdForOnboarding(), JSON.parse(JSON.stringify(this.workFlow))).subscribe(data => {
                this.onbaordingService.setInvIdForOnboarding("");
                this.showmessage(data['responseDetails']['responseEntity'].targetResourceRefId);
                if (!this.isGDFlag)
                    this.router.navigate(['tpdir']);
                else
                    this.router.navigate(['globalDirectory'], { queryParams: { gd: 't' }, skipLocationChange: true });
            }, error => {
                this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = country;
                this.workFlow.provisioningRequestData.registrationData.businessUnit.buId = cachedBuid;
                this.workFlow.provisioningRequestData.tpRequestType = cachedReqType;
                this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        }

    }

    PrepareCPSNodeData() {
        this.workFlow.processingContext.invitationCode = this.tpService.selectedinvCodeSubject;
        this.workFlow.processingContext.action = "CREATE";
    }

    showmessage(data: string) {
        let message = {
            "title": this.translate.instant("common.workflowSuccess"),
            "severity": "success",
            "details": this.translate.instant("common.workflowId").replace("{0}", data)
        }
        this.notficationHandler.notify(message);
    }

    previous() {
        if (!this.tpmsgService.isTGMSworkflow()) {
            this.router.navigate(['/tpdir/tpmsgbase/msgDetails'],{skipLocationChange: true});
        } else {
            this.router.navigate(['/tpdir/tgmsbase/billingSplit'],{skipLocationChange: true});
        }
    }


    cancel(){
        this.dialogueboxService.confirm({
            dialogName: 'cancelSetup',
            accept: () => {
                let navigationExtras = {
                    queryParams: {gd: 't'},
                    skipLocationChange: true
                  }
                if(this.isGDFlag){
                    this.router.navigate(['/globalDirectory'],navigationExtras);
                }else{
                    this.router.navigate(['/tpdir']);
                }
            }
            , reject: () => {
            }
        });
    }
}
