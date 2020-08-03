import { Component } from "@angular/core";
import { NotificationHandler } from "../../../../util/exception/notfication.handler";
import { ActivatedRoute, Router } from "@angular/router";
import * as _ from "lodash";
import { OnboardingService } from "../../../../services/onboarding.service";
import { CreateInvitationEntity, CompanyDetailsEntity } from "./createInvitation.entity";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { TranslateService } from '@ngx-translate/core';
import { SchemaFormOutputService } from 'json-editor/dist/src/app/services/schema-form-output.service';
import { Constants } from '../../../../services/AppConstant';
import { CompanyRegistrationService } from "../../../../services/company.registration.service";
import { JsonSharedService } from 'json-editor/dist/src/app/services/json-shared.service';

@Component({
    templateUrl: "./create-invitation.component.html"
})
export class CreateInvitationComponent {
    isFormValid = false;
    requesterTaskSchema: string;
    scrollConfig = PSscrollUtils.scrollY();
    invitation: any = {};
    workflow: any = {};
    taskId: any = "";
    tpSelfServiceMode: boolean;
    registrationTasksListURL: string;
    locale = "en";

    cancelWrkflwSubscription: any;
    successSubscription: any;
    contextData: any = {};
    submitUrl = this.companyRegistrationService.registrationSumbitURL;

    companyDetails: CompanyDetailsEntity = new CompanyDetailsEntity();
    createInvitationEntity = new CreateInvitationEntity();

    constructor(private notificationHandler: NotificationHandler,
        private translateService: TranslateService, public datacardService: SchemaFormOutputService,
        private route: ActivatedRoute, private router: Router, private onboardingService: OnboardingService,
        private companyRegistrationService: CompanyRegistrationService, private jsonSharedService: JsonSharedService) {
        this.onLoadSubscriptions();
    }

    ngOnInit() {
        if (this.route.routeConfig.path != "createInvitation") {
            this.invitation = this.onboardingService.selectedInvitation;
        }
        this.workflow = this.onboardingService.selectedWorkFlow;
        this.tpSelfServiceMode = this.workflow.selfServiceMode === Constants.TP_SELF_SERVICE;
        this.getRequestorTask();
    }

    onLoadSubscriptions() {
        //cancel subscription
        this.cancelWrkflwSubscription = this.jsonSharedService.onCancel().subscribe(data => {
            this.companyRegistrationService.cancelRegistration();
        });

        //success subscription
        this.successSubscription = this.jsonSharedService.onWorkflowSuccess().subscribe(data => {
            let message = {
                "title": this.translateService.instant("common.workflowSuccess"),
                "severity": "success",
                "details": this.translateService.instant("common.workflowId").replace("{0}", data.responseDetails.responseEntity.targetResourceRefId)
            }
            this.notificationHandler.notify(message);
            setTimeout(() => {
                this.router.navigate(['/tpdir']);
            }, 6500);
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: this.translateService.instant("tpdir.company.taskregistration.failed"), details: error.userMessage });
        });
    }

    private getRequestorTask() {
        this.onboardingService.listWorkflowTasks(this.workflow.workflowId, 'Requester').subscribe(data => {
            let taskList = data.responseDetails.responseEntity;
            this.taskId = taskList[0].task.taskId;
            this.onboardingService.getTask(this.taskId, this.translateService.currentLang).subscribe(data => {
                this.requesterTaskSchema = JSON.parse(data.jsonSchema);
            });
        }, error => {
            if (this.tpSelfServiceMode)
                this.notificationHandler.notify({ severity: 'error', title: error.userMessage, details: error.restErrorDetails });
            else
                this.processInvitation(null);
        });
    }

    cancel() {
        if (this.onboardingService.isGDSflag) {
            this.onboardingService.isGDSflag = false;
            this.router.navigate(['globalDirectory'], { queryParams: { 'gd': 't' }, skipLocationChange: true });

        } else {
            this.router.navigate(['tpdir/']);
        }
    }

    initiateInvitation() {
        this.datacardService.callOutput();
    }

    processInvitation(outputJson) {
        if (this.tpSelfServiceMode) {
            this.sendInvitation(outputJson);
        } else {
            this.contextData.invitationId = this.invitation.invitationId;
            this.contextData.workflowId = this.workflow.workflowId;
            this.contextData.serviceName = this.workflow.serviceName;
            this.contextData.requesterData = outputJson;
            this.contextData.workflowDisplayName=this.workflow.displayName;
            this.registrationTasksListURL = this.companyRegistrationService.buildRegistrationTasksURL(this.workflow.workflowId, this.locale, this.workflow.ownerBuId);
        }
    }

    sendInvitation(outputJson) {
        if (this.isFormValid) {
            if (outputJson.companyInfo && outputJson.contactInfo && outputJson.contactInfo.email) {
                this.companyDetails.companyId = outputJson.companyInfo.companyId;
                this.companyDetails.companyName = outputJson.companyInfo.companyName;
                this.companyDetails.contact.email = outputJson.contactInfo.email;
                this.createInvitationEntity.companyDetailsEntity = this.companyDetails;
                this.createInvitationEntity.invitationEntity = this.invitation;
                this.createInvitationEntity.requestorTask = JSON.stringify(outputJson);
                this.createInvitationEntity.workflowId = this.workflow.workflowId;
                this.createInvitationEntity.taskId = this.taskId;
                this.createInvitationEntity.serviceName = this.workflow.serviceName;
                this.onboardingService.invite(this.createInvitationEntity).subscribe(results => {
                    this.notificationHandler.notify({
                        severity: 'success', title: this.translateService.instant("createInvitation.invited")
                    });

                    let scope = this;
                    setTimeout(function (this) { scope.router.navigate(['tpdir']) }, 1000);
                }, error => {
                    this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
                });
            } else {
                this.router.navigate(['tpdir']);
            }
        }
    }

    formValidation(status) {
        this.isFormValid = (status == 'VALID' ? true : false);
    }

    ngOnDestroy() {
        this.cancelWrkflwSubscription.unsubscribe();
        this.successSubscription.unsubscribe();
    }

}