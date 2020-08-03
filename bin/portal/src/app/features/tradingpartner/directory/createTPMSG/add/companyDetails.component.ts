import { Component, ViewChild } from "@angular/core";
import { TPCreateMsgService } from "../../../../../services/tp-create.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { TradingParnterService } from "../../../../../services/trading-partner.service";
import { NgForm } from "@angular/forms";
import { Router, ActivatedRoute } from '@angular/router';
import { TPMSGService } from "../../../../../services/tpmsg.service";
import { Workflow } from "../entity/workflow.entity";
import { NgConstants } from "tgocp-ng/dist/shared/Ng.constants";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { WorkflowTpMsgService } from "../entity/workflowtpmsg.service";
import { STEPS } from "../entity/steps.model";
import { TPREQUESTTYPE } from "../entity/tptype.constants";
import { CompanyProfileService } from '../../../../../services/company-profile.service';
import { TranslateService } from '@ngx-translate/core';
import { TradingParnterDetailsService } from '../../../../../services/trading-partner-details.service';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { Constants } from '../../../../../services/AppConstant';
import { Subscription } from "rxjs";

@Component({
    templateUrl: "./companyDetails.component.html"
})
export class CompanyDetailsComponent {

    workFlow: Workflow = new Workflow();
    isGDFlag: boolean = false;
    newTpFlag: boolean = false;
    countries: any;
    selectedCity: any;
    selectedCountry: any;
    submitted = false;
    isUnchanged = true;
    isReadonly = true;
    ediValid = false;
    gxs_company = "";
    loggedInCompanyId = "";
    companyIdRegx = "^[a-zA-Z0-9äöüÄÖÜ ]*$";
    gdsTpId = "";
    gdsEdi = "";
    nottificationMsgJson: any;
    inputQualifier = '';
    inputAddress = '';
    emailregex = NgConstants.emailRegEx;
    @ViewChild("buForm") buForm: NgForm;
    scrollConfig = PSscrollUtils.scrollY();
    mandatoryFields = ['cname', 'cfname', 'clname', 'cemail', 'cphone'];
    ediData: any;
    companyData: any;

    constructor(private router: Router, private route: ActivatedRoute, private tpmsgService: TPMSGService,
        private ediService: TradingParnterDetailsService, private dialogueboxService: DialogueboxService,
        private createtpMsgService: TPCreateMsgService, private tpService: TradingParnterService, private cpService: CompanyProfileService,
        private notificationHandler: NotificationHandler, private workflowTpmsgService: WorkflowTpMsgService, private translate: TranslateService) {
    }

    ngOnInit() {
        this.translate.get("tpdir.create.notificationMsgs").subscribe(res => { this.nottificationMsgJson = res; })
        this.tpmsgService.activateRoute("CD");
        if (this.router.url.indexOf("tpmsgbase") > 0) {
            this.tpmsgService.setTGMSWorkFlowType(false);
        } else {
            this.tpmsgService.setTGMSWorkFlowType(true);
        }
        this.tpmsgService.getInternalCompanyDetails().subscribe(data => {
            this.gxs_company = data.buName;
            this.tpmsgService.gxsCompany = this.gxs_company;
        });
        this.tpmsgService.getLoggedInCompanyDetails().subscribe(data => {
            if (null != data && data.success) {
                this.loggedInCompanyId = data.responseEntity.companyId;
                this.tpmsgService.loggedinBUID = this.loggedInCompanyId;
            }
        }, error => {

        })
        let isCached = this.route.snapshot.queryParams["prev"];
        this.isGDFlag = this.tpmsgService.isGDFlag = this.cpService.isPrivateViewEnabled;
        this.newTpFlag = this.cpService.setFlag;
        this.checkCached(isCached);
        this.valueChangeListener();

        this.ediData=this.cpService.getEdi();
        this.companyData=this.cpService.getCompanyName();
        this.buForm.value.cname=this.companyData;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyName=this.buForm.value.cname;

        this.buForm.value.qualifier=this.cpService.getEdi();
        if(this.buForm.value.qualifier){
        var separateText = (this.buForm.value.qualifier).split(":");
        console.log(separateText)
        this.inputQualifier=separateText[0];
        this.inputAddress=separateText[1];
        }


        if(!this.cpService.getNoRecordGpdFlag()){
            this.workFlow.provisioningRequestData.registrationData.businessUnit.companyName="";
            this.inputAddress="";
            this.inputQualifier="";
        }

    }
    private fetchEdiDetails(edi) {
        if (edi && edi.length > 0) {
            if (edi.indexOf(":") >= 0) {
                let address = edi.split(":");
                this.inputQualifier = address[0];
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier = address[0];
                this.inputAddress = address[1];
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = address[1];
            } else {
                this.inputAddress = edi;
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = edi;
            }
            this.verifyEdi();
        }
    }

