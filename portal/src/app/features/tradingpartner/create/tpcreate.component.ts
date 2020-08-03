import { Component, OnInit, ViewChild } from "@angular/core";
import { Router } from '@angular/router';
import { TradingParnterService } from "../../../services/trading-partner.service";
import { NotificationHandler } from "../../../util/exception/notfication.handler";
import { Company } from "./entity/company.entity";
import { Contact } from "./entity/contact.entity";
import { FormDataService } from "./entity/formData.service";
import { TPCreateMsgService } from "../../../services/tp-create.service";
import { NgForm } from "@angular/forms";
import { CompanyProfileService } from "../../../services/company-profile.service";
import { NgConstants } from "tgocp-ng/dist/shared/Ng.constants";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { Workflow } from '../directory/createTPMSG/entity/workflow.entity';
import { Constants } from '../../../services/AppConstant';
import { TPMSGService } from '../../../services/tpmsg.service';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { TranslateService } from '@ngx-translate/core';
import { OnboardingService } from "../../../services/onboarding.service";

@Component({
    selector: 'add-tp',
    templateUrl: "./tpcreate.component.html"
})
export class TradingPartnerCreateComponent implements OnInit {
    company: Company = new Company();
    contact: Contact = new Contact();
    workflow: Workflow = new Workflow();
    countries: any;
    selectedCity: any;
    submitted = false;
    isUnchanged = true;
    isReadonly = false;
    emailregex = NgConstants.emailRegEx;
    scrollConfig = PSscrollUtils.scrollY();
    companyIdRegx = "^[a-zA-Z0-9äöüÄÖÜ ]*$";
    nottificationMsgJson: any;

    @ViewChild("buForm") buForm: NgForm;
    constructor(private cpService: CompanyProfileService, private tpService: TradingParnterService, private createtpMsgService: TPCreateMsgService,
        private router: Router, private formDataService: FormDataService, private tpmsgService: TPMSGService, private dialogueboxService: DialogueboxService
        , private notificationHandler: NotificationHandler, private translate: TranslateService,
        private onbService: OnboardingService) {
    }

    private isNotNull(fieldName): boolean {
        if (this.buForm.controls[fieldName] && this.buForm.controls[fieldName].value && typeof (this.buForm.controls[fieldName].value) === "string" && this.buForm.controls[fieldName].value.trim().length > 0) {
            return true;
        } else if (this.buForm.controls[fieldName] && this.buForm.controls[fieldName].value && typeof (this.buForm.controls[fieldName].value) != "string") {
            return true;
        }
        return false;
    }


