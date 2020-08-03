import { Component, OnInit, ViewChild } from "@angular/core";

import { Router } from '@angular/router';
import { TradingParnterService } from "../../../../services/trading-partner.service";
import { NotificationHandler } from "../../../../util/exception/notfication.handler";
import { Company } from "../entity/company.entity";
import { Contact } from "../entity/contact.entity";
import { FormDataService } from "../entity/formData.service";
import { TPCreateMsgService } from "../../../../services/tp-create.service";
import { NgForm } from "@angular/forms";
import { CompanyProfileService } from "../../../../services/company-profile.service";
import { NgConstants, PSscrollUtils } from "tgocp-ng/dist";
import { Workflow } from "../../directory/createTPMSG/entity/workflow.entity";
import { OnboardingService } from "../../../../services/onboarding.service";
import { Constants } from '../../../../services/AppConstant';


@Component({
    selector: 'add-tp',
    templateUrl: "./tp-core.component.html"
})
export class TradingPartnerCoreComponent implements OnInit {
    company: Company = new Company();
    workflow: Workflow = new Workflow();
    contact: Contact = new Contact();
    countries: any;
    selectedCity: any;
    selectedCountry: string;
    submitted = false;
    isUnchanged = true;
    isReadonly = false;
    emailregex = NgConstants.emailRegEx;
    scrollConfig = PSscrollUtils.scrollY();
    test: boolean = true;
    companyIdRegx = "^[a-zA-Z0-9äöüÄÖÜ ]*$";
    @ViewChild("buForm") buForm: NgForm;
    constructor(private notficationHandler: NotificationHandler, private cpService: CompanyProfileService, private tpService: TradingParnterService, private createtpMsgService: TPCreateMsgService,
        private router: Router, private formDataService: FormDataService, private onbaordingService: OnboardingService
        , private notificationHandler: NotificationHandler) {
    }
    ngOnInit() {
        this.getCountryList();
        this.company = this.formDataService.getcompanyDetails();
        if (null != this.company.contact && typeof (this.company.contact) != 'undefined')
            this.contact = this.company.contact;
        this.buForm.valueChanges.subscribe(fields => {
            if (this.buForm.controls['cname'] && this.buForm.controls['cname'].value.trim().length > 0 &&
                this.buForm.controls['addr1'] && this.buForm.controls['addr1'].value.trim().length > 0 &&
                this.buForm.controls['displayName'] && this.buForm.controls['displayName'].value.trim().length > 0 &&
                this.buForm.controls['city'] && this.buForm.controls['city'].value.trim().length > 0 &&
                (this.buForm.controls['country'] && (this.buForm.controls['country'].status == 'VALID' || this.buForm.controls['country'].status == 'DISABLED'))) {
                this.isUnchanged = false;
            }
            else {
                this.isUnchanged = true;
            }
            if (this.buForm.controls['country'] && this.buForm.controls['country'].value && (this.buForm.controls['country'].value.countryCode == 'GB' || this.buForm.controls['country'].value.countryCode == 'CA' || this.buForm.controls['country'].value.countryCode == 'US')) {
                if (this.buForm.controls['state'] && null !== this.buForm.controls['state'].value && (this.buForm.controls['state'].value == "" || this.buForm.controls['state'].value.trim().length < 0)) {
                    this.buForm.controls['state'].setErrors({ 'exists': true });
                    this.isUnchanged = true;
                } else {
                    if (this.buForm.controls['state'])
                        this.buForm.controls['state'].setErrors(null);
                }
                if (this.buForm.controls['postalcode'] && null !== this.buForm.controls['postalcode'].value && (this.buForm.controls['postalcode'].value == "" || this.buForm.controls['postalcode'].value.trim().length < 0)) {
                    this.buForm.controls['postalcode'].setErrors({ 'exists': true });
                    this.isUnchanged = true;
                } else {
                    if (this.buForm.controls['postalcode'])
                        this.buForm.controls['postalcode'].setErrors(null);
                }
            }
            else {
                if (this.buForm.controls['state'])
                    this.buForm.controls['state'].setErrors(null);
                if (this.buForm.controls['postalcode'])
                    this.buForm.controls['postalcode'].setErrors(null);
            }
            this.validateWhiteSpace(fields);
        });

        if (null != this.tpService.selectedTpId && this.tpService.selectedTpId != "NA") {
            this.isReadonly = true;
        }

    }

