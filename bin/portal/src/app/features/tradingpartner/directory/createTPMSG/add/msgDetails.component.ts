import { Component, ViewChild, ElementRef } from "@angular/core";
import { TPCreateMsgService } from "../../../../../services/tp-create.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { TPMSGService } from "../../../../../services/tpmsg.service";
import { Workflow } from "../entity/workflow.entity";
import { Router } from "@angular/router";
import { ProvisioningMap } from "../entity/provisioningMap.entity";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { WorkflowTpMsgService } from "../entity/workflowtpmsg.service";
import { STEPS } from "../entity/steps.model";
import { NgForm } from "@angular/forms";
import { TranslateService } from '@ngx-translate/core';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';

@Component({
    templateUrl: "./msgDetails.component.html"
})
export class MsgDetailsComponent {

    // isSeletedMapEmpty:boolean=true;
    mapSelected: boolean = false;
    workFlow: Workflow;
    isFilterApplied: boolean = false;
    customerTestISAindicator: boolean = false;
    tpTestISAindicator: boolean = false;
    functionalAck: boolean = false;
    sel_prod_address: any = "";
    sel_test_address: any = "";
    provisioningMap: ProvisioningMap[] = [];
    selectedMaps: ProvisioningMap[] = [];
    tempSelectedMaps: ProvisioningMap[] = [];

    vanProvidersList: any = [];
    sub_elem_separator: any = [];
    elem_separator: any = [];
    seg_terminator: any = [];
    your_prod_addrs: any = [];
    loaderFlag: boolean = true;
    after = 0;
    showMapFlag: boolean = true;
    tempSelectedVan = "";
    scrollConfig = PSscrollUtils.scrollY();
    vanDisableFlag = false;
    showSetup: boolean = true;
    isGDFlag: boolean = false;
    @ViewChild("msgForm") msgForm: NgForm;
    @ViewChild("tpdt") tpdt: any;
    currentLanguage: string = "en";
    constructor(private workflowTpmsgService: WorkflowTpMsgService, private router: Router,private dialogueboxService: DialogueboxService,
        private tpmsgService: TPMSGService, private createtpMsgService: TPCreateMsgService,
        private notificationHandler: NotificationHandler, private translate: TranslateService) {

    }

    ngOnInit() {
        this.currentLanguage=this.translate.currentLang;
        this.tpmsgService.activateRoute("MD");
        this.isGDFlag = this.tpmsgService.isGDFlag;
        this.workFlow = this.tpmsgService.getCachedWorkFlow();
        this.mapSelected = this.tpmsgService.getSelectedMapFlag();
        this.selectedMaps = this.tpmsgService.getSelectedProvisioningMap();
        this.tempSelectedMaps = this.removeTableEntriesFromArray(this.tpmsgService.getSelectedProvisioningMap);
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.ediStadard = "ANSI ASC X12";
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.connectivityType = "VAN";
        this.getSeparator();
        this.getProdAddress();
        this.provisioningMap.length = 0;
        this.getProvisioningMaps();
        this.setShowSetupFlag();
        this.subscribeToStep();
        this.setVanProvider();
        this.getCachedData();
        this.attachValueChangeListener();
    }

