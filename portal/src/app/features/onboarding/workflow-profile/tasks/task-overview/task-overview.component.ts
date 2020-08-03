import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from '@angular/router';
import { PSscrollUtils } from "tgocp-ng/dist";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { CustomFieldsService } from '../../../../../services/custom-fields.service';

@Component({
    templateUrl: "./task-overview.component.html"
})

export class TaskOverviewComponent implements OnInit {

    scrollConfig = PSscrollUtils.scrollY();

    constructor(private onboardingService: OnboardingService, private router: Router, private route: ActivatedRoute,
        private customFieldsService: CustomFieldsService) { }

    readOnly: boolean = false;
    taskDetail: any = {};
    fieldGroupOptionList: any[] = [];
    fgIds: any = [];
    displayFG = [];
   

    ngOnInit(): void {
        let taskId = this.route.snapshot.params['id'];
        this.readOnly = this.route.snapshot.queryParams["readOnly"];
        this.getTask(taskId);
        this.getAssociatedFGIds(taskId);
    }

    getTask(taskId) {
        this.taskDetail = this.onboardingService.getTaskById(taskId);
    }
    getAssociatedFGIds(taskId) {
        this.onboardingService.getAssociatedFGIds(taskId).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.fgIds = data['responseDetails']['responseEntity'];
                this.getFGList();
            }
        }, error => {
            console.log("error")
        });
    }

    getFGList() {
        this.customFieldsService.getCustomFieldGroup(true).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.fieldGroupOptionList = data['responseDetails']['responseEntity'].itemList;
                this.getSelectedFGName();
            }
        }, error => {

        });

    }

    getSelectedFGName() {
        this.displayFG = [];
        for (let id of this.fgIds) {
            for (let list of this.fieldGroupOptionList) {
                if (list.uniqueId === id) {
                    this.displayFG.push(list.name);
                }
            }
        }
    }

    editTask(taskId) {
        this.onboardingService.setCachedTaskDetail(this.taskDetail);
        let workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.router.navigate(['onboarding/workflow/' + workflowId + '/tasks/edit/' + taskId], { queryParams: { 'create': 'false' },  skipLocationChange: true  });
    }

}