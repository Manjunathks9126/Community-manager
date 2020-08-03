import { Component, OnInit } from "@angular/core";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { CustomFieldsService } from '../../../../../services/custom-fields.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { PSscrollUtils } from 'tgocp-ng/dist';
import { TranslateService } from '@ngx-translate/core';

@Component({
    templateUrl: "./task.component.html"
})

export class TaskComponent implements OnInit {
    scrollConfig = PSscrollUtils.scrollY();
    task: any = {};
    onbotask: any = {};
    fieldGroups: any = {};
    workflowId: any = "";
    fieldGroupList = [];
    fieldGroupListOption: any;
    selectedFieldGroups: any[] = [];
    createFlag: any;

    constructor(private onboardingService: OnboardingService, private customFieldsService: CustomFieldsService,
        private route: ActivatedRoute, private router: Router, private notificationHandler: NotificationHandler,
        private translate: TranslateService) { }

    ngOnInit(): void {
        this.workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.createFlag = this.route.snapshot.queryParams['create']
        if (this.createFlag == 'false') {
            let taskId = this.route.snapshot.params['id'];
            this.task = this.onboardingService.getCachedTaskDetail().task;
            this.onbotask = this.onboardingService.getCachedTaskDetail();
            this.getSelectedFGs(taskId);
        }
        this.getFGOptions();
    }

    getFGOptions() {
        this.customFieldsService.getCustomFieldGroup(true).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.fieldGroupListOption = [];
                this.fieldGroupList = data['responseDetails']['responseEntity'].itemList;
                if (this.fieldGroupList) {
                    this.fieldGroupList.forEach(fieldGroup => {
                        this.fieldGroupListOption.push({ label: fieldGroup.name, value: fieldGroup.uniqueId })
                    });
                }
            }
        }, error => {

        });
    }

    getSelectedFGs(taskId) {
        this.onboardingService.getAssociatedFGIds(taskId).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.selectedFieldGroups = data['responseDetails']['responseEntity'];
            }
        });
    }

    saveTask(taskId) {
        this.onbotask.taskId = this.onbotask.task.taskId;
        this.onbotask.task.displayName = this.task.displayName;
        let requestData = {
            "taskId": taskId,
            "fgId": this.selectedFieldGroups,

        }
        this.onbotask.fieldgroup = requestData;
        this.onboardingService.saveFieldGroup(taskId, this.onbotask).subscribe(data => {

            this.notificationHandler.notify({
                severity: 'success',
                title: this.translate.instant("onboarding.task.customTaskUpdateSuccess")
            });
            this.router.navigate(['onboarding/workflow/' + this.workflowId + '/tasks'], { skipLocationChange: true });
        }, error => {
            this.notificationHandler.notify({ severity: 'error', details: error.restErrorDetails })
        });
    }
    createTask() {
        this.task.taskStage = 'TP';
        this.task.taskType = 'Custom';
        this.onbotask.workflowId = this.workflowId;
        this.onbotask.fgId = this.selectedFieldGroups;
        this.onbotask.task = this.task;
        this.onboardingService.createCustomTask(this.onbotask, this.workflowId).subscribe(data => {
            this.onboardingService.taskList = data['responseDetails']['responseEntity']

            this.notificationHandler.notify({
                severity: 'success',
                title: this.translate.instant("onboarding.task.customTaskCreateSuccess")
            });
            this.router.navigate(['onboarding/workflow/' + this.workflowId + '/tasks'], { skipLocationChange: true });
        }, err =>
                this.notificationHandler.notify(
                    {
                        severity: 'error',
                        title: this.translate.instant("onboarding.task.CustomTaskCreateFailure"),
                        details: err.restErrorDetails
                    }));
    }
}