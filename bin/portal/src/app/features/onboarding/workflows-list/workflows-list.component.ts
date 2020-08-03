import { Component, OnInit } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OnboardingService } from "../../../services/onboarding.service";
import { NotificationHandler } from "../../../util/exception/notfication.handler";

@Component({
    selector: 'workflows-list',
    templateUrl: "./workflows-list.component.html"
})
export class WorkflowsListComponent implements OnInit {
    workflows: any[] = [];
    isFilterApplied = false;
    currentLanguage: string = "en";
    constructor(private translate: TranslateService, private route: ActivatedRoute,
        private router: Router, private onboardingService: OnboardingService,
        private notificationHandler: NotificationHandler) {
    }

    ngOnInit() {
        this.translate.use(this.route.snapshot.data['userLocale']);
        this.onboardingService.getInternalCompanyDetails().subscribe(data => {
            let gxsCompanyId = data.buId;
            this.onboardingService.getLoggedInCompanyDetails().subscribe(data => {
                if (null != data && data.success) {
                    let loggedInCompanyId = data.responseEntity.companyId;
                    // Attaching EDI Address to Invitation is not Mandatory for GXS Company Generic workflows/Invitations
                    this.onboardingService.setEdiAddressMandatory(!(gxsCompanyId === loggedInCompanyId));
                }
            }, error => {
            })
        });
        this.onboardingService.listWorkflows().subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.workflows = data['responseDetails']['responseEntity'];
                    this.onboardingService.workflowList = data['responseDetails']['responseEntity'];
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        this.currentLanguage = this.translate.currentLang;
    }

    navigateToOverview(workflowId) {
        this.router.navigate(['onboarding/workflow', workflowId], { skipLocationChange: true });
    }

    public downloadReport(workflowId) {
        this.onboardingService.getOnboardingWorkflowReport(workflowId).subscribe(result => {
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
}