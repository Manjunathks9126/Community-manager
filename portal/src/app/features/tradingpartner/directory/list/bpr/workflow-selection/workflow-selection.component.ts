import { Component, OnInit, Input, EventEmitter, Output } from "@angular/core";
import { Router } from '@angular/router';
import { OnboardingService } from "../../../../../../services/onboarding.service";
import { NotificationHandler } from "../../../../../../util/exception/notfication.handler";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { Url } from 'url';
import { PSscrollUtils } from 'tgocp-ng/dist';
import { TranslateService } from '@ngx-translate/core';
import { CompanyProfileService } from '../../../../../../services/company-profile.service';
import { Constants } from '../../../../../../services/AppConstant';
import * as _ from "lodash";

@Component({
    selector: 'workflow-selection',
    templateUrl: './workflow-selection.component.html'
})

export class WorkflowSelection implements OnInit {
    public config: PerfectScrollbarConfigInterface = {};
    @Input() workflowList: any[];
    @Input() showModal: boolean;
    @Input() bulkUpload: boolean;
    @Input() searchedTp:any;
    @Output() workflowParams = new EventEmitter<any>();
    selectedInvitationList: any[] = [];
    selectedInvitation: any = "";
    selectedWorkflow: any;
    disabledDropdownList = [];
    isBulkUploadModal: boolean;
    url: Url;
    uploadedFileName: string;
    scrollConfig = PSscrollUtils.scrollY();
    nottificationMsgJson: any;
    workflowTypes: any;
    isInvitationPresent: boolean;
    constructor(private compProfile: CompanyProfileService, private onboardingService: OnboardingService, private translate: TranslateService,
        private router: Router, private notificationHandler: NotificationHandler) { }

    ngOnInit() {
        this.translate.get("tpdir.create.notificationMsgs").subscribe(res => { this.nottificationMsgJson = res; })
        console.log(this.searchedTp)
    }