    onSubmit() {

        this.submitted = true;
        if (this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode) {
            this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode.countryCode;
        } else {
            this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = '';
        }
        this.workflow.processingContext.invitationSource = "TGOCP";
        this.workflow.processingContext.tpIdentifier.countryCode = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode;
        this.workflow.processingContext.tpIdentifier.companyName = this.workflow.provisioningRequestData.registrationData.businessUnit.companyName;
        this.workflow.processingContext.tpIdentifier.city = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.city;

        this.tpService.createCoreTP(this.workflow, this.onbaordingService.getInvIdForOnboarding()).subscribe(data => {
            let workflowId = data['responseDetails']['responseEntity'].targetResourceRefId;
            this.showmessage(workflowId);
            this.router.navigate(['tpdir']);
        }, error => {
            this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
        });

    }

    private getCountryList() {
        this.tpService.getCountries().subscribe(data => {
            if (null != data) {
                this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = undefined;
                this.countries = data;
                if (null != this.tpService.selectedTpId && this.tpService.selectedTpId != "NA")
                    this.getCompanyById(this.tpService.selectedTpId);
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    }

    validateEdi(data: any) {
        if (data.model != "" && data.model.trim().length > 0) {
            this.createtpMsgService.verifyEdiInfo(data.model.trim().toUpperCase()).subscribe(
                data => {
                    if (data['responseDetails']['success']) {
                        this.buForm.controls['companyId'].setErrors({ 'exists': true });
                    }
                    else {
                        this.buForm.controls['companyId'].setErrors(null);
                    }

                }, () => {
                    this.buForm.controls['companyId'].setErrors(null);
                });
        }
    }

    showSavemessage(tpId: any, flow: string) {
        let title = "";
        if (null != flow && flow == "NA") {
            title = "Trading Partner successfully created";
        }
        else {
            title = "Trading Partner successfully updated";
        }
        let message = {
            "title": title,
            "severity": "success",
            "userMessage": "Trading Partner Id: " + tpId
        }
        this.notificationHandler.notify(message);
    }

    getCompanyById(companyId: any) {
        this.cpService.companyById(companyId).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    let comp = data['responseDetails']['responseEntity'];
                    //if(comp.companyAddress.countryCode == Constants.Default_Country_Code) comp.companyAddress.countryCode='';
                    this.company.companyName = comp.companyName;
                    this.company.displayName = comp.companyName;
                    this.company.addressLine1 = comp.companyAddress.addressLine1;
                    this.company.addressLine2 = comp.companyAddress.addressLine2;
                    this.company.city = comp.companyAddress.city;
                    this.company.state = comp.companyAddress.state;
                    this.company.postalcode = comp.companyAddress.postalCode;
                    //    this.company.country=comp.companyAddress.countryCode;
                    this.company.country = this.countries.filter(country => country.countryCode == comp.companyAddress.countryCode)[0];
                    // let c = countryDescription[0].countryDescription;
                    // this.buForm.controls['country'].setValue(selectedVal, { onlySelf: true });
                    this.company.companyWebsiteURL = comp.companyWebsiteURL;
                    comp.buAdditionalCodes.forEach(e => {
                        if (e.key === "INVITATION_CODE")
                            this.tpService.selectedinvCodeSubject = e.value[0]
                    });
                }
            }
        )
    }

    onCountryChange() {
        if (typeof (this.buForm.controls['country'].value) === "string" && !this.buForm.controls['country'].pristine) {
            this.buForm.controls['country'].setErrors({ 'required': true });
            this.isUnchanged = true;
        }
    }

    validateWhiteSpace(fields: any) {
        for (const fName in fields) {
            if (null != this.buForm.controls[fName].value && typeof (this.buForm.controls[fName].value) === "string" && this.buForm.controls[fName].value.trim().length == 0
                && this.buForm.controls[fName].value.length > 0) {
                this.buForm.controls[fName].setErrors({ 'required': true });
            }
        }
    }

    showmessage(data: string) {
        let message = {
            "title": "Trading partner successfully invited",
            "severity": "success",
            "userMessage": "WorkFlow id : " + data
        }
        this.notficationHandler.notify(message);
    }
}