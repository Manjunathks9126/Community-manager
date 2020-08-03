import { Component, OnInit } from "@angular/core";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { Router } from "@angular/router";
import { TranslateService } from '@ngx-translate/core';

@Component({
    templateUrl: "./invitations-list.component.html"
})
export class InvitationsListComponent implements OnInit {
    invitations: any[] = [];
    currentLanguage:string="en";
    constructor(private onboardingService: OnboardingService, private notificationHandler: NotificationHandler, private router: Router,
        private translate: TranslateService) {
    }

    ngOnInit() {
        this.onboardingService.listWorkflowInvitations(this.onboardingService.selectedWorkFlow['workflowId'], null).subscribe(
            data => {
                if (data['responseDetails']['success'] && data['responseDetails']['responseEntity'].length > 0) {
                    this.invitations = data['responseDetails']['responseEntity'];
                    this.onboardingService.invitationList = data['responseDetails']['responseEntity'];
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
            this.currentLanguage=this.translate.currentLang;
    }
    
    navigateToOverview(invitationId) {
        let workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.router.navigate(['onboarding/workflow/' + workflowId + '/invitations/overview/' + invitationId], { queryParams: { readOnly: 'true' } ,  skipLocationChange: true });
    }
}
