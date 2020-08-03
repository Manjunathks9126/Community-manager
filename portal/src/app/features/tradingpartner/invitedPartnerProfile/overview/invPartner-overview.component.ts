import { Component, OnInit } from "@angular/core";
import { InvitedPartnerProfileService } from '../../../../services/invitedPartner-profile.service';
import { NotificationHandler } from '../../../../util/exception/notfication.handler';
import { Router } from '@angular/router';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { TranslateService } from '@ngx-translate/core';
import { ReinvitationEntity, CompanyDetailsEntity } from '../../directory/create-invitation/createInvitation.entity';
import { SchemaFormOutputService } from 'json-editor/dist/src/app/services/schema-form-output.service';
import { PERMISSSION } from '../../directory/createTPMSG/entity/permission.constants';


@Component({
    selector:"invited-partner-overview",
    templateUrl:'./invPartner-overview.component.html'
})
export class InvitedPartnerOverviewComponent implements OnInit{

    constructor(private invitedPartnerProfileService:InvitedPartnerProfileService,
        private notificationHandler: NotificationHandler,private router:Router,
        private dialogueboxService: DialogueboxService,private translateService: TranslateService,
        public datacardService: SchemaFormOutputService){}
    
    invitedTPInfo:any;
    requesterTaskSchema:any;
    requesterTaskData:any;
    schemaData:any;
    invPartnerProfileSubscription:any;
    requesterDataSubscription:any;
    scrollConfig = PSscrollUtils.scrollY();
    invPartnerMessage:any;
    isFormValid = false;
    reInvitationEntity:ReinvitationEntity = new ReinvitationEntity();
    reInvCompanyDetails: CompanyDetailsEntity = new CompanyDetailsEntity();
    reinviteAlertMsg:string="";
    permissions:any;

    ngOnInit(): void {
        this.permissions=PERMISSSION;
        this.translateService.get("invitedTps.messages").subscribe(res => { this.invPartnerMessage = res; })
        this.invitedTPInfo=this.invitedPartnerProfileService.getInvitedPartnerInfo();
        this.invPartnerProfileSubscription=this.invitedPartnerProfileService.getRequesterTaskSchema().subscribe(data=>{
        this.requesterTaskSchema= data;
       });
       this.requesterDataSubscription=this.invitedPartnerProfileService.getRequesterTaskdata().subscribe(data=>{
           this.schemaData=data;
       })
      
    }
    ngOnDestroy(){
        if(this.invPartnerProfileSubscription){
            this.invPartnerProfileSubscription.unsubscribe();
        }
        if(this.requesterDataSubscription){
            this.requesterDataSubscription.unsubscribe();
        }
        
    }
     formValidation(status){
        this.isFormValid = (status == 'VALID' ? true : false);
     }
    deleteInvite(){
       let invitationCode=this.invitedTPInfo.invitationCode;
        if (invitationCode) {
            this.dialogueboxService.confirm({
              dialogName: 'deleteInvite',
              accept: () => {
                this.invitedPartnerProfileService.deleteInvitationSent(invitationCode).subscribe(data=>{
                    this.notificationHandler.notify({ severity: 'success', title: this.invPartnerMessage.success.deleteInvitationMsg});
                      this.router.navigate(['/tpdir'], {queryParams:{'listView': 'INVTD'}, skipLocationChange: true });
                },error=>{
                    this.notificationHandler.notify({ severity: 'error', title: this.invPartnerMessage.success.deleteInvitationMsg });
                })
              }
            });
      
          }       
    }
    initiateReInvitation(){
        this.datacardService.callOutput();
    }

    sendInvitation(outputJson){
        console.log(outputJson);
        if(this.isFormValid){
            this.reInvCompanyDetails.companyId = outputJson.companyInfo.companyId;
            this.reInvCompanyDetails.companyName = outputJson.companyInfo.companyName;
            this.reInvCompanyDetails.contact.email = outputJson.contactInfo.email;
            this.reInvitationEntity.companyDetailsEntity = this.reInvCompanyDetails;
            this.reInvitationEntity.invitationCode = this.invitedTPInfo.invitationCode;
            this.reInvitationEntity.invitationId = this.invitedTPInfo.invitationId;
            this.reInvitationEntity.requestorTask = JSON.stringify(outputJson);
            this.reInvitationEntity.workflowId = this.invitedTPInfo.workflowId;
            this.reInvitationEntity.taskId= this.invitedPartnerProfileService.taskId;
            this.reInvitationEntity.serviceName=this.invitedTPInfo.serviceName;

             this.getAlertMessage(outputJson.companyInfo.companyName,outputJson.contactInfo.email);//this.invitedTPInfo.companyIdentifier,

            this.dialogueboxService.confirm({
                dialogName: 'reInviteAlert',
                accept: () => {
                            this.invitedPartnerProfileService.reinviteWithNew(this.reInvitationEntity).subscribe(results => {
                                this.notificationHandler.notify({severity: 'success', title: this.invPartnerMessage.success.reinviteMsg});

                                this.router.navigate(['/tpdir'], {queryParams:{'listView': 'INVTD'}, skipLocationChange: true });
                            }, error => {
                                this.notificationHandler.notify({ severity: 'error', title: this.invPartnerMessage.error.reinviteMsg });
                            });
                        }
                    });    
                    
    
        }
    }

    getAlertMessage(companyName,emailId){
        this.reinviteAlertMsg= this.invPartnerMessage.alert.reinviteMsg;
        /*let pattern = /{(.*?)}/;
         const matched:string[] = this.reinviteAlertMsg.match(pattern);
         console.log("matched: ",matched) 
         this.reinviteAlertMsg=matched!=null ? this.reinviteAlertMsg.replace("{0}",this.invitedTPInfo.companyIdentifier): this.reinviteAlertMsg;
         this.reinviteAlertMsg=matched!=null ? this.reinviteAlertMsg.replace("{1}",this.invitedTPInfo.companyIdentifier): this.reinviteAlertMsg;
        */
        
         this.reinviteAlertMsg=this.reinviteAlertMsg.replace("{0}",companyName);
         this.reinviteAlertMsg=this.reinviteAlertMsg.replace("{1}",emailId);
        
    }
}