import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute, Params } from "@angular/router";
import { PSscrollUtils, BreadCrumbsService } from "tgocp-ng/dist";
import { OnboardingService } from "../../../../services/onboarding.service";
import { NotificationHandler } from "../../../../util/exception/notfication.handler";
import { DatePipe } from '@angular/common';
import { TranslateService } from "@ngx-translate/core";
import * as _ from "lodash";
import { Constants } from '../../../../services/AppConstant';

@Component({
    selector: 'workflow-overview',
    templateUrl: "./workflow-overview.component.html"
})
export class WorkflowOverviewComponent implements OnInit {

    scrollConfig = PSscrollUtils.scrollY();
    workflowData: any = {};
    editMode: boolean;
    workflowTypes: any;
    showDownloadReport: boolean;
	isStandardWorkflow:boolean;
    constructor(private onboardingService: OnboardingService, private notificationHandler: NotificationHandler,
        private breadCrumbService: BreadCrumbsService, private datePipe: DatePipe, private router: Router,
        private translate: TranslateService, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.workflowTypes = Constants
        this.workflowData = this.onboardingService.selectedWorkFlow;
		 if( this.workflowData.ownerBuId!= this.onboardingService.loggedBuId){
            this.isStandardWorkflow=true;
        }
        else{
            this.isStandardWorkflow=false;
        }
        if (_.isEqual(this.workflowData.type, this.workflowTypes.Skynet) || _.isEqual (this.workflowData.type, this.workflowTypes.Teambook)) {
            this.showDownloadReport = false;
        }
        else{
            this.showDownloadReport = true;
        }
        this.editMode = this.activatedRoute.snapshot.data['editMode'];
    }
    saveWorkflow() {
      
        this.onboardingService.updateWorkflow(this.workflowData).subscribe(result => {
            this.workflowData = result.responseDetails.responseEntity;
            this.onboardingService.selectedWorkFlow = result.responseDetails.responseEntity;
            this.notificationHandler.notify({
                severity: 'success',
                title: this.translate.instant("onboarding.workflowForm.workflowUpdateSuccessMSg")
            });
            this.router.navigate(['onboarding']);
        },
            error => {
                this.notificationHandler.notify({ severity: 'error', details: error.restErrorDetails });
            });
    }

    public downloadReport() {
        this.onboardingService.getOnboardingWorkflowReport(this.workflowData.workflowId).subscribe(result => {
            let report = result.responseDetails.responseEntity;
            if (report != null && report.itemList != null && report.itemList.length > 0) {
                this.onboardingService.exportToCsv(report.itemList);
            } else if (report.itemCount > 0) {
                this.notificationHandler.notify({
                    severity: 'success',
                    title: this.translate.instant("onboarding.download.downloadMSg")
                });
            } else {
                this.notificationHandler.notify({
                    severity: 'info',
                    title: this.translate.instant("onboarding.download.downloadError")
                });
            }
        },
            error => {
                this.notificationHandler.notify({ severity: 'error', details: error.restErrorDetails });
            });
    }


    editWorkflow(workflowId) {
        this.router.navigate(['onboarding/workflow', workflowId, 'overview', 'edit'], { skipLocationChange: true });
    }
}