    fetchInvitationData(workflow) {
        this.selectedInvitation = "";
        this.selectedWorkflow = workflow;
        this.selectedInvitationList = [];
        this.workflowTypes = Constants;
        if (_.isEqual(workflow.type, this.workflowTypes.Skynet) || _.isEqual(workflow.type, this.workflowTypes.Teambook)) {

            this.isInvitationPresent = false;
        }
        else {
            this.isInvitationPresent = true;
        }
        this.onboardingService.getWorkflow(workflow.workflowId).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.selectedWorkflow = data['responseDetails']['responseEntity'];
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });

        this.onboardingService.listWorkflowInvitations(workflow.workflowId, "SAVED,ACTIVE").subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.selectedInvitationList = data['responseDetails']['responseEntity'];
                    this.onboardingService.invitationList = data['responseDetails']['responseEntity'];
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }


    private getUrl(type,data):string{
        let finalUrl = "";
        let urlArray = data.urls.filter(data=> data.indexOf(type)==0);
        if(urlArray.length > 0){
            let urlString = urlArray[0];
            finalUrl = urlString.replace(type+'=','');
        }
       return finalUrl;
    }

    addTp() {

        if (this.bulkUpload) {
            this.showModal = false;
            this.isBulkUploadModal = true;
            const {invitationId, invitationName} = this.selectedInvitation;    
            const {workflowId,displayName} = this.selectedWorkflow;
            this.workflowParams.emit({invitationId, invitationName,workflowId,displayName})
            return;
        }
        if (this.selectedWorkflow.type && this.selectedWorkflow.type.indexOf("non_neo") >= 0) {
            let param = "";
            if (this.onboardingService.isGDSflag) {
                param = this.onboardingService.getSkynetURL_params();
            }
            if (this.selectedWorkflow.type.indexOf("skynet")>=0) {
                this.onboardingService.getAppUrl().subscribe(data => {
                    window.open(this.getUrl(this.selectedWorkflow.type,data) + "&serviceMap=skynet" + param, "_blank");
                    this.closeWorkflowmodal();
                });
            } else {
                this.onboardingService.getAppUrl().subscribe(data => {
                    window.open(this.getUrl(this.selectedWorkflow.type,data) , "_blank");
                    this.closeWorkflowmodal();
                });
            }

            return;
        }

        let taskBased: boolean = false;
        this.onboardingService.getInvitationDetails(this.selectedInvitation.invitationId).subscribe(result => {
            if (result.responseDetails.success) {
                let invitation = result.responseDetails.responseEntity;
                taskBased = this.selectedWorkflow.type === Constants.TaskBased;

                if (taskBased) {
                    this.onboardingService.selectedInvitation = invitation;
                    this.onboardingService.selectedWorkFlow = this.selectedWorkflow;
                    this.router.navigate(['tpdir/createInvitation', invitation.invitationId], {skipLocationChange: true});
                } else if (this.selectedWorkflow.type) {
                    this.onboardingService.setInvIdForOnboarding(invitation.invitationId);
                    this.compProfile.selectedInvitaion = invitation.invitationId;
                    this.router.navigate(['tpdir/', this.selectedWorkflow.type], {skipLocationChange: true});
                } else {
                    this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("common.notFound") });
                    this.closeWorkflowmodal();
                }
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            this.closeWorkflowmodal();
        });
    }

    closeWorkflowmodal() {
        this.showModal = false;
        this.selectedInvitation = '';
        this.selectedInvitationList = [];
        this.isBulkUploadModal = false;
        this.uploadedFileName = null;
        this.myFile = null;
        this.fileMetaInfo = {};
        this.fileUpload = { status: '', message: '' };
        //(<HTMLInputElement>document.getElementById("excelUpload")).value = '';
        this.isInvitationPresent = true;

    }


    fileUpload = { status: '', message: '' };
    myFile: File;
    fileMetaInfo = {};

    onSelectFile(event) {
        this.uploadedFileName = null;
        this.myFile = null;
        //.invitation.imageContent = null;
        // called each time file input changes
        if (event.target.files && event.target.files[0]) {
            this.myFile = event.target.files[0];
            this.uploadedFileName = event.target.files[0].name
            /*  var reader = new FileReader();
             reader.onload = (event: any) => {
                 console.log(event)
               this.url = event.target.result;
             }
             reader.readAsDataURL(event.target.files[0]); */

            const data = this.createFormData(this.myFile, null);
            this.onboardingService.upload(this.selectedInvitation.invitationId, data, this.selectedWorkflow.workflowId).subscribe(
                event => {
                    this.fileUpload = event;
                    if (this.fileUpload) {
                        this.fileMetaInfo = this.fileUpload.message;
                    }
                    /* 
                    if (event.type === HttpEventType.UploadProgress) {
                        // This is an upload progress event. Compute and show the % done:
                        const percentDone = Math.round(100 * event.loaded / event.total);
                        console.log(`File is ${percentDone}% uploaded.`);
                      } else if (event instanceof HttpResponse) {
                        console.log('File is completely uploaded!');
                      } 
                      
                      */
                },
                err => {
                    this.notificationHandler.notify({ severity: 'error', title: err.userMessage });
                    this.myFile = null;
                }
            );
        } else {
            this.fileUpload = { status: '', message: '' };
        }
    }

    private createFormData(file, metaData): FormData {
        let formData = new FormData();
        formData.append("file", file);
        if (metaData != null) {
            formData.append("metaInfo", new Blob([JSON.stringify(metaData)], {
                type: "application/json"
            }));
        }
        return formData;
    }

    downloadTemplate() {
        window.open(this.onboardingService.downloadTemaple(this.selectedWorkflow.workflowId, this.selectedWorkflow.displayName + "_" + this.selectedInvitation.invitationName), '_self');
    }

    submit() {
        const data = this.createFormData(this.myFile, this.fileMetaInfo);
        this.onboardingService.submitUploadedFile(this.selectedInvitation.invitationId, data, this.selectedWorkflow.workflowId).subscribe(
            res => {
                this.fileMetaInfo = res['responseDetails']['responseEntity'];
                this.notificationHandler.notify({ severity: 'success', title: this.nottificationMsgJson['bulk.success'] });
                this.closeWorkflowmodal();
            },
            err => {
                this.notificationHandler.notify({ severity: 'error' });
                this.myFile = null;
                this.fileMetaInfo = null;
            });
    }
}