    ediOptions = [];
    selectedEDI: any;
    private getEdiListForDropDown(buId) {
        if (buId.length > 0) {
            this.ediService.getEdiAddressList(buId).subscribe(data => {
                if (data.responseDetails.success) {
                    this.ediOptions = [];
                    data.responseDetails.responseEntity.forEach(entry => {
                        let option = { "label": "", "value": "" };
                        option.value = entry;
                        option.label = entry;
                        this.ediOptions.push(option);
                    });

                    if (this.ediOptions.length == 1) {
                        this.selectedEDI = this.ediOptions[0].value;
                        this.fetchEdiDetails(this.selectedEDI);
                    }
                    if (this.ediOptions.length > 0) {
                        // this.selectedEDI = this.ediOptions[0];
                        this.tpmsgService.EDI_ARRAY = [...this.ediOptions];
                        // this.onEdiSelect();
                    }
                }
            }, error => {
                console.log(error);
            })
        }
    }

    private assignEdiTowrkflow() {
        if (typeof this.selectedEDI == 'object') {
            if (this.selectedEDI.label && this.selectedEDI.label.indexOf(":") >= 0) {
                let address = this.selectedEDI.label.split(":");
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier = address[0];
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = address[1];
            } else {
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = this.selectedEDI.label;
            }
        } else {
            if (this.selectedEDI.indexOf(":") >= 0) {
                let address = this.selectedEDI.split(":");
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier = address[0];
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = address[1];
            } else {
                this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = this.selectedEDI;
            }
        }
    }


    onEdiSelect() {
        if (this.workFlow.provisioningRequestData.registrationData.businessUnit.companyName.length == 0) {
            this.fetchEdiDetails(this.selectedEDI.value);
        }
        this.assignEdiTowrkflow();

    }

    private checkCached(isCached) {
        if (!isCached) {
            this.getCountries();
            this.selectedEDI = this.cpService.selectedEdi;
            this.fetchEdiDetails(this.selectedEDI);
            this.getEdiListForDropDown(this.cpService.selectedBuId);
        } else {
            this.workFlow = this.tpmsgService.getCachedWorkFlow();
            console.log(this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode);
            this.inputQualifier = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier;
            this.inputAddress = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address;
            this.countries = this.tpmsgService.getCachedCountryList();
            console.log(this.countries);
            this.isReadonly = this.tpmsgService.getIsReadOnly();
            this.ediOptions = this.tpmsgService.EDI_ARRAY;
            this.selectedEDI = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier + ":" + this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address;

            if (!this.countries) {
                this.getCountries();
            }
        }
    }

    private valueChangeListener() {
        this.buForm.valueChanges.subscribe(fields => {
            if (this.buForm.controls['address'] != undefined && this.buForm.controls['address'].value != null && this.buForm.controls['address'].value.trim().length > 0) {
                this.ediValid = true;
            } else {
                this.ediValid = false;
            }

            if (this.buForm.controls['address'] != undefined && this.buForm.controls['qualifier'] != undefined) {
                if (this.buForm.controls['qualifier'].value != null && this.buForm.controls['address'].value != null) {
                    if (this.buForm.controls['address'].value.length > 0 || this.buForm.controls['qualifier'].value.length > 0) {
                        if ((this.buForm.controls['address'].value.length + this.buForm.controls['qualifier'].value.length) > 34) {
                            this.buForm.controls['address'].setErrors({ 'maxLength': true });
                        } else {
                            this.buForm.controls['address'].setErrors(null);
                        }
                    }
                }
            }

            this.validateState();
            this.validateWhiteSpace(fields);
        })
    }

