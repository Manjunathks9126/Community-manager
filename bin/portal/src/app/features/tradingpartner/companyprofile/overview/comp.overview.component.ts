import { Component, OnInit, OnDestroy } from "@angular/core";
import { Company } from "../companyProfile.entity";
import { BusinessFacades } from "../../facades.entity";
import { TradingParnterService } from "../../../../services/trading-partner.service";
import { CompanyProfileService } from "../../../../services/company-profile.service";
import { NotificationHandler } from "../../../../util/exception/notfication.handler";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from '@angular/router';
import { OnboardingService } from '../../../../services/onboarding.service';
import { TranslateService } from '@ngx-translate/core';
import { PERMISSSION } from '../../directory/createTPMSG/entity/permission.constants';
import { BreadCrumbsService } from 'tgocp-ng/dist';

@Component({
    selector: 'tp-profile-overview',
    templateUrl: './comp.overview.component.html'
})

export class CompanyOverviewComponent implements OnInit, OnDestroy {

    scrollConfig = PSscrollUtils.scrollY();
    company: Company;
    editMode: boolean; // Is Component in edit mode?
    facadeDataSubscription: Subscription;
    facadeData: BusinessFacades = new BusinessFacades();
    isPrivateView: boolean = false;
    globalDirFlag: boolean = false;
    showWorkflowModal: boolean;
    onboardingErrorDetails: string;
    workflowList: any;
    display: boolean;
    formValidFlag: Subscription;
    bprId: string;
    formValid: boolean = true;
    bprStatus: string;
    availableEDI: boolean;
    displayName: string;
    companyDisplayNameEdit: string;
    companyDisplayNameTempEdit: string;
    companyIDEdit: string;
    facadeId: string;
    companyIdRegx = "^[a-zA-Z0-9äöüÄÖÜ ]*$";
    buId: string;
    permissions:any;

    constructor(private route: ActivatedRoute, private router: Router,private breadCrumbService: BreadCrumbsService,
        private tpService: TradingParnterService, private cpService: CompanyProfileService,
        private onboardingService: OnboardingService, private notificationHandler: NotificationHandler, private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.permissions=PERMISSSION;
        this.buId = this.route.snapshot.params['id'];
        this.bprId = this.route.snapshot.queryParams['bprId'];
        this.facadeData.uniqueId = this.route.snapshot.queryParams['facadeId'];
        this.company = this.cpService.getCompany();
        this.companyIDEdit = this.company.facadeCompanyId
        if(this.route.snapshot.queryParams['name']){
            this.tpService.displayName = this.route.snapshot.queryParams['name'];
            this.companyDisplayNameEdit = this.route.snapshot.queryParams['name'];
        }else{
            this.companyDisplayNameEdit =  this.tpService.displayName;
        }
        
        this.companyDisplayNameTempEdit = this.companyDisplayNameEdit;
        this.bprStatus = this.route.snapshot.queryParams['status'] ? this.route.snapshot.queryParams['status'] : this.company.bprStatus;
        this.onboardingErrorDetails = this.cpService.getOnboardingHistory() && this.cpService.getOnboardingHistory().provisioningDetails ? this.cpService.getOnboardingHistory().provisioningDetails.description : '';
        this.bprId = this.route.snapshot.queryParams['bprId'] ? this.route.snapshot.queryParams['bprId'] : this.company.bprId;
        this.editMode = false;
        if (this.route.snapshot.queryParamMap.has("gd")) {
            this.globalDirFlag = true;
            if (this.route.snapshot.queryParamMap.has("privateView")) {
                this.availableEDI = this.route.snapshot.queryParams['edi'] == "true" ? true : false;
                this.cpService.setPrivateView(true);
                this.isPrivateView = true;
            } else {
                this.cpService.setPrivateView(false);
            }
        } else {
            this.isPrivateView = false;
            this.globalDirFlag = false;
        }
        this.cpService.setCompany(this.company);
    }
    toggleEditMode() {
        this.companyDisplayNameTempEdit = this.companyDisplayNameEdit;
        this.companyIDEdit = this.company.facadeCompanyId
        if (!this.companyDisplayNameTempEdit)
            this.companyDisplayNameTempEdit = this.company.companyName;
        this.editMode = !this.editMode;
        // this.cpService.setHeaderEdit(this.editMode);
    }
    ngOnDestroy() {
        //  this.cpService.clearHeaderEdit();
    }

