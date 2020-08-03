import { Component, ViewChild, ElementRef } from "@angular/core";
import { NgForm } from "@angular/forms";
import { PSscrollUtils } from "tgocp-ng/dist/shared/perfect-scrollbar-config";
import { TPMSGService } from "../../../../../services/tpmsg.service";
import { Workflow, ListAddress, SplitCharges } from "../entity/workflow.entity";
import { TPCreateMsgService } from "../../../../../services/tp-create.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { Router } from "@angular/router";
import { WorkflowTpMsgService } from "../entity/workflowtpmsg.service";
import { STEPS } from "../entity/steps.model";
import { TranslateService } from '@ngx-translate/core';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';

@Component({
    templateUrl: "./msgDetails-TGMS.component.html"
})
export class MsgDetailsTGMSComponent {
    public config: PerfectScrollbarConfigInterface = {}; // to fix minimum scroll height for on scroll loading
    scrollConfig = PSscrollUtils.scrollY();
    workFlow: Workflow;
    vanProvidersList: any = [];
    vanDisableFlag: boolean = false;
    yourAddrs: any[] = [];
    selectedAddress: any;
    loaderFlag: boolean = true;
    after = 0;
    yourEdis: any[] = [];
    tempSelectedVan = "";
    billingOption: any = "SPLIT";
    sendingPaidBy: any = "SENDER";
    receivingPaidBy: any = "RECEIVER";
    receiverMailbxStoragePaidBy: any = "RECEIVER";
    billingOptionsDisabled: boolean = true;
    allChargesPaidBy: any = "";
    isGDFlag: boolean = false;
    splitFieldsDisabled: boolean = true;
    allChargesFieldsDisabled: boolean = true;
    @ViewChild("tgmsForm") tgmsForm: NgForm;
    tprList: any[] = [];
    companyName: any;
    showPopup: boolean;

    constructor(private workflowTpmsgService: WorkflowTpMsgService, private tpmsgService: TPMSGService,
        private router: Router, private createtpMsgService: TPCreateMsgService, private dialogueboxService: DialogueboxService,
        private notificationHandler: NotificationHandler, private translate: TranslateService) {

    }

    ngOnInit() {
        //  this.yourAddrs.push({ 'label': "ZXC:HCN877831", 'value': "ZXC:HCN877831" });
        this.config.minScrollbarLength = 10;
        this.tpmsgService.activateRoute("MD");
        this.isGDFlag = this.tpmsgService.isGDFlag;
        this.workFlow = this.tpmsgService.getCachedWorkFlow();
        this.selectedAddress = this.tpmsgService.getSelectedEdi();
        this.billingOption = this.tpmsgService.getCachedBillingInfo().billingOption || "SPLIT";
        this.sendingPaidBy = this.tpmsgService.getCachedBillingInfo().sendingPaidBy || "SENDER";
        this.receivingPaidBy = this.tpmsgService.getCachedBillingInfo().receivingPaidBy || "RECEIVER";
        this.receiverMailbxStoragePaidBy = this.tpmsgService.getCachedBillingInfo().receiverMailbxStoragePaidBy || "RECEIVER";
        this.allChargesPaidBy = this.tpmsgService.getCachedBillingInfo().allChargesPaidBy;
        this.workFlow.provisioningRequestData.registrationData.tradingAddress.connectivityType = "VAN";
        this.attachValueChangeListener();
        this.setVanProvider();
        this.getAddress(0);
        this.subscribeToStep();
    }

