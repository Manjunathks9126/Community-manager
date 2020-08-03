import { TprRequestEntity } from './../../createTPMSG/entity/tprResponse.entity';
import { DialogueboxService } from "tgocp-ng/dist/components/dialoguebox/dialoguebox.service";
import { Component, ViewChild, OnInit } from "@angular/core";
import { DatePipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { TradingParnterService } from "../../../../../services/trading-partner.service";
import { TPDirectoryListFilterOptions } from "./tpr.filter.options";
import { FilterEntity } from "../../../filter-entity";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { DataTable } from "tgocp-ng/dist/components/datatable/datatable";
import { FormDataService } from "../../../create/entity/formData.service";
import { LazyLoadEvent } from "tgocp-ng/dist/components/common/lazyloadevent";
import { TranslateService } from '@ngx-translate/core';
import { PERMISSSION } from '../../createTPMSG/entity/permission.constants';
import { MatomoTracker } from 'ngx-matomo';

@Component({
  selector: "tpr-list",
  templateUrl: "./tpr-list.component.html",
  providers: [MatomoTracker]
})
export class TradingPartnerListComponent implements OnInit {
  @ViewChild(DataTable) dataTableComponent: DataTable;

  companyName: string = "";
  partnerCompanyId: string;
  ediAddress: string = "";
  qualifier: string = "";
  selectedstatus: string[] = [];
  tplist: any[] = [];
  totalRecords: number = 0;
  selectedFilters: FilterEntity;
  isAllSelected = false;
  isSearchClicked = false;
  isFilterClicked = false;
  isFilterApplied = false;
  adjustFilterPanel: {};
  adjustContentPanel: {};
  filterOptions: TPDirectoryListFilterOptions;
  event: {};
  edis: any[];
  ediCompanyName: string;
  selectedData: any = [];
  exportColumns: any[];
  invitationcodes: any = [];
  exportBulk: any[] = [];
  bplist: any[] = [];
  bprId: string;
  numberOfFiltersApplied: number = 0;
  isDisplayName: string = "false";
  searchAttribute: string = "companyName";
  terminateTpResponse: any;
  modifyTpResponse: any;
  rejectTpResponse: any;
  terminateTp: boolean = false;
  modifyStatus: string = "";
  rejectReason: string = "";
  tprRequestData: TprRequestEntity[] = [];
  onSelectCount: number = 0;
  pendingStatus: boolean = false;
  failedMessage: any[] = [];
  terminateStatus: boolean;
  maxSelection: number = 10;
  permissions:any;
  currentLanguage:string="en";
  constructor(
    private tpService: TradingParnterService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private dialogueboxService: DialogueboxService,
    private notificationHandler: NotificationHandler,
    private formDataService: FormDataService,
    private translate: TranslateService,
    private matomoTracker: MatomoTracker
  ) { }

  ngOnInit() {
    this.permissions=PERMISSSION;

    this.filterOptions = this.route.snapshot.data[
      "TPOPtions"
    ].responseDetails.responseEntity;
    this.initVariables();
    this.totalTradingPartnerRelationships();
    this.tpService.selectedTpId = "NA";
    this.formDataService.resetFormData();
    this.currentLanguage=this.translate.currentLang;
    this.matomoTracker.trackPageView("Community Management: My Trading Community - Trading Relationship")
  }
  initVariables() {
    this.selectedFilters = new FilterEntity();
  }
  totalTradingPartnerRelationships() {
    this.selectedFilters.countOnly = true;
    this.tpService.totalTradingPartnerRelationships(this.selectedFilters).subscribe(
      data => {
        if (data["responseDetails"]["success"]) {
          this.totalRecords =
            data["responseDetails"]["responseEntity"].itemCount;
        }
      },
      error => {
        this.totalRecords = 0;
        if (error.status != 404)
          this.notificationHandler.notify({
            severity: "error",
            details: error.userMessage
          });
      }
    );
  }

  listTradingPartnerReltationships() {
    if (!this.selectedFilters.limit)
      this.selectedFilters.limit = this.filterOptions.rowsPerPage;
    this.selectedFilters.countOnly = false;
    this.tpService.listTradingPartnerRelationships(this.selectedFilters).subscribe(
      data => {
        if (data["responseDetails"]["success"]) {
          this.tplist = data["responseDetails"]["responseEntity"].itemList;
        } else {
          this.tplist = [];
        }
      },
      error => {
        this.tplist = [];
        this.notificationHandler.notify({
          severity: "error",
          title: error.userMessage
        });
      }
    );
  }

  lazyLoadTPList(event: LazyLoadEvent) {
    this.selectedData = this.dataTableComponent.selection;
    this.selectedFilters.after = event.first;
    this.selectedFilters.limit = event.rows;
    this.listTradingPartnerReltationships();
  }

  handleRowSelect($event: { checked: any; }) {
    if ($event.checked) {
      this.isAllSelected = true;
    } else {
      this.isAllSelected = false;
    }
  }

  displayFilterPanel() {
    this.isFilterClicked = !this.isFilterClicked;
    this.isSearchClicked = false;
  }

  displaySearchPanel() {
    this.isSearchClicked = !this.isSearchClicked;
    this.isFilterClicked = false;
  }

  clearFilterCriteria() {
    this.isFilterApplied = false;
    this.isFilterClicked = false;
    this.partnerCompanyId = null;
    this.companyName = null;
    this.selectedstatus = [];
    this.selectedFilters.displayName = undefined;
    this.selectedFilters.status = undefined;

    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
    this.searchAttribute = "companyName";
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
  }
  clearSearchCriteria() {
    this.displaySearchPanel();
  }
  filterData() {
    this.numberOfFiltersApplied = 0;
    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
    this.isFilterApplied = true;
    this.isFilterClicked = true;
    this.selectedFilters.after = 0;
    // this.selectedFilters.limit = this.filterOptions.rowsPerPage;
    this.companyName = this.companyName ? this.companyName.trim() : "";
    this.partnerCompanyId = this.partnerCompanyId
      ? this.partnerCompanyId.trim().toUpperCase()
      : "";
    this.ediAddress = this.ediAddress ? this.ediAddress.trim() : "";
    this.qualifier = this.qualifier ? this.qualifier.trim() : "";
    if (this.searchAttribute == "displayName") {
      this.resetSearcKeys();
      this.selectedFilters.displayName = this.companyName;
    } else if (this.searchAttribute == "companyName") {
      this.resetSearcKeys();
      this.selectedFilters.companyName = this.companyName;
    }
    if (this.searchAttribute == "companyId") {
      this.resetSearcKeys();
      this.selectedFilters.partnerCompanyId = this.partnerCompanyId;
    }

    //this.selectedFilters.qualifier = this.qualifier;
    if (this.searchAttribute == "ediAddress") {
      this.resetSearcKeys();
      if (
        (this.qualifier != null && this.qualifier.length > 0) ||
        (this.ediAddress != null && this.ediAddress.length > 0)
      ) {
        this.selectedFilters.ediAddress = (
          (this.ediAddress ? this.qualifier : "") +
          ":" +
          this.ediAddress
        ).toUpperCase();
      } else {
        this.selectedFilters.ediAddress = "";
      }
    }
    if (this.selectedstatus != undefined && this.selectedstatus.length > 0) {
      this.selectedFilters.status = this.selectedstatus;
    } else {
      this.selectedFilters.status = [];
      this.selectedstatus = undefined;
    }
    this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(
      x =>
        this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0
    ).length;

    this.tplist = [];
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    // this.dataTableComponent.paginatorElement['activePage']=1;
    this.dataTableComponent.reset();
  }

  searchData() {
    this.numberOfFiltersApplied = 0;
    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
    this.isFilterApplied = true;
    this.selectedFilters.after = 0;
    // this.selectedFilters.limit = this.filterOptions.rowsPerPage;
    this.companyName = this.companyName ? this.companyName.trim() : "";
    this.partnerCompanyId = this.partnerCompanyId
      ? this.partnerCompanyId.trim().toUpperCase()
      : "";
    this.ediAddress = this.ediAddress ? this.ediAddress.trim() : "";
    this.qualifier = this.qualifier ? this.qualifier.trim() : "";
    if (this.searchAttribute == "displayName") {
      this.resetSearcKeys();
      this.selectedFilters.displayName = this.companyName;
    } else if (this.searchAttribute == "companyName") {
      this.resetSearcKeys();
      this.selectedFilters.companyName = this.companyName;
    }
    if (this.searchAttribute == "companyId") {
      this.resetSearcKeys();
      this.selectedFilters.partnerCompanyId = this.partnerCompanyId;
    }
    // this.selectedFilters.qualifier = this.qualifier;
    if (this.searchAttribute == "ediAddress") {
      this.resetSearcKeys();
      if (
        (this.qualifier != null && this.qualifier.length > 0) ||
        (this.ediAddress != null && this.ediAddress.length > 0)
      ) {
        this.selectedFilters.ediAddress = (
          (this.ediAddress ? this.qualifier : "") +
          ":" +
          this.ediAddress
        ).toUpperCase();
      } else {
        this.selectedFilters.ediAddress = "";
      }
    }
    if (this.selectedstatus != undefined && this.selectedstatus.length > 0) {
      this.selectedFilters.status = this.selectedstatus;
    } else {
      this.selectedFilters.status = [];
      this.selectedstatus = undefined;
    }

    this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(
      x =>
        this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0
    ).length;

    this.tplist = [];
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    this.resetPaginator();
  }

  resetPaginator() {
    this.dataTableComponent.paginatorElement["activePage"] = 1;
    this.dataTableComponent.paginatorComponent.first = 0;
    this.dataTableComponent.paginatorComponent.start = 0;
  }

  // need to clear only chipset filter
  clearStatusChipset() {
    this.selectedFilters.status.length = 0;
    this.selectedFilters.status = undefined;
    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
    this.selectedstatus = undefined;
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    if (
      this.selectedstatus == undefined &&
      !this.companyName &&
      !this.ediAddress &&
      !this.qualifier &&
      !this.partnerCompanyId
    ) {
      this.closeChipset();
    }
  }

  clearEdiChipset() {
    this.clearSearchFilter();
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    if (
      (this.selectedstatus == undefined || this.selectedstatus.length == 0) &&
      !this.companyName &&
      !this.ediAddress &&
      !this.qualifier &&
      !this.partnerCompanyId
    ) {
      this.closeChipset();
    }
  }
  // need to clear only Company filter
  clearCompanyChipset() {
    this.clearSearchFilter();
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    if (
      this.selectedstatus == undefined &&
      !this.companyName &&
      !this.ediAddress &&
      !this.qualifier &&
      !this.partnerCompanyId
    ) {
      this.closeChipset();
    }
  }
  clearCompanyIdChipset() {
    this.clearSearchFilter();
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
    if (
      this.selectedstatus == undefined &&
      !this.companyName &&
      !this.ediAddress &&
      !this.qualifier &&
      !this.partnerCompanyId
    ) {
      this.closeChipset();
    }
  }

  clearSearchFilter() {
    this.selectedFilters.displayName = "";
    this.selectedFilters.companyName = "";
    this.companyName = null;

    this.selectedFilters.partnerCompanyId = "";
    this.partnerCompanyId = null;

    this.ediAddress = "";
    this.qualifier = "";
    this.selectedFilters.ediAddress = "";
    this.selectedFilters.qualifier = "";

    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
  }

  exportToCsv() {
    var date = new Date();
    this.dataTableComponent.exportFilename =
      "TPRList_" +
      this.datePipe.transform(date, "MM_dd_y_") +
      date.getHours() +
      "_" +
      date.getMinutes() +
      "_" +
      date.getSeconds();
    var clonedDataTable = Object.assign([], this.dataTableComponent.columns);
    this.dataTableComponent.exportCSV({ selectionOnly: true });
    this.dataTableComponent.columns = clonedDataTable;
    this.selectedData = [];
  }

  sortTpData(event: { field: string; }) {
    this.selectedFilters.sortField = event.field;
  }

  // need to clear both Company and status filter

  clearFilter() {
    this.closeChipset();
    this.totalTradingPartnerRelationships();
    this.listTradingPartnerReltationships();
  }

  closeChipset() {
    this.isFilterApplied = false;
    this.companyName = "";
    this.partnerCompanyId = null;
    this.ediAddress = "";
    this.qualifier = "";
    this.selectedstatus = [];
    this.selectedFilters.name = "";
    this.selectedFilters.displayName = "";
    this.selectedFilters.partnerCompanyId = "";
    this.selectedFilters.companyName = "";
    this.selectedFilters.ediAddress = "";
    this.selectedFilters.qualifier = "";
    this.selectedFilters.status = [];
    this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
  }

  filterHasData(): boolean {
    return (
      (this.companyName != null && this.companyName.length > 0) ||
      (this.selectedstatus != null && this.selectedstatus.length > 0)
    );
  }

  redirectToEdit(companyId: string, displayName: string) {
    this.router.navigate(["tpdir/company", companyId, "overview"],
      {
        queryParams: { 'name': displayName },
        skipLocationChange: true
      }
    );
  }

  resetSearcKeys() {
    this.selectedFilters.displayName = null;
    this.selectedFilters.companyName = null;
    this.selectedFilters.ediAddress = "";
    this.selectedFilters.partnerCompanyId = null;
  }

  // Terminate tpr data assignment
  terminateTpr(action: string) {
    this.tprRequestData = [];
    for (let i = 0; i < this.selectedData.length; i++) {
      let tprData = new TprRequestEntity();
      tprData.tprId = this.selectedData[i].tprId;
      tprData.companyName = this.selectedData[i].companyName;
      // THIS VALUE not used as the datatype in java layer is enum just added it.
      tprData.modifiedData.modifyStatus = 'REJECT';
      this.tprRequestData.push(tprData);
    }
    this.doTerminateTPRs(action);
  }

  // Terminate tpr service call
  doTerminateTPRs(action: string) {
    this.tpService.terminateTprList(this.tprRequestData).subscribe(
      data => {
        this.failedMessage = [];
        let successResponse = 0;
        const failedTpList = [];
        if (data['responseDetails']['success']) {
          for (let i = 0; i < data.responseDetails.responseEntity.length; i++) {
            this.terminateTpResponse = data.responseDetails.responseEntity[i];
            if (this.terminateTpResponse.statusCode == 200) {
              successResponse++;
            } else if (this.terminateTpResponse.statusCode != 200) {
              successResponse--;
              failedTpList[i] = this.selectedData[i];
              this.failedMessage.push(
                failedTpList[i].companyName + ' ' + this.translate.instant("common.unsuccessful")
              );
            }
          }
          this.reloadTradingPartnerReltationships(failedTpList, data, successResponse, action);
        }
      },
      error => {
        this.notificationHandler.notify({
          severity: 'error',
          details: error.userMessage
        });
      }
    );
  }

  // On successful execution or partial execution of tpr actions notifier.
  notifyResponse(data: { responseDetails: { responseEntity: { length: number; }; }; }, successResponse: any, action: string, failedTpList: { length: any; }) {
    if (data.responseDetails.responseEntity.length === successResponse) {
      if (action === 'terminate') {
        this.notificationHandler.notify({
          severity: 'success',
          title: this.translate.instant("tradingpartnerreltationship.messages.success.terminate"),
        });
      } else if (action === 'reject') {
        this.notificationHandler.notify({
          severity: 'success',
          title: this.translate.instant("tradingpartnerreltationship.messages.success.reject"),
        });
      } else if (action === 'approve') {
        this.notificationHandler.notify({
          severity: 'success',
          title: this.translate.instant("tradingpartnerreltationship.messages.success.approve")
        });
      }
    } else if (failedTpList.length > 0) {
      this.notificationHandler.notify({
        severity: 'error',
        title: this.translate.instant("tradingpartnerreltationship.messages.failed"),
        details: this.failedMessage
      });
    }
  }

  // Approve or Reject Tpr data assignment based on the action
  approveRejectTpr(action: string) {
    this.tprRequestData = [];
    for (let i = 0; i < this.selectedData.length; i++) {
      let tprData = new TprRequestEntity();
      tprData.tprId = this.selectedData[i].tprId;
      tprData.companyName = this.selectedData[i].companyName;
      tprData.companyId = this.selectedData[i].companyId;
      if (action === 'approve') {
        tprData.modifiedData.modifyStatus = 'ACTIVE';
      } else if (action === 'reject') {
        if (this.rejectReason.length === 0) {
          this.notificationHandler.notify({
            severity: 'error',
            title: this.translate.instant("tradingpartnerreltationship.messages.enterReason")
          });
          return;
        } else if (this.rejectReason.length < 257) {
          tprData.modifiedData.modifyStatus = 'REJECT';
          tprData.modifiedData.reasonForRejection = this.rejectReason;
        } else if (this.rejectReason.length >= 257) {
          this.notificationHandler.notify({
            severity: 'error',
            title: this.translate.instant("tradingpartnerreltationship.messages.reject")
          });
          return;
        }
      }
      this.tprRequestData.push(tprData);
    }
    this.approveRejectTprServiceCall(action);
  }

  // Service call to perform the action for approve/reject
  approveRejectTprServiceCall(action: string) {
    this.tpService.approveRejectTprList(this.tprRequestData).subscribe(
      data => {
        let successResponse = 0;
        const failedTpList = [];
        this.failedMessage = [];
        if (data['responseDetails']['success']) {
          for (let i = 0; i < data.responseDetails.responseEntity.length; i++) {
            this.modifyTpResponse = data.responseDetails.responseEntity[i];
            if (this.modifyTpResponse.statusCode == 200) {
              successResponse++;
            } else if (this.modifyTpResponse.statusCode != 200) {
              successResponse--;
              failedTpList[i] = this.selectedData[i];
              this.failedMessage.push(
                failedTpList[i].companyName + ' ' + this.translate.instant("common.unsuccessful")
              );
            }
            this.rejectReason = '';
          }
          this.reloadTradingPartnerReltationships(failedTpList, data, successResponse, action);
        }
      },
      error => {
        this.notificationHandler.notify({
          severity: 'error',
          details: error.userMessage
        });
      }
    );
  }

  // After tpr approve/reject/terminate action releasing the data and updating the tpr directory.
  reloadTradingPartnerReltationships(failedTpList: any[], data: any, successResponse: number, action: string) {
    if (failedTpList.length !== this.selectedData.length) {
      this.notifyResponse(data, successResponse, action, failedTpList);
      this.selectedData = [];
      this.listTradingPartnerReltationships();
      this.totalTradingPartnerRelationships();
    }
  }

  // terminate Dialogbox initializer.
  terminateDialogbox(action: string) {
    if (this.selectedData.length <= 10) {
      this.dialogueboxService.confirm({
        dialogName: 'confirmTerminate',
        accept: () => {
          this.terminateTpr(action);
        },
        reject: () => { }
      });
    }
  }

  // Approve/Reject Dialoguebox initializer.
  approveRejectDialogbox(action: string) {
    if (action === 'approve' && this.selectedData.length <= 10) {
      this.dialogueboxService.confirm({
        dialogName: 'confirmApprove',
        accept: () => {
          this.approveRejectTpr(action);
        },
        reject: () => { }
      });
    } else if (action === 'reject' && this.selectedData.length <= 10) {
      this.dialogueboxService.confirm({
        dialogName: 'confirmReject',
        accept: () => {
          this.approveRejectTpr(action);
        },
        reject: () => { }
      });
    }
  }


  // On selecting the tpr fields hiding/showing based on the status.
  onSelect() {
    this.onSelectCount = 0;
    this.pendingStatus = false;
    for (let i = 0; i < this.selectedData.length; i++) {
      if (this.selectedData[i].status === 'PENDING') {
        this.onSelectCount++;
      } else {
        this.onSelectCount = 0;
      }
    }
    if (this.onSelectCount === this.selectedData.length) {
      this.pendingStatus = true;
    }
    this.onSelectCount = 0;
    this.terminateStatus = false;
    for (let i = 0; i < this.selectedData.length; i++) {
      if (this.selectedData[i].status !== 'PENDING') {
        this.onSelectCount++;
      } else {
        this.onSelectCount = 0;
      }
    }
    if (this.onSelectCount === this.selectedData.length) {
      this.terminateStatus = true;
    }
  }
}
