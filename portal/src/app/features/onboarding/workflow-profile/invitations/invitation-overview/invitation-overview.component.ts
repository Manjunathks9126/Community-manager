import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";

import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";

import { InvitationConstant } from "../invitation.constant";
import { DatePipe } from "@angular/common";
import { TranslateService } from '@ngx-translate/core';


@Component({
    templateUrl: "./invitation-overview.component.html"
})
export class InvitationOverviewComponent implements OnInit {
    url: string = "";
    invitationMessage: string = "";
    invitation: any = {};
    companyName: string = "";
    registrationUrl: string = "";
    completeRegistrationUrl: string = "";
    invitations: {};
    invitationImage: any;
    invitationExtendedRequest: any[] = [];
    imageContent: any;
    imageType: string = "";
    invitationMap: any;
    senderEmail: string = "";
    ccReceipts: string = "";
    readOnly: boolean = false;
    listRecipients: any[] = [];
    ccList: any[] = [];
    ediOptions: any[] = [{ 'label': this.translate.instant("onboarding.invitationForm.ediOptions.SYSTEM_GENERATES"), 'value': 'SYSTEM_GENERATES' },
    { 'label': this.translate.instant("onboarding.invitationForm.ediOptions.MANUAL_ENTER"), 'value': 'MANUAL_ENTER' },
    { 'label': this.translate.instant("onboarding.invitationForm.ediOptions.MANUAL_ENTER_FROM_PRELOAD"), 'value': 'MANUAL_ENTER_FROM_PRELOAD' },
    { 'label': this.translate.instant("onboarding.invitationForm.ediOptions.SYSTEM_GENERATES_FROM_PRELOAD"), 'value': 'SYSTEM_GENERATES_FROM_PRELOAD' },
    { 'label': this.translate.instant("onboarding.invitationForm.ediOptions.DURING_REG"), 'value': 'DURING_REG' }
    ];
    isEdiAddressMandatory: boolean;

    constructor(private route: ActivatedRoute, private translate: TranslateService,
        private onboardingService: OnboardingService, private datePipe: DatePipe, private notificationHandler: NotificationHandler, private router: Router) {

    }
    scrollConfig = PSscrollUtils.scrollY();
    ngOnInit() {
        this.isEdiAddressMandatory = this.onboardingService.isEdiAddressMandatory();
        let invitationId = this.route.snapshot.params['id'];
        this.readOnly = this.route.snapshot.queryParams["readOnly"];
        this.getInvitationList(invitationId);
    }
    getInvitationList(invitationId) {
        this.onboardingService.getInvitationDetails(invitationId).subscribe(result => {
            if (result.responseDetails.success) {
                this.invitation = result.responseDetails.responseEntity;

                this.invitationMap = new Map()
                this.invitation.invitationExtendedRequest.forEach(item => {
                    this.invitationMap.set(item.invitationAttrName, item.invitationAttrValue)
                })
                if (this.invitation.approvalType == "AUTO") {
                    this.invitation.approvalType = "AUTOMATIC";
                }
                for (let i = 0; i < this.ediOptions.length; i++) {
                    if (this.invitation.ediAddrCreateOption == this.ediOptions[i].value) {
                        this.invitation.ediAddrCreateOptionLabel = this.ediOptions[i].label;
                    }
                }

                this.invitation.invitationExpirationDate = this.datePipe.transform(this.invitation.invitationExpirationDate, 'MM/dd/yyyy');
                if (this.invitation.invitationMessage != null) {
                    this.invitation.subject = this.invitation.invitationMessage.subject;
                    this.invitation.content = this.invitation.invitationMessage.content;
                }
                this.invitation.senderEmailAddr = this.invitationMap.get(InvitationConstant.last_sent_from);
                this.invitation.emailNotify = this.invitation.emailNotify == "Y" ? true : false;
                this.invitation.invitingCoEdiAddr = this.invitationMap.get(InvitationConstant.edi_address);
                this.invitation.inviteeEdiAddress = this.invitation.invitingCoEdiAddr ? this.invitation.invitingCoEdiAddr.split(',') : [];
                this.listRecipients = this.invitation.notificationEmailRecipients ? this.invitation.notificationEmailRecipients.split(",") : [];
                this.ccList = this.invitation.ccRecipients ? this.invitation.ccRecipients.split(",") : [];
                if (this.invitation.imageContent) {
                    this.imageContent = this.invitation.imageContent;
                    this.imageType = this.invitation.imageName.substr(this.invitation.imageName.indexOf('.') + 1);
                }
            }
        },
            error => {
                this.notificationHandler.notify({ severity: 'error', details: error.restErrorDetails });
            });
    }
    preview() {
        this.invitation.imageType = this.imageType;
        this.invitation.imageContent = this.imageContent;
        this.invitation.invitationId = this.invitation.invitationId;
        this.invitation.readOnly = this.readOnly;
        this.invitation.workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.onboardingService.setCachedInvitationData(this.invitation);
        this.router.navigate(['onboarding/workflow/' + this.invitation.workflowId + '/invitations/preview'], { skipLocationChange: true });
    }
    editInvitation(invitationId) {
        this.invitation.imageType = this.imageType;
        this.invitation.imageContent = this.imageContent;
        this.onboardingService.setCachedInvitationData(this.invitation)
        let workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.router.navigate(['onboarding/workflow/' + workflowId + '/invitations/edit/' + invitationId], { skipLocationChange: true });
    }

}