    private trimSpace() {
        Object.keys(this.buForm.controls).forEach(fieldName => {
            if (this.buForm.controls[fieldName].value != null && this.buForm.controls[fieldName] != undefined
                && typeof (this.buForm.controls[fieldName].value) === "string"
                && this.buForm.controls[fieldName].value.length != this.buForm.controls[fieldName].value.trim().length) {
                this.buForm.controls[fieldName].setValue(this.buForm.controls[fieldName].value.trim());
            }
        });
    }

    validateWhiteSpace(fields: any) {
        for (const fName in fields) {
            if (this.mandatoryFields.indexOf(fName) > -1 && null != this.buForm.controls[fName].value && typeof (this.buForm.controls[fName].value) === "string" && this.buForm.controls[fName].value.trim().length == 0
                && this.buForm.controls[fName].value.length > 0) {
                this.buForm.controls[fName].setErrors({ 'required': true });
            }
        }
    }

    private validateState() {
        if (this.buForm.controls['country'] != undefined && this.buForm.controls['country'].value != null) {
            if (this.buForm.controls['country'].value.countryCode == "US" || this.buForm.controls['country'].value.countryCode == "GB" || this.buForm.controls['country'].value.countryCode == "CA") {
                if (this.buForm.controls['postalcode'] != undefined && this.buForm.controls['postalcode'].value != null && this.buForm.controls['postalcode'].value.trim().length == 0) {
                    this.buForm.controls['postalcode'].setErrors({ 'exists': true });
                }
                if (this.buForm.controls['state'] != undefined && this.buForm.controls['state'].value != null && this.buForm.controls['state'].value.trim().length == 0) {
                    this.buForm.controls['state'].setErrors({ 'exists': true });
                }
            } else {
                if (this.buForm.controls['state'] != undefined) {
                    this.buForm.controls['state'].setErrors(null);
                }
                if (this.buForm.controls['postalcode'] != undefined) {
                    this.buForm.controls['postalcode'].setErrors(null);
                }
            }
        }
    }
    private getCountries() {
        this.tpService.getCountries().subscribe(data => {
            this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = undefined;
            this.countries = data;
        })
    }

    onCountryChange() {
        if (typeof (this.buForm.controls['country'].value) === "string" && !this.buForm.controls['country'].pristine) {
            this.buForm.controls['country'].setErrors({ 'required': true });
            this.isUnchanged = true;
        }
    }

