import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { Router } from '@angular/router';
import { OnboardingService } from "../../../../../../services/onboarding.service";
import { CustomFieldsService } from "../../../../../../services/custom-fields.service";
import { NotificationHandler } from "../../../../../../util/exception/notfication.handler";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { Url } from 'url';
import { PSscrollUtils } from 'tgocp-ng/dist';
import { TranslateService } from '@ngx-translate/core';
import { CompanyProfileService } from '../../../../../../services/company-profile.service';
import { Constants } from '../../../../../../services/AppConstant';
import * as _ from "lodash";
import { Observable } from 'rxjs';

@Component({
    selector: 'bulk-import',
    templateUrl: './bulk-import.component.html'
})

export class BulkImport implements OnInit {
    public config: PerfectScrollbarConfigInterface = {};
    @Input() showBulkImportModal: boolean;
    @Input() invitationId: any;
    @Input() invitationName: any;
    @Input() workflowId: any;
    @Input() workflowDisplayName: any;
    @Input() bulkImportType: string;
    @Output() showBulkImport = new EventEmitter<boolean>();
    url: Url;
    uploadedFileName: string;
    scrollConfig = PSscrollUtils.scrollY();
    nottificationMsgJson: any;
    constructor(private compProfile: CompanyProfileService, private onboardingService: OnboardingService, private customFieldsService: CustomFieldsService, private translate: TranslateService,
        private router: Router, private notificationHandler: NotificationHandler) { }

    ngOnInit() {
        this.translate.get("tpdir.create.notificationMsgs").subscribe(res => { this.nottificationMsgJson = res; })
    }

   

    fileUpload = { status: '', message: '' };
    myFile: File;
    fileMetaInfo = {};

    onSelectFile(event) {
        this.uploadedFileName = null;
        this.myFile = null;
        if (event.target.files && event.target.files[0]) {
            this.myFile = event.target.files[0];
            this.uploadedFileName = event.target.files[0].name
            const data = this.createFormData(this.myFile, null);
            let service: Observable<any>;
            if(this.bulkImportType === Constants.BULK_INVITE_TYPE){
                service=this.onboardingService.upload(this.invitationId, data, this.workflowId);
            }
            else if(this.bulkImportType === Constants.BULK_CUSTOM_IMPORT){
                service=this.customFieldsService.uploadCustomImportFile(data);
            }
            service.subscribe(
                event => {
                    this.fileUpload = event;
                    if (this.fileUpload) {
                        this.fileMetaInfo = this.fileUpload.message;
                    }
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


    closeWorkflowmodal() {
        this.showBulkImportModal = false;
        this.showBulkImport.emit(this.showBulkImportModal);
        this.uploadedFileName = null;
        this.myFile = null;
        this.fileMetaInfo = {};
        this.fileUpload = { status: '', message: '' };
        (<HTMLInputElement>document.getElementById("excelUpload")).value = '';

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
        if(this.bulkImportType === Constants.BULK_INVITE_TYPE){
            window.open(this.onboardingService.downloadTemaple(this.workflowId, this.workflowDisplayName + "_" + this.invitationName), '_self');
        }else{
            window.open(this.customFieldsService.downloadCustomImportTemplate("Import_Template"), '_self');
        }
       
    }

    submit() {
        const data = this.createFormData(this.myFile, this.fileMetaInfo);
        let service: Observable<any>;
        if(this.bulkImportType === Constants.BULK_INVITE_TYPE){
            console.log(Constants.BULK_INVITE_TYPE);
            service=this.onboardingService.submitUploadedFile(this.invitationId, data, this.workflowId);
        }
        else if(this.bulkImportType === Constants.BULK_CUSTOM_IMPORT){
            console.log(Constants.BULK_CUSTOM_IMPORT);
            service=this.customFieldsService.submitCustomImportUploadedFile(data);
        }


        service.subscribe(
            res => {
                this.fileMetaInfo = res['responseDetails']['responseEntity'];
                this.notificationHandler.notify({ severity: 'success', title: this.nottificationMsgJson['bulkimport.success'] });
                this.closeWorkflowmodal();
            },
            err => {
                this.notificationHandler.notify({ severity: 'error' });
                this.myFile = null;
                this.fileMetaInfo = null;
            });
    }

    get getKey():string{
        var bulkKey= 'tpdir.create.bulkImportType';
        if(this.bulkImportType === Constants.BULK_INVITE_TYPE)
            return bulkKey.replace('bulkImportType','bulk');
        else if(this.bulkImportType === Constants.BULK_CUSTOM_IMPORT)
            return bulkKey.replace('bulkImportType','bulkimport');
        
    }

}