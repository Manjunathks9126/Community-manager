import { Component, OnInit } from "@angular/core";
import { OnboardingService } from "../../../../../services/onboarding.service";
import * as _ from "lodash";
import { PREVIEW } from "../../../../tradingpartner/directory/createTPMSG/entity/preview.constants";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { Company } from "../../../../tradingpartner/companyprofile/companyProfile.entity";
import { Router } from "@angular/router";

@Component({
    templateUrl: "./invitation-preview.component.html"
})
export class InvitationPreviewComponent implements OnInit {
    url: string = "";
    invitationMessage: string = "";
    invitation: any;
    company: Company;
    companyName: string = "";
    registrationUrl: string = "";
    completeRegistrationUrl: string = "";
    invitationComments: string = "";
    service:string;
    invitationTextParameters: any;
    constructor(private onboardingService: OnboardingService, private router: Router) {

    }
    scrollConfig = PSscrollUtils.scrollY();
    ngOnInit() {
        this.invitation = this.onboardingService.getCachedInvitationData();
        if (this.invitation.invitationMessage&& this.invitation.invitationMessage.subject) {
            this.invitation.invitationSubject = this.invitation.invitationMessage.subject;
        }
        this.url = this.invitation.url;
        this.service = this.onboardingService.selectedWorkFlow['serviceName'];
        this.onboardingService.getPreviewUtil().subscribe(data => {
            this.registrationUrl = data.registrationUrl;
            this.completeRegistrationUrl = this.registrationUrl + PREVIEW.registrationUrl;
            this.companyName = data.companyName;
            this.invitationTextParameters = {
                companyName: this.companyName, 
                service: this.service, 
                completeRegistrationUrl: this.completeRegistrationUrl, 
                registrationUrl: this.registrationUrl, 
                currentYear: new Date().getFullYear(), 
                invitationcontent: this.invitation.content
            }
        });
        
    }
    cancel() {
        let worfFlowId = this.onboardingService.selectedWorkFlow['workflowId'];
        if (this.invitation.readOnly) {
            this.router.navigate(['onboarding/workflow/' + worfFlowId + '/invitations/overview/' + this.invitation.invitationId],  { skipLocationChange: true })
        }
        else if (this.invitation.editMode) {
            this.router.navigate(['onboarding/workflow/' + worfFlowId + '/invitations/edit/' + this.invitation.invitationId], { queryParams: { isedit: 'true', editMode: 'false' },  skipLocationChange: true  });
        }
        else {
            this.router.navigate(['onboarding/workflow/' + worfFlowId + '/invitations/create'], { queryParams: { cancel: 'true' },  skipLocationChange: true });
        }
        this.onboardingService.setCachedInvitationData(this.invitation);
    }
}