    ngOnInit() {
        this.getCountryList();
        this.translate.get("tpdir.create.notificationMsgs").subscribe(res => { this.nottificationMsgJson = res; })
        this.workflow = JSON.parse(JSON.stringify(this.formDataService.getCachedCompanyDetails()));

        this.buForm.valueChanges.subscribe(fields => {
            if (this.isNotNull('cname')) {
                this.isUnchanged = false;
            }
            else {
                this.isUnchanged = true;
            }
            if (this.buForm.controls['country'] && this.buForm.controls['country'].value && (this.buForm.controls['country'].value == 'GB' || this.buForm.controls['country'].value == 'CA' || this.buForm.controls['country'].value == 'US')) {
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
        this.workflow.processingContext.tpIdentifier.countryCode = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode;

        this.workflow.processingContext.tpIdentifier.companyName = this.workflow.provisioningRequestData.registrationData.businessUnit.companyName;
        this.workflow.processingContext.tpIdentifier.city = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.city;
        const bu = this.workflow.provisioningRequestData.registrationData.businessUnit;
        if (this.isReadonly) {
            this.goNext();
        } else {
            this.tpmsgService.validateTP(bu.companyName, bu.companyAddress.city, bu.companyAddress.countryCode).subscribe(data => {
                this.dialogueboxService.confirm({
                    dialogName: 'exisitingTp',
                    accept: () => {
                        this.goNext();
                    }

                });
            },
                () => this.goNext()
            );
        }

    }

    private goNext() {
        this.formDataService.setCachedCompanyDetails(JSON.parse(JSON.stringify(this.workflow)));
        if (this.tpService.selectedTpId == "NA") {
            this.validateTPDetails(this.workflow);
        } else {
            this.router.navigate(['tpdir/base/work']);
        }
    }

    onSave() {
        this.workflow.processingContext.tpIdentifier.countryCode = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode;
        this.workflow.processingContext.tpIdentifier.companyName = this.workflow.provisioningRequestData.registrationData.businessUnit.companyName;
        this.workflow.processingContext.invitationCode = this.tpService.selectedinvCodeSubject;

        if (null != this.tpService.selectedTpId && this.tpService.selectedTpId != "NA") {
            this.tpService.editTP(this.workflow, this.tpService.selectedTpId).subscribe(data => {
                this.router.navigate(['tpdir']);
                if (data['responseDetails']['responseEntity'].statusCode == 200 || data['responseDetails']['responseEntity'].statusCode == 201) {
                    this.showSavemessage(data['responseDetails']['responseEntity'].targetResourceRefId, this.tpService.selectedTpId);
                    this.formDataService.resetFormData();
                    this.formDataService.clearCache();

                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        }
        else {
            const bu = this.workflow.provisioningRequestData.registrationData.businessUnit;
            this.tpmsgService.validateTP(bu.companyName, bu.companyAddress.city, bu.companyAddress.countryCode).subscribe(data => {
                if (data.success) {
                    this.dialogueboxService.confirm({
                        dialogName: 'exisitingTp',
                        accept: () => {
                            this.saveTp();
                        }
                        , reject: () => {
                            this.notificationHandler.notify({ severity: 'info', title: this.nottificationMsgJson['unique'] });
                        }
                    });
                }
            }, () => {
                this.saveTp();
            });
        }

    }

    private saveTp() {
        this.tpService.saveTP(this.workflow, this.onbService.getInvIdForOnboarding()).subscribe(data => {
            this.router.navigate(['tpdir']);
            if (data['responseDetails']['responseEntity'].statusCode == 200 || data['responseDetails']['responseEntity'].statusCode == 201) {
                this.showSavemessage(data['responseDetails']['responseEntity'].targetResourceRefId, this.tpService.selectedTpId);
                this.formDataService.resetFormData();
                this.formDataService.clearCache();
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'warning', title: error.userMessage });
        });
    }



    private getCountryList() {
        this.tpService.getCountries().subscribe(data => {
            if (null != data) {
                this.resetCountryField();
                this.countries = data;
                if (null != this.tpService.selectedTpId && this.tpService.selectedTpId != "NA") {
                    this.getCompanyById(this.tpService.selectedTpId);
                    this.isReadonly = true
                }
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    }

    private resetCountryField() {
        let country = this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode;
        if (country == "[object Object]" || country != null && Object.keys(country).length === 0)
            this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = '';
    }

    validateEdi(data: any) {
        if (this.buForm.controls['companyId'].invalid) return;
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
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyName = comp.companyName;
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.addressLine1 = comp.companyAddress.addressLine1;
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.addressLine2 = comp.companyAddress.addressLine2;
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.city = comp.companyAddress.city;
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.state = comp.companyAddress.state;
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.postalCode = comp.companyAddress.postalCode;

                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = comp.companyAddress.countryCode;//this.countries.filter(element => element.countryCode == comp.companyAddress.countryCode)[0];
                    this.workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.website = comp.companyWebsiteURL;
                    comp.buAdditionalCodes.forEach(e => {
                        if (e.key === "INVITATION_CODE")
                            this.tpService.selectedinvCodeSubject = e.value[0]
                    });
                }
            }
        )
    }

    validateWhiteSpace(fields: any) {
        for (const fName in fields) {
            if (null != this.buForm.controls[fName].value && typeof (this.buForm.controls[fName].value) === "string" && this.buForm.controls[fName].value.trim().length == 0
                && this.buForm.controls[fName].value.length > 0) {
                this.buForm.controls[fName].setErrors({ 'required': true });
            }
        }
    }


    validateTPDetails(data: any) {
        this.tpService.verifyTP(data).subscribe(res => {
            if (null != res.responseDetails.responseEntity) {
                this.router.navigate(['tpdir/base/work']);
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'warning', title: error.userMessage });
        });

    }
    onCancel() {
        this.formDataService.clearCache();
    }
}