    saveData() {
        if (this.bprId) {
            this.companyDisplayNameEdit = this.companyDisplayNameTempEdit;
            this.facadeData.targetBuCustomName = this.companyDisplayNameEdit;
			this.tpService.displayName=this.facadeData.targetBuCustomName;
            this.facadeData.targetBuCustomId = this.companyIDEdit;
            this.facadeData.bprId = this.bprId;
            this.facadeData.targetBuId = this.cpService.buId;
            // Delete the facade record in case both the values are blank
            if (!this.facadeData.targetBuCustomName && !this.facadeData.targetBuCustomId) {
                if (this.facadeData.uniqueId) {
                    this.tpService.deleteFacades(this.facadeData).subscribe(
                        data => {
                            this.facadeData.uniqueId = data['responseDetails']['responseEntity'].uniqueId;
                            this.setDisplayNameDefault();
                        },
                        error => {
                            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
                        }
                    );
                } else {
                    this.setDisplayNameDefault();
                }
                return;
            }

            if (this.facadeData.targetBuCustomName) {
                this.addOrUpdateFacade();
            }
            else if (!this.facadeData.targetBuCustomName && this.facadeData.targetBuCustomId) {
                this.setDisplayNameDefault();
                this.addOrUpdateFacade();
                this.toggleEditMode();
            }
        }
        else {
            this.toggleEditMode();
        }
    }

    displayErrorStatus() {
        this.notificationHandler.notify({ title: this.translate.instant("common.errorDetails"), severity: 'info', details: this.onboardingErrorDetails });
    }

    // Connect | Emerald

    connect() {
        if (!this.availableEDI) {
            this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("tpdir.company.profile.noEDI") });
            return;
        }
        this.showWorkflowModal = false;
        this.onboardingService.listWorkflows().subscribe(data => {
            this.workflowList = data['responseDetails']['responseEntity'];
            if (this.workflowList.length < 1) {
                this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("onboarding.noWorkflows") });
            } else {
                this.showWorkflowModal = true;
            }
        });

    }

    selectedInvCode: string;
    invitationcodes: any = [];

    addOrUpdateFacade() {
        this.tpService.addUpdateFacades(this.facadeData).subscribe(
            data => {
                this.notificationHandler.notify({ severity: 'success', title: this.translate.instant("tpdir.company.profile.updateSuccess") });
                this.facadeData.uniqueId = data['responseDetails']['responseEntity'].uniqueId;
                this.facadeData['edited'] = true;
                this.cpService.setObservableFacadeData(this.facadeData);
                this.company.facadeCompanyId = this.facadeData.targetBuCustomId;
                this.cpService.setCompany(this.company);
                this.toggleEditMode();
            },
            error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            }
        );
    }

    setDisplayNameDefault() {
        this.notificationHandler.notify({ severity: 'success', title: this.translate.instant("tpdir.company.profile.fieldsSet") });
        this.facadeData['edited'] = true;
        this.company.facadeCompanyId = '';
        this.cpService.setCompany(this.company);
        this.facadeData.targetBuCustomName = this.company.companyName;
        this.cpService.setObservableFacadeData(this.facadeData);
        this.toggleEditMode();
    }

    isCancel() {
        this.selectedInvCode = '';
        this.invitationcodes.length = 0;
    }

    setFacadeData() {
        this.facadeData.bprId = this.bprId;
        this.facadeData.targetBuId = this.cpService.buId;
        this.facadeData.targetBuCustomName = this.companyDisplayNameTempEdit;
        this.facadeData.targetBuCustomId = this.companyIDEdit;
        this.facadeData['edited'] = false;
    }

}