    private getProvisioningMaps() {
        this.provisioningMap.length = 0;
        this.tpmsgService.getProvisioningMapsRest().subscribe(data => {

            let map = data[this.tpmsgService.loggedinBUID];
            let mapNameArray = [];
            let mapDoctypeArray = [];
            let mapDirection = [];
            let mapDocStndrd = [];
            let mapDocVersion = [];
            if (map == null) {
                map = data["COMMON"];
                mapNameArray = map.mapName.split(',');
                mapDoctypeArray = map.docType.split(',');
                mapDirection = map.direction.split(',');
                mapDocStndrd = map.documentStandard.split(',');
                mapDocVersion = map.docVersion.split(',');
            } else {
                mapNameArray = map.mapName.split(',');
                mapDoctypeArray = map.docType.split(',');
                mapDirection = map.direction.split(',');
                mapDocStndrd = map.documentStandard.split(',');
                mapDocVersion = map.docVersion.split(',');
            }

            for (let i = 0; i < mapNameArray.length; i++) {
                this.provisioningMap.push(new ProvisioningMap(mapNameArray[i], mapDoctypeArray[i], mapDirection[i], mapDocStndrd[i], mapDocVersion[i], "", "", "", "", ""));
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: this.translate.instant("tpmsg.legends.mapsError") });
        });
    }

    private setShowSetupFlag() {
        this.tpmsgService.isTestOptionVisible().subscribe(data => {
            if (!data.showTest) {
                this.showSetup = false;
                this.workFlow.provisioningRequestData.setup = "FINAL";
            } else {
                this.showSetup = true;
            }
        });
    }

    private subscribeToStep() {
        this.tpmsgService.moveToStepSUB.subscribe(data => {
            if (data == "summary" && this.workflowTpmsgService.isStepValid(STEPS.messagedetails)) {
                this.next();
            } else if (data == "add") {
                this.previous();
            }
        });
    }

    private setVanProvider() {
        if (this.tpmsgService.getVanProvider()) {
            this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanProvider = this.tpmsgService.getVanProvider();
            this.vanDisableFlag = true;
        } else {
            this.vanDisableFlag = false;
            this.getVanProvider();
        }
    }

    private attachValueChangeListener() {
        this.msgForm.valueChanges.subscribe(data => {
            if (data.vanProvider && data.vanProvider != this.tempSelectedVan) {
                if (this.vanProviderDATA && this.vanProviderDATA.length > 0) {
                    const van = this.vanProviderDATA.find(element => data.vanProvider == element.vanProvider.companyName);
                    this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanMailboxId = van.mailboxId;
                }
            }

            this.tempSelectedVan = data.vanProvider;

            if (this.msgForm.controls['mapSelected']) {
                if (!this.msgForm.controls['mapSelected'].value) {
                    this.msgForm.controls['mapSelected'].setErrors({ "required": "true" });
                } else {
                    this.msgForm.controls['mapSelected'].setErrors(null);
                }
            }
            this.validateWhiteSpace(data);
        })
    }

    trimTableEntries() {
        for (let field in this.msgForm.controls) {
            if (field.startsWith("acr") || field.startsWith("tableEntries")) {
                this.msgForm.controls[field].setValue(this.msgForm.controls[field].value.trim());
            }
        }
    }

    validateWhiteSpace(fields: any) {
        for (const fName in fields) {
            if (null != this.msgForm.controls[fName].value && typeof (this.msgForm.controls[fName].value) === "string" && this.msgForm.controls[fName].value.trim().length == 0
                && this.msgForm.controls[fName].value.length > 0) {
                this.msgForm.controls[fName].setErrors({ 'required': true });
            }
        }
    }
    private trimSpace() {
        Object.keys(this.msgForm.controls).forEach(fieldName => {
            if (this.msgForm.controls[fieldName].value != null && this.msgForm.controls[fieldName] != undefined
                && typeof (this.msgForm.controls[fieldName].value) === "string"
                && this.msgForm.controls[fieldName].value.length != this.msgForm.controls[fieldName].value.trim().length) {
                this.msgForm.controls[fieldName].setValue(this.msgForm.controls[fieldName].value.trim());
            }
        });
    }

    checkMapSize(): boolean {
        if (this.tempSelectedMaps.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    private getProdAddress() {
        this.loaderFlag = false;
        this.tpmsgService.getEDIList(this.after).subscribe(data => {
            if (data['responseDetails']['success']) {
                data['responseDetails']['responseEntity'].forEach(element => {
                    let qual = !element.tradingAddress.qualifier ? "" : element.tradingAddress.qualifier;
                    let edi = qual + ":" + element.tradingAddress.address;
                    this.your_prod_addrs.push({ "label": edi, "value": edi });
                });
                this.loaderFlag = true;
                this.after += 20;
            }
        }, () => {
            this.loaderFlag = true;
        })
    }


    private getCachedData() {
        if (this.tpmsgService.get_Step_2_ProdAdd() && this.tpmsgService.get_Step_2_ProdAdd().length > 0) {
            this.sel_prod_address = this.tpmsgService.get_Step_2_ProdAdd();
        }
        if (this.tpmsgService.get_sel_test_address() && this.tpmsgService.get_sel_test_address().length > 0) {
            this.sel_test_address = this.tpmsgService.get_sel_test_address();
        }
        this.functionalAck = this.tpmsgService.getfunctionalAck();
        this.tpTestISAindicator = this.tpmsgService.getTpTestISAindicator();
        this.customerTestISAindicator = this.tpmsgService.getCustTestISAindicator();
    }

    removeTableEntriesFromArray(data) {
        if (data && data.length > 0) {
            data.forEach(element => {
                element.edi_dc40_prod = "";
                element.edi_dc40_test = "";
                element.tableEntries = "";
                element.acr = "";
            });
        }
        return data;
    }

    totalRecords = "100";
    showMap(type) {
        //console.log(this.tpdt);
        if (type == "cancel") {
            this.tempSelectedMaps = [];
        } else if (type == "open") {
            // let data = this.removeTableEntriesFromArray(this.selectedMaps);
            this.tempSelectedMaps = [...this.selectedMaps];// = data;
        }
        else {
            let data2 = this.tempSelectedMaps;
            this.selectedMaps = data2;
        }
        if (this.selectedMaps.length > 0) {
            this.mapSelected = true;
        } else {
            this.mapSelected = false;
        }
        this.showMapFlag = !this.showMapFlag;
        this.tpmsgService.hideStepper(!this.showMapFlag);
    }
    onScrollToEnd(event) {
        this.getProdAddress();
    }
    private getSeparator() {
        this.tpmsgService.getSeparator().subscribe(data => {
            if (data['responseDetails']['success']) {
                data['responseDetails']['responseEntity'].sub_elem_separator.forEach(element => {
                    this.sub_elem_separator.push({ "label": element, "value": element });
                });
                data['responseDetails']['responseEntity'].elem_separator.forEach(element => {
                    this.elem_separator.push({ "label": element, "value": element });
                });
                data['responseDetails']['responseEntity'].seg_terminator.forEach(element => {
                    this.seg_terminator.push({ "label": element, "value": element });
                });
            }
        })
    }


    copyProdDetails() {
        if (this.workFlow.provisioningRequestData.registrationData.tradingAddress.testTpAsProdTp) {
            let yourGsid = this.workFlow.provisioningRequestData.customer.tradingAddresses.prod.gsId
            let tpProdQualifier = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier;
            let tpProdAddress = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address;
            let tpProdGsID = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.gsId;

            this.workFlow.provisioningRequestData.registrationData.tradingAddress.testtp.qualifier = tpProdQualifier;
            this.workFlow.provisioningRequestData.registrationData.tradingAddress.testtp.address = tpProdAddress;
            this.workFlow.provisioningRequestData.registrationData.tradingAddress.testtp.gsId = tpProdGsID;
            this.workFlow.provisioningRequestData.customer.tradingAddresses.test.gsId = yourGsid;
            let selectedEDi = this.sel_prod_address;
            this.sel_test_address = selectedEDi;
        }
    }

    test() {
        if (this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator) {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator = "Y";
        } else {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator = "N";
        }
        // console.log(this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator);
    }


    sortVanProviderArray(array) {
        array.sort((a, b) => {
            if (a.vanProvider.companyName < b.vanProvider.companyName) return -1;
            else if (a.vanProvider.companyName > b.vanProvider.companyName) return 1;
            else return 0;
        });
    }

    vanProviderDATA: any = [];
    private getVanProvider() {
        this.createtpMsgService.getVanProvidersList().subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.vanProviderDATA = data['responseDetails']['responseEntity'].vanProviderDetail;
                    let vanArray = data['responseDetails']['responseEntity'].vanProviderDetail;
                    this.sortVanProviderArray(vanArray)
                    vanArray.forEach(element => {
                        this.vanProvidersList.push({ "label": element.vanProvider.companyName, "value": element.vanProvider.companyName });
                    });
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }
    private consolidateData() {
        if (this.customerTestISAindicator) {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator = "Y";
        } else {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.customerTestISAindicator = "N";
        }

        if (this.tpTestISAindicator) {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.tpTestISAindicator = "Y";
        } else {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.tpTestISAindicator = "N";
        }

        if (this.functionalAck) {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.functionalAck = "Y";
        } else {
            this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.functionalAck = "N";
        }
        if (this.sel_prod_address.length > 0) {
            let add = this.sel_prod_address.split(":");
            this.workFlow.provisioningRequestData.customer.tradingAddresses.prod.qualifier = add[0];
            this.workFlow.provisioningRequestData.customer.tradingAddresses.prod.address = add[1];
        }
        if (this.sel_test_address.length > 0) {
            let add = this.sel_test_address.split(":");
            this.workFlow.provisioningRequestData.customer.tradingAddresses.test.qualifier = add[0];
            this.workFlow.provisioningRequestData.customer.tradingAddresses.test.address = add[1];
        }
        this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.maps = this.selectedMaps;

    }

    setCacheData() {
        this.tpmsgService.set_Step_2_ProdAdd(this.sel_prod_address);
        this.tpmsgService.set_sel_test_address(this.sel_test_address);
        this.tpmsgService.setfunctionalAck(this.functionalAck);
        this.tpmsgService.setTpTestISAindicator(this.tpTestISAindicator);
        this.tpmsgService.setCustTestISAindicator(this.customerTestISAindicator);
        this.tpmsgService.setSelectedProvisioningMap(this.selectedMaps);
        this.tpmsgService.setSelectedMapFlag(this.mapSelected);
        // this.tpmsgService.setTempSelMap(this.tempSelectedMaps);

    }

    tableEntryError: boolean = false;
    @ViewChild('entriesError')
    private entriesError: ElementRef;
    @ViewChild('container')
    private container: ElementRef;
    validateMaps(): boolean {
        this.tableEntryError = false;
        this.selectedMaps.forEach(data => {
            if (data.direction == "Inbound") {
                if (data.edi_dc40_prod.trim() == "" || data.edi_dc40_test.trim() == "") {
                    let scrollTo = this.entriesError.nativeElement;
                    scrollTo.scrollIntoView();
                    this.tableEntryError = true;
                }
            } else {
                if (data.acr.trim() == "") {
                    let scrollTo = this.entriesError.nativeElement;
                    scrollTo.scrollIntoView();
                    this.tableEntryError = true;
                }
            }
        })
        return this.tableEntryError;
    }
    next() {
        if (!this.validateMaps() && this.msgForm.valid) {
            this.trimSpace();
            //console.log(this.selectedMaps);
            this.setCacheData();
            this.consolidateData();
            console.log(this.workFlow);
            this.workflowTpmsgService.validateStep(STEPS.messagedetails);
            this.router.navigate(['/tpdir/tpmsgbase/summary']);
        }

    }

    previous() {
        this.setCacheData();
        this.router.navigate(['/tpdir/tpmsgbase/add'], { queryParams: { prev: 'true' }, skipLocationChange: true });
    }

    seg_terminantorFlag: boolean = false;
    elem_separatorFlag: boolean = false;
    sub_elem_terminatorFlag: boolean = false;
    separatorErrorFlag: boolean = false;

    checkSeparatorValue() {

        let segment_separatorValue = this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.segmentTerminator;
        let element_separatorValue = this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.elementSeparator;
        let subelement_separatorValue = this.workFlow.provisioningRequestData.registrationData.tgtsProfiles.messageDetails.subelementTerminator;

        this.seg_terminantorFlag = false;
        this.elem_separatorFlag = false;
        this.sub_elem_terminatorFlag = false;
        this.separatorErrorFlag = false;

        if (segment_separatorValue != "" && segment_separatorValue != "Default") {
            if (this.checkSegement(segment_separatorValue, element_separatorValue, subelement_separatorValue)) {
                this.msgForm.form.controls["segmentSeparator"].setErrors({ 'incorrect': true });
                this.seg_terminantorFlag = true;
                this.separatorErrorFlag = true;
            } else {
                this.msgForm.form.controls["segmentSeparator"].setErrors(null);
                //this.seg_terminantorFlag=false;
            }
        }
        if (element_separatorValue != "" && element_separatorValue != "Default") {
            if (this.checkElement(segment_separatorValue, element_separatorValue, subelement_separatorValue)) {
                this.msgForm.form.controls["elementSeprator"].setErrors({ 'incorrect': true });
                this.elem_separatorFlag = true;
                this.separatorErrorFlag = true;
            } else {
                this.msgForm.form.controls["elementSeprator"].setErrors(null);
                //this.elem_separatorFlag=false;
            }
        }
        if (subelement_separatorValue != "" && subelement_separatorValue != "Default") {
            if (this.checkSubElement(segment_separatorValue, element_separatorValue, subelement_separatorValue)) {
                this.msgForm.form.controls["subElementSeparator"].setErrors({ 'incorrect': true });
                this.sub_elem_terminatorFlag = true;
                this.separatorErrorFlag = true;
            } else {
                this.msgForm.form.controls["subElementSeparator"].setErrors(null);
                //this.sub_elem_terminatorFlag=false;
            }
        }
    }
    checkSegement(segment_separatorValue, element_separatorValue, subelement_separatorValue) {

        if ((segment_separatorValue === element_separatorValue) || (segment_separatorValue === subelement_separatorValue)
        ) {
            return true;
        } else {
            return false;
        }
    }
    checkElement(segment_separatorValue, element_separatorValue, subelement_separatorValue) {

        if ((element_separatorValue === segment_separatorValue) || (element_separatorValue === subelement_separatorValue)
        ) {
            return true;
        } else {
            return false;
        }
    }
    checkSubElement(segment_separatorValue, element_separatorValue, subelement_separatorValue) {

        if ((subelement_separatorValue === segment_separatorValue) || (subelement_separatorValue === element_separatorValue)
        ) {
            return true;
        } else {
            return false;
        }
    }

    cancel(){
        this.dialogueboxService.confirm({
            dialogName: 'cancelSetup',
            accept: () => {
                let navigationExtras = {
                    queryParams: {gd: 't'},
                    skipLocationChange: true
                  }
                if(this.isGDFlag){
                    this.router.navigate(['/globalDirectory'],navigationExtras);
                }else{
                    this.router.navigate(['/tpdir']);
                }
            }
            , reject: () => {
            }
        });
    }
}