    private ediBelongsToSameHUB(ediBuid): boolean {
        if (this.loggedInCompanyId == ediBuid) {
            this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("tpmsg.legends.ediSameCompany") });
            this.resetForm();
            return true;
        }
        return false;
    }
    verifyEdi() {
        this.inputQualifier = this.inputQualifier.toUpperCase();
        this.inputAddress = this.inputAddress.toUpperCase();
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier = this.inputQualifier;
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = this.inputAddress;
        let ediAddress = (this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier) + ":" + (this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address);
        //console.log(ediAddress.toUpperCase());
        this.createtpMsgService.verifyEdiInfo(ediAddress.toUpperCase()).subscribe(
            data => {
                this.isReadonly = true;
                if (data['responseDetails']['success']) {
                    let ediData = data['responseDetails']['responseEntity'];
                    if (this.ediBelongsToSameHUB(ediData.companyId)) {
                        return;
                    }
                    //this.mapReponseToCompany(ediData);

                    if (ediData.vanProvider && ediData.vanProvider.companyName != this.gxs_company)
                        this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.exist_interconnect_tp;
                    else if (ediData.vanProvider && ediData.vanProvider.companyName == this.gxs_company)
                        this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.exist_customer_tp;

                    this.createtpMsgService.retriveCompanyDetails(ediData.companyId).subscribe(
                        response => {
                            if (response.responseDetails.success) {
                                this.mapReponseToCompany(response.responseDetails.responseEntity);
                            }
                        }
                    )
                    this.tpmsgService.clearCache();
                    if (ediData.vanProvider && ediData.vanProvider.companyName) {
                        this.tpmsgService.setVanProvider(ediData.vanProvider.companyName);
                        this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanMailboxId = ediData.mailboxId;
                    } else {
                        this.tpmsgService.setVanProvider(null);
                    }

                    //  this.tpmsgService.setVanProvider(ediData.vanProvider.companyName);
                }

            }, () => {
                this.tpmsgService.clearCache();
                this.resetForm();
                this.isReadonly = false;
                this.tpmsgService.setVanProvider(null);
                this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.new_ic_edi;
                this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("tpmsg.legends.ediNotExists") });
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


    nextStep() {
        this.trimSpace();
        this.validateTp();
    }

    private validateTp() {
        let bu = this.workFlow.provisioningRequestData.registrationData.businessUnit;
        let address = bu.companyAddress;
        //clearing company van cache
        this.tpmsgService.setCompanyIdVanProviderMap('', '');
        if (this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.new_ic_edi ||
            this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_ic_edi ||
            this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_service_provider_edi) {
            this.tpmsgService.validateTP(bu.companyName, address.city, address.countryCode ? address.countryCode.countryCode : '').subscribe(data => {
                if (data.responseEntity && data.responseEntity.length == 1 && data.responseEntity[0].status.toUpperCase() == 'ACTIVE') {
                    //  this.dialogueboxService.confirm({
                    //    dialogName: 'exisitingTp',
                    //  accept: () => {
                    this.navigateToNextPage();
                    this.cacheCompanyIdVanProviderMap(data.responseEntity[0].companyId);

                    /*       }
                          , reject: () => { 
                              this.notificationHandler.notify({ severity: 'info', title: this.nottificationMsgJson['unique'] });
                          } */
                    //  });
                } else if (data.responseEntity && data.responseEntity.length > 1) {
                    this.dialogueboxService.confirm({
                        dialogName: 'exisitingTp',
                        accept: () => {
                            this.navigateToNextPage();

                        }
                        , reject: () => {
                            this.notificationHandler.notify({ severity: 'info', title: this.nottificationMsgJson['unique'] });
                        }
                    });
                }
                else {
                    this.navigateToNextPage();
                }
            }, () => {
                this.navigateToNextPage();
            });
        }
        else if (this.workFlow.provisioningRequestData.tpRequestType != TPREQUESTTYPE.exist_interconnect_tp &&
            this.workFlow.provisioningRequestData.tpRequestType != TPREQUESTTYPE.exist_customer_tp &&
            this.workFlow.provisioningRequestData.tpRequestType != TPREQUESTTYPE.exist_service_provider_tp) {

            this.tpmsgService.validateTP(bu.companyName, address.city, address.countryCode ? address.countryCode.countryCode : '').subscribe(data => {
                if (data.success) {
                    this.dialogueboxService.confirm({
                        dialogName: 'exisitingTp',
                        accept: () => {
                            this.navigateToNextPage();
                            this.notificationHandler.notify({ severity: 'warning', title: this.nottificationMsgJson['unique'] });
                        }
                        , reject: () => {
                            this.notificationHandler.notify({ severity: 'info', title: this.nottificationMsgJson['unique'] });
                        }
                    });
                }
            }, () => {
                this.navigateToNextPage();
            });
        } else {
            this.validateBPR();
        }
    }
    cacheCompanyIdVanProviderMap(companyId: string) {
        this.tpmsgService.getCompanyVan(companyId, true).subscribe(
            data => this.tpmsgService.setCompanyIdVanProviderMap(companyId, data.responseDetails.responseEntity)
        );
    }

    private validateBPR() {
        let bu = this.workFlow.provisioningRequestData.registrationData.businessUnit;
        if (this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_interconnect_tp
            || this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_customer_tp || this.workFlow.provisioningRequestData.tpRequestType == TPREQUESTTYPE.exist_service_provider_tp) {
            this.tpmsgService.validateBPR(bu.buId).subscribe(res => {
                if (res.success) {
                    this.navigateToNextPage();
                }
            }, () => {
                //this.showBprValidationMessage(bu.companyName);
                this.navigateToNextPage();
            })
        } else {
            this.navigateToNextPage();
        }
    }


    private navigateToNextPage() {

        if (typeof this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode === "string" && this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode.length > 0) {
            this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = this.countries.filter(item => item.countryDescription == this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode)[0];
        }

        this.workflowTpmsgService.validateStep(STEPS.add);
        this.tpmsgService.holdCompanyDataTemp(this.workFlow, this.countries, this.isReadonly);
        if (this.tpmsgService.isTGMSworkflow()) {
            this.router.navigate(['/tpdir/tgmsbase/billingSplit'], {skipLocationChange: true});
        } else {
            this.router.navigate(['/tpdir/tpmsgbase/msgDetails'], {skipLocationChange: true});
        }
    }

    private mapReponseToCompany(ediData: any) {
        this.resetForm();
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.city = ediData.companyAddress.city;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyName = ediData.companyName;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = this.getCountryObject(ediData.companyAddress.countryCode);
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.addressLine1 = ediData.companyAddress.addressLine1;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.addressLine2 = ediData.companyAddress.addressLine2;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.website = ediData.companyWebsiteURL;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.state = ediData.companyAddress.state;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.postalCode = ediData.companyAddress.postalCode;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.participationType = ediData.companyParticipationType;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.buId = ediData.companyId;

        if (ediData.companyParticipationType === "VAN PROVIDER") {
            this.resetForm();
            this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.exist_ic_edi;
            this.isReadonly = false;
            this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("tpmsg.legends.ediVan").replace("{0}", ediData.companyName) });
        } else if (ediData.companyParticipationType === "SERVICE_PROVIDER") {
            this.resetForm();
            this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.exist_service_provider_edi;
            this.workFlow.provisioningRequestData.registrationData.businessUnit.participationType = "SERVICE_PROVIDER_TP";
            this.isReadonly = false;
            this.notificationHandler.notify({ severity: 'info', title: this.translate.instant("tpmsg.legends.ediSP").replace("{0}", ediData.companyName) });
        } else if (ediData.companyParticipationType === "SERVICE_PROVIDER_TP") {
            //this.resetForm();
            this.workFlow.provisioningRequestData.tpRequestType = TPREQUESTTYPE.exist_service_provider_tp;
            //this.isReadonly = false;
            //this.notificationHandler.notify({ severity: 'info', title: 'EDI belongs to service provider tp ' + ediData.companyName + '. Proceed to create new Trading Partner.' });
        } else {
            this.isReadonly = true;
        }
    }

    private getCountryObject(countryCode) {
        let countryObj = [];
        if (countryCode != null && countryCode != '') {
            this.countries.forEach(element => {
                if (element.countryCode == countryCode) {
                    countryObj = element;
                }
            });
        }
        return countryObj;
    }

    private resetForm() {
        let qualifier = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier;
        let address = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address;
        let mailBoxID = this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanMailboxId;
        let tpRequestType = this.workFlow.provisioningRequestData.tpRequestType;
        this.workFlow = new Workflow();
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier = qualifier;
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address = address;
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanMailboxId = mailBoxID;
        this.workFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = "";
        this.workFlow.provisioningRequestData.tpRequestType = tpRequestType;
    }

    validateForm() {
        if (this.buForm.valid && this.buForm.controls['cname'] != undefined
            && this.buForm.controls['cname'].value != null
            && this.buForm.controls['cname'].value != "") {
            return true;
        }
        return false;
    }

    cancel() {
       
        this.dialogueboxService.confirm({
            dialogName: 'cancelSetup',
            accept: () => {
                let navigationExtras = {
                    queryParams: { gd: 't' },
                    skipLocationChange: true
                }
                if (this.isGDFlag) {
                    this.router.navigate(['/globalDirectory'], navigationExtras);
                }
                else if(this.newTpFlag){
                    this.router.navigate(['/globalDirectory'], navigationExtras);

                } else {
                    this.router.navigate(['/tpdir']);
                }
            }
            , reject: () => {
            }
        });
    }

    showBprValidationMessage(buName) {
        let message = {
            "title": this.translate.instant("tpmsg.legends.bprExists").replace("{0}", buName),
            "severity": "error",
            "userMessage": ""
        }
        this.notificationHandler.notify(message);
    }
}