import { Component, OnInit } from "@angular/core";
import { OnboardingService } from '../../../services/onboarding.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationHandler } from '../../../util/exception/notfication.handler';
import { InvitedPartnerProfileService } from '../../../services/invitedPartner-profile.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    templateUrl: './invPartner-profile.component.html'
})
export class InvitedPartnerProfileComponent implements OnInit {

    workflowId: any;
    requestId: any;
    taskId: any;
    requesterTaskSchema: any;
    requesterTaskData: any;
    invitedTPInfo: any;

    constructor(private onboardingService: OnboardingService, private translateService: TranslateService, private route: ActivatedRoute,
        private notificationHandler: NotificationHandler, private invitedPartnerprofileService: InvitedPartnerProfileService) { }

    ngOnInit(): void {
        this.invitedTPInfo = this.invitedPartnerprofileService.getInvitedPartnerInfo();
        this.workflowId = this.route.snapshot.params['workflowId'];
        this.getRequestorTask();
    }

    private getRequestorTask() {
        this.onboardingService.listWorkflowTasks(this.workflowId, 'Requester').subscribe(data => {
            let taskList = data.responseDetails.responseEntity;
            this.taskId = taskList[0].task.taskId;
            this.invitedPartnerprofileService.taskId = this.taskId;
            this.onboardingService.getTask(this.taskId, this.translateService.currentLang).subscribe(data => {
                this.requesterTaskSchema = JSON.parse(data.jsonSchema);
                this.invitedPartnerprofileService.setRequesterTaskSchema(this.requesterTaskSchema);
                this.getRequesterTaskdata();
            });
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: error.userMessage, details: error.restErrorDetails });
        });
    }

    private getRequesterTaskdata() {
        this.requestId = this.invitedTPInfo.requestId;
        this.onboardingService.getRequesterTaskdata(this.requestId).subscribe(data => {
            let requestData = data.responseDetails.responseEntity;
            this.requesterTaskData = JSON.parse(requestData.outputData);
            this.invitedPartnerprofileService.setRequesterTaskData(this.requesterTaskData);
        }, error => {
            console.log(error)
        })
    }

}