    private subscribeToStep() {
        this.tpmsgService.moveToStepSUB.subscribe(data => {
            if (data == "summary" && this.tgmsForm.valid) {
                this.next();
            } else if (data == "add") {
                this.previous();
            }
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

    billingOptionSelected() {
        if (this.billingOption == "SPLIT") {
            this.splitFieldsDisabled = false;
            this.allChargesFieldsDisabled = !this.splitFieldsDisabled;
        } else {
            this.splitFieldsDisabled = true;
            this.allChargesFieldsDisabled = !this.splitFieldsDisabled;
        }
    }
    previous() {
        this.cacheData();
        this.router.navigate(['/tpdir/tgmsbase/add'], { queryParams: { prev: 'true' }, skipLocationChange: true });
    }

    cacheData() {
        if (this.selectedAddress.length > 0) {
            this.workFlow.provisioningRequestData.customer.listAddress = [];
            this.selectedAddress.forEach(data => {
                this.workFlow.provisioningRequestData.customer.listAddress.push(new ListAddress(data));
            })
        }

        this.tpmsgService.saveBilliingOptions(this.billingOption,
            this.sendingPaidBy,
            this.receivingPaidBy,
            this.receiverMailbxStoragePaidBy,
            this.allChargesPaidBy);
        if (this.billingOption == "ALL_CHARGES") {
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.allCharges = this.allChargesPaidBy;
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.splitCharges = new SplitCharges();
        } else {
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.splitCharges.receiverMBStorage = this.receiverMailbxStoragePaidBy;
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.splitCharges.receiving = this.receivingPaidBy;
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.splitCharges.sending = this.sendingPaidBy;
            this.workFlow.provisioningRequestData.registrationData.tgmsBilling.allCharges = "";
        }
        this.tpmsgService.saveWorkflow(this.workFlow);
        this.tpmsgService.saveSelectedEdis(this.selectedAddress);
    }


    next() {
        this.cacheData();
        let tradingAdress = this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.qualifier + ":" + this.workFlow.provisioningRequestData.registrationData.tradingAddress.prodtp.address
        //validation for Tpr Exist or Not for selected Hub EDi's
        this.createtpMsgService.getTprList(this.tpmsgService.loggedinBUID, tradingAdress).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.tprList = data['responseDetails']['responseEntity'];
                    if (this.tprList && this.tprList.length > 0) {
                        let ownerEdiAddress = this.tprList.map(item => (item.ownerTradingAddress.qualifier != null ? item.ownerTradingAddress.qualifier : "")
                            + ":" + item.ownerTradingAddress.address);
                        let partnerTradingAddress = this.tprList.map(item => (item.partnerTradingAddress.qualifier != null ? item.partnerTradingAddress.qualifier : "")
                            + ":" + item.partnerTradingAddress.address);
                        // removing duplicates partnertrading address (contains same edis with different direction)
                        let partnerEdiAddress = partnerTradingAddress.filter((item, i, ar) => ar.indexOf(item) === i);
                        this.yourEdis = [];
                        this.selectedAddress.forEach(selectedEdi => {
                            let ediWithTpr = (ownerEdiAddress.find(id => id == selectedEdi)) || (partnerEdiAddress.find(id => id == selectedEdi));
                            if (ediWithTpr) {
                                this.yourEdis.push({ 'value': ediWithTpr })
                            }
                        })
                        if (this.yourEdis && this.yourEdis.length > 0) {
                            if (this.yourAddrs.length > 1) {
                                this.showPopup = true;
                            }
                            else if (this.yourAddrs.length == 1) {
                                this.notificationHandler.notify({ severity: 'warning', title: this.translate.instant("tpmsg.tprValidation.tprExistMsg"), details: this.translate.instant("tpmsg.tprValidation.singleHubEdiMsg") });
                            }
                        }
                        else {
                            this.workflowTpmsgService.validateStep(STEPS.billingSplit);
                            this.router.navigate(['/tpdir/tgmsbase/summary'], { queryParams: { type: 'tgms' }, skipLocationChange: true });
                        }
                    }
                    else {
                        this.workflowTpmsgService.validateStep(STEPS.billingSplit);
                        this.router.navigate(['/tpdir/tgmsbase/summary'], { queryParams: { type: 'tgms' }, skipLocationChange: true });
                    }
                }
            })


    }

    public onScrollToEnd(event: any): void {
        if (event.target.className.indexOf("active-y") > 0) {
            this.loaderFlag = false;
            this.after += 20;
            this.getAddress(this.after);
        }
    }

    private getAddress(after) {
        this.tpmsgService.getEDIList(after).subscribe(data => {
            if (data['responseDetails']['success']) {
                data['responseDetails']['responseEntity'].forEach(element => {
                    let qual = !element.tradingAddress.qualifier ? "" : element.tradingAddress.qualifier;
                    let edi = qual + ":" + element.tradingAddress.address;
                    this.yourAddrs.push({ "label": edi, "value": edi });
                });
                this.loaderFlag = true;
            }
        }, () => {
            this.loaderFlag = true;
            this.notificationHandler.notify({ severity: 'success', title: this.translate.instant("common.endOfRecords") });
        })
    }

    private attachValueChangeListener() {
        this.tgmsForm.valueChanges.subscribe(data => {
            if (data.vanProvider && data.vanProvider != this.tempSelectedVan) {
                if (this.vanProviderDATA && this.vanProviderDATA.length > 0) {
                    const van = this.vanProviderDATA.find(element => data.vanProvider == element.vanProvider.companyName);
                    this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanMailboxId = van.mailboxId;
                }
            }
            this.tempSelectedVan = data.vanProvider;
        })
    }


    private setVanProvider() {
        if (this.tpmsgService.getVanProvider()) {
            this.workFlow.provisioningRequestData.registrationData.tradingAddress.vanProvider = this.tpmsgService.getVanProvider();
            this.vanDisableFlag = true;
            this.billingOptionsDisabled = this.tpmsgService.getVanProvider() != this.tpmsgService.gxsCompany;
            this.billingOptionSelected();
        } else {
            this.vanDisableFlag = false;
            this.getVanProvider();
        }
    }

    sortVanProviderArray(array) {
        array.sort((a, b) => {
            if (a.vanProvider.companyName < b.vanProvider.companyName) return -1;
            else if (a.vanProvider.companyName > b.vanProvider.companyName) return 1;
            else return 0;
        });
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
                } else {
                    this.router.navigate(['/tpdir']);
                }
            }
            , reject: () => {
            }
        });
    }
    cancelTpDetailsPopUp() {
        this.showPopup = false;
        this.yourEdis = [];
    }
}
