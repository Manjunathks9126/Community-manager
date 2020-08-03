import { Component, OnInit } from "@angular/core";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { Router } from "@angular/router";
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { TranslateService } from '@ngx-translate/core';


@Component({
    templateUrl: "./tasks-list.component.html"
})
export class TaskListComponent implements OnInit {

    taskList = [];
    selectedTask: any[];
    customTask: string;
    selectedWorkflow: any;
    addCustomTaskFlag: boolean;
    taskBasedType: string = "taskBased";
    multiSortMeta = [];
    currentLanguage:string="en";
    constructor(private onboardingService: OnboardingService, private notificationHandler: NotificationHandler, 
        private router: Router, private dialogueboxService: DialogueboxService, private translate: TranslateService) { }

    ngOnInit(): void {
        this.getWorkflowTaskList();
        this.isCustomTaskApplicable();
        this.currentLanguage=this.translate.currentLang;
    }
    getWorkflowTaskList() {
        this.onboardingService.listWorkflowTasks(this.onboardingService.selectedWorkFlow['workflowId'], null).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.taskList = data['responseDetails']['responseEntity'];
                    this.customTask = this.taskList.find(tasks => tasks.task.taskType == 'Custom');
                    this.onboardingService.setCachedTaskList(this.taskList);
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }

    navigateToOverview(taskId) {
        let workflowId = this.onboardingService.selectedWorkFlow['workflowId'];
        this.router.navigate(['onboarding/workflow/' + workflowId + '/tasks/overview/' + taskId], { queryParams: { readOnly: 'true' } ,  skipLocationChange: true });
    }
    delete() {
        let workflowTaskIds: number[] = [];
        this.selectedTask.forEach(task => workflowTaskIds.push(task.workflowTaskId))
        if (workflowTaskIds.length > 0) {
            this.dialogueboxService.confirm({
                dialogName: 'deleteCustomTask',
                accept: () => {
                    this.onboardingService.deleteCustomTask(workflowTaskIds, this.onboardingService.selectedWorkFlow['workflowId']).subscribe(data => {
                        if (data['responseDetails']['success']) {
                            this.notificationHandler.notify({ severity: 'success', title: this.translate.instant("onboarding.task.deleteTaskSuccess") })
                            this.reloadTable();

                        }

                        else if (data['responseDetails']['statusMessage'] != null) {
                            this.notificationHandler.notify({ severity: 'error', title: this.translate.instant("onboarding.task.deleteTaskFailure"), details: data['responseDetails']['statusMessage'] })
                            this.reloadTable();

                        }
                    },
                        error => this.notificationHandler.notify({ severity: 'error', title: this.translate.instant("onboarding.task.deleteTaskFailure"), details: error.restErrorDetails }),
                        () => this.reloadTable(),

                    )
                }


            })

        }
    }
    reloadTable() {
        this.selectedTask = [];
        this.getWorkflowTaskList();
    }

    isCustomTaskApplicable() {
        this.onboardingService.getWorkflow(this.onboardingService.selectedWorkFlow['workflowId']).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    let selectedWorkflow = data['responseDetails']['responseEntity'];
                    if (!selectedWorkflow.type || selectedWorkflow.type == this.taskBasedType) {
                        this.addCustomTaskFlag = true;
                    }
                }
            })
    }
}