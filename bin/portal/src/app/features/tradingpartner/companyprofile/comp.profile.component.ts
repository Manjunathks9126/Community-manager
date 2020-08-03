import { Component, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from '@angular/router';
import { Company } from "./companyProfile.entity";
import { BreadCrumbsService } from "tgocp-ng/dist";
import { Subscription } from "rxjs";
import { BusinessFacades } from "../facades.entity";
import { CompanyProfileService } from "../../../services/company-profile.service";
import { OnboardingService } from '../../../services/onboarding.service';




@Component({
    templateUrl: './comp.profile.component.html'
})

export class CompanyProfileComponent implements OnInit {


    company: Company = null;
    dataFormat: string = "MM/DD/YYYY";
    isActive: boolean = true;
    buId: string;
    companyDisplayName: string;
    companyDisplayNameEdit: string;
    targetBuCustomId: string;
    companyIDEdit: string;
    facadeId: string;
    bprId: string;
    editFacade: boolean;
    privateViewQueryParam = {};
    facadeData: BusinessFacades = new BusinessFacades();
    isPrivateView = false;
    facadeCompanyId: string;
    facadeDataSubscription: Subscription;
    bprStatus: string;
    otherInfoFlag: boolean = false;

    constructor(private cpService: CompanyProfileService, private onboardingService: OnboardingService,
        private route: ActivatedRoute, private breadCrumbService: BreadCrumbsService,
        private router: Router) {
        this.facadeDataSubscription = this.cpService.getObservableFacadeData().subscribe(
            data => {
                this.facadeData = data;
                if (this.facadeData['edited']) {
                    this.companyDisplayName = this.facadeData.targetBuCustomName;
                }
            }
        );
    }
    ediOrDocFlag: boolean = false;
    ngOnInit(): void {
        // this.otherInfoFlag = this.cpService.otherInfo;
        this.ediOrDocFlag = this.cpService.hasEdiOrDocs;
        this.companyDisplayName = this.route.snapshot.queryParams['name'];
        this.buId = this.route.snapshot.params['id'];
        this.cpService.buId = this.buId;
        this.getCompanyById(this.buId);
        this.bprId = this.route.snapshot.queryParams['bprId'];
        this.bprStatus = this.route.snapshot.queryParams['status'];
        let edi = this.route.snapshot.queryParams['edi'];
        this.facadeId = this.route.snapshot.queryParams['facadeId'];
        this.facadeData.uniqueId = this.facadeId;
        this.targetBuCustomId = this.route.snapshot.queryParams['companyId'];
        this.getExtendedAttribute();
        this.cpService.isPrivateView().subscribe(data => {
            if (data) {
                this.isPrivateView = true;
                this.privateViewQueryParam = { 'gd': 't', 'privateView': 'true', "edi": edi };
            } else {
                this.isPrivateView = false;
                this.privateViewQueryParam = { 'gd': 't' };
            }
        });
    }

    private getExtendedAttribute() {
        this.cpService.companyGpdConsent().subscribe(response => {
            if(response.responseDetails.responseEntity == 'Yes'){
                this.cpService.getExtendedAttribute(this.buId).subscribe(data => {
                    this.onboardingService.OTHER_INFO_EW = [];
                    if (data.responseDetails && data.responseDetails.responseEntity) {
                        this.onboardingService.OTHER_INFO_EW = data.responseDetails.responseEntity;
                        if (this.onboardingService.OTHER_INFO_EW.length > 0) {
                            this.otherInfoFlag = true;
                        }
                    }
                }, error => {
                    // do nothing
                })
            }
        }, error => {
            // do nothing
        });

    }

    getCompanyById(companyId: any) {
        this.cpService.companyById(companyId).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    let comp = data['responseDetails']['responseEntity'];
                    // if(comp.companyAddress.countryCode == Constants.Default_Country_Code) comp.companyAddress.countryCode='';
                    this.company = new Company(
                        comp.activityLog ? comp.activityLog.createTimeStamp : '',
                        comp.activityLog ? comp.activityLog.modifyTimeStamp : '',
                        comp.status,
                        comp.companyName,
                        comp.companyId,
                        comp.companyParticipationType,
                        comp.companyAddress.addressLine1,
                        comp.companyAddress.addressLine2,
                        comp.companyAddress.city,
                        comp.companyAddress.countryCode,
                        comp.companyAddress.state,
                        comp.companyAddress.postalCode,
                        comp.companyWebsiteURL,
                        comp.dunsNumber,
                        comp.globalLocationNumber,
                        comp.homeZoneId,
                        comp.customerBUGPDVisibility,
                        comp.internalBUGPDVisibility
                    );
                    this.company.facadeCompanyId = this.targetBuCustomId;
                    this.company.bprId = this.bprId;
                    this.company.bprStatus = this.bprStatus;
                    this.cpService.setCompany(this.company);
                    this.onboardingService.selectedCompanyName = this.company.companyName;
                    this.onboardingService.selectedCompanyName = this.company.companyName;
                    this.onboardingService.loggedBuId = this.cpService.buId;
                    // below hack is to prevent breadcrumb to remove GPD from ROUTE
                    if (this.route.snapshot.queryParams['gd']) {
                        this.breadCrumbService.addBreadCrumbItem({ label: "gpd", url: '/globalDirectory' });
                    }
                    this.breadCrumbService.addBreadCrumbItem({ label: this.company.companyName, url: this.router.url });
                }
            }
        )
    }
}

