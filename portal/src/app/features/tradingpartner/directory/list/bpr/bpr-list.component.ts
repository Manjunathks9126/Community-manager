import { Component, ViewChild, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from '@angular/router';
import { TradingParnterService } from "../../../../../services/trading-partner.service";
import { CompanyProfileService } from "../../../../../services/company-profile.service";
import { TPDirectoryListFilterOptions } from "./tplist.filter.options";
import { FilterEntity } from "../../../filter-entity";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { FormDataService } from "../../../create/entity/formData.service";
import { BusinessFacades } from "../../../facades.entity";
import { DataTable } from "tgocp-ng/dist/components/datatable/datatable";
import { TradingParnterDetailsService } from "../../../../../services/trading-partner-details.service";
import { LazyLoadEvent } from "tgocp-ng/dist/components/common/lazyloadevent";
import { OnboardingService } from "../../../../../services/onboarding.service";
import { Workflow } from "../../createTPMSG/entity/workflow.entity";
import { TPGraphService } from "../../../../../services/tp-graph.service";
import { TranslateService } from '@ngx-translate/core';
import { PERMISSSION } from '../../createTPMSG/entity/permission.constants';
import { DatePipe } from '@angular/common';
import { Company } from './bpr-company.entity';
import { Constants } from '../../../../../services/AppConstant';
import { MatomoTracker } from 'ngx-matomo';

@Component({
    styleUrls: ['./bpr-list.css'],
    selector: 'bpr-list',
    templateUrl: './bpr-list.component.html',
    providers: [MatomoTracker]

})

export class BusinessPartnerListComponent implements OnInit {
    showWorkflowModal: boolean = false;

    @ViewChild(DataTable) dataTableComponent: DataTable;
    dateTime = new Date();
    companyName: string = "";
    partnerCompanyId: string;
    ediAddress: string = "";
    qualifier: string = "";
    selectedstatus: string[] = [];
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
    display: boolean = false;
    edis: any[];
    ediCompanyName: string;
    exportColumns: any[];
    invitationcodes: any = [];
    selectedInvCode: string;
    exportBulk: any[] = [];
    bplist: any[] = null;
    bprId: string;
    numberOfFiltersApplied: number = 0;
    searchAttribute: string = 'companyName';
    workflowList: any[] = [];
    showapprovetp: boolean = false;
    isGraphSubscribed: boolean = false;
    errorDescription: string = "";
    isBulkInvite: boolean;
    bprStatus = [];
    permissions: any;
    dateFrom: any;
    dateTo: any;
    selectedDateTo: any;
    selectedDateFrom: any;
    dateValid: boolean = true;
    notificationMsgs: string[] = [];
    validationNotification: any;
    editDispNamePermission:boolean;
    currentLanguage:string="en";
    isImportCustomFields : boolean = false;
    invitationId: any;
    invitationName: any;
    workflowId: any;
    workflowDisplayName: any;
    bulkImportType: string;

    constructor(private cpService: CompanyProfileService, private tpService: TradingParnterService,
        private route: ActivatedRoute, private router: Router,
        private tpdService: TradingParnterDetailsService,
        private notificationHandler: NotificationHandler, private formDataService: FormDataService,
        private onboardingService: OnboardingService,
        private tpGraph: TPGraphService, private translate: TranslateService, public datepipe: DatePipe, private matomoTracker: MatomoTracker) { }

    ngOnInit() {
        this.cpService.setPrivateView(false);
        this.translate.get("tpdir.read").subscribe(res => { this.validationNotification = res; })
        this.permissions = PERMISSSION;
        this.bprStatus = [
            { name: 'status.bpr.Approved', value: 'APPROVED' },
            { name: 'status.bpr.Requested', value: 'REQUESTED' },
            { name: 'status.bpr.Rejected', value: 'REJECTED' },
            { name: 'status.bpr.Error', value: 'Error' },
            { name: 'status.bpr.Testing', value: 'Testing' },
        ]
        this.filterOptions = this.route.snapshot.data['TPOPtions'].responseDetails.responseEntity;
        this.initVariables();
        this.bprCount();
        this.tpService.selectedTpId = "NA";
        this.formDataService.resetFormData();
        this.getSubscription();
        this.translate.get("onboarding.noWorkflows").subscribe(res => { this.notificationMsgs.push(res) })
        this.translate.get("onboarding.noBulkWorkflows").subscribe(res => { this.notificationMsgs.push(res) })
        this.currentLanguage=this.translate.currentLang;
        this.matomoTracker.trackPageView("Community Management: My Trading Community - Business Relationship")
    }

    private getSubscription() {
        this.tpGraph.getTpGraphSubscription().subscribe(data => {
            if (data.responseEntity)
                this.isGraphSubscribed = data.responseEntity.graphsubscription;
        }, error => {
            this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    }

    refresh() {
        this.selectedFilters.after = 0;
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        this.resetPaginator();
    }

    initVariables() {
        this.selectedFilters = new FilterEntity();
    }

    lazyLoadBPRList(event: LazyLoadEvent) {
        this.selectedFilters.after = event.first;
        this.selectedFilters.limit = event.rows;
        if (event.sortField && event.sortOrder) {
            this.selectedFilters.sortField = event.sortField;
            this.selectedFilters.sortOrder = event.sortOrder;
        }
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
    }

    displayFilterPanel() {
        this.isFilterClicked = !this.isFilterClicked;
        this.isSearchClicked = false;
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Filter");
    }

    displaySearchPanel() {
        this.isSearchClicked = !this.isSearchClicked;
        this.isFilterClicked = false;
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Search");
    }

    clearFilterCriteria() {
        this.isFilterApplied = false;
        this.isFilterClicked = false;
        this.partnerCompanyId = null;
        this.companyName = null;
        this.selectedstatus = [];
        this.selectedFilters.dateFrom = "";
        this.selectedFilters.dateTo = "";
        this.resetDates();
        this.selectedFilters.displayName = undefined;
        this.selectedFilters.status = undefined;
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
        this.searchAttribute = 'companyName';
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
    }
    clearSearchCriteria() {
        this.displaySearchPanel();
    }
    filterData() {
        if ((this.dateFrom && !this.dateTo) || (!this.dateFrom && this.dateTo)) {
            this.notificationHandler.notify({ severity: 'warning', title: this.validationNotification.badRequest, details: this.validationNotification.dateValidation });
        }
        else {
            this.numberOfFiltersApplied = 0;
            this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
            this.isFilterApplied = true;
            this.isFilterClicked = true;
            this.selectedFilters.after = 0;
            this.companyName = this.companyName ? this.companyName.trim() : "";
            this.partnerCompanyId = this.partnerCompanyId ? this.partnerCompanyId.trim() : '';
            this.ediAddress = this.ediAddress ? this.ediAddress.trim() : '';
            this.qualifier = this.qualifier ? this.qualifier.trim() : '';
            if (this.searchAttribute == 'displayName') {
                this.resetSearcKeys()
                this.selectedFilters.displayName = this.companyName;
            } else if (this.searchAttribute == 'companyName') {
                this.resetSearcKeys()
                this.selectedFilters.companyName = this.companyName;
            }
            if (this.searchAttribute == 'companyId') {
                this.resetSearcKeys()
                this.selectedFilters.partnerCompanyId = this.partnerCompanyId;
            }

            //this.selectedFilters.qualifier = this.qualifier;
            if (this.searchAttribute == 'ediAddress') {
                this.resetSearcKeys()
                if (this.qualifier != null && this.qualifier.length > 0 || this.ediAddress != null && this.ediAddress.length > 0) {
                    this.selectedFilters.ediAddress = ((this.ediAddress ? this.qualifier : '') + ':' + this.ediAddress).toUpperCase();
                } else {
                    this.selectedFilters.ediAddress = '';
                }
            }

            this.selectedFilters.dateFrom = this.datepipe.transform(this.dateFrom, 'MM/dd/yyyy');
            this.selectedFilters.dateTo = this.datepipe.transform(this.dateTo, 'MM/dd/yyyy');


            if (this.selectedstatus != undefined && this.selectedstatus.length > 0) {
                this.selectedFilters.status = this.selectedstatus;
            } else {
                this.selectedFilters.status = [];
                this.selectedstatus = undefined;
            }
            this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(x => this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0).length;

            this.bplist = [];
            this.bprCount();
            this.listBusinessPartners();
            // this.listBusinessPartnersBySolr();// Solr api
            this.resetPaginator();
        }
    }

    searchData() {
        this.totalRecords = 0;
        this.numberOfFiltersApplied = 0;
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
        this.isFilterApplied = true;
        this.selectedFilters.after = 0;
        //  this.selectedFilters.limit = this.filterOptions.rowsPerPage;
        this.companyName = this.companyName ? this.companyName.trim() : "";
        this.partnerCompanyId = this.partnerCompanyId ? this.partnerCompanyId.trim() : '';
        this.ediAddress = this.ediAddress ? this.ediAddress.trim() : '';
        this.qualifier = this.qualifier ? this.qualifier.trim() : '';
        if (this.searchAttribute == 'displayName') {
            this.resetSearcKeys()
            this.selectedFilters.displayName = this.companyName;
        } else if (this.searchAttribute == 'companyName') {
            this.resetSearcKeys()
            this.selectedFilters.companyName = this.companyName;
        }
        if (this.searchAttribute == 'companyId') {
            this.resetSearcKeys()
            this.selectedFilters.partnerCompanyId = this.partnerCompanyId;
        }
        // this.selectedFilters.qualifier = this.qualifier;
        if (this.searchAttribute == 'ediAddress') {
            this.resetSearcKeys()
            if (this.qualifier != null && this.qualifier.length > 0 || this.ediAddress != null && this.ediAddress.length > 0) {
                this.selectedFilters.ediAddress = ((this.ediAddress ? this.qualifier : '') + ':' + this.ediAddress).toUpperCase();
            }
            else {
                this.selectedFilters.ediAddress = '';
            }
        }
        if (this.selectedstatus != undefined && this.selectedstatus.length > 0) {
            this.selectedFilters.status = this.selectedstatus;
        } else {
            this.selectedFilters.status = [];
            this.selectedstatus = undefined;
        }

        this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(x => this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0).length;

        this.bplist = [];
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        this.resetPaginator();
    }

    resetPaginator() {
        this.dataTableComponent.paginatorElement['activePage'] = 1;
        this.dataTableComponent.paginatorComponent.first = 0;
        this.dataTableComponent.paginatorComponent.start = 0;
    }

    // need to clear only chipset filter
    clearStatusChipset() {
        this.selectedFilters.status.length = 0;
        this.selectedFilters.status = undefined;
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
        this.selectedstatus = undefined
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        if (this.selectedstatus == undefined && !this.companyName && !this.ediAddress && !this.qualifier && !this.partnerCompanyId && this.selectedFilters.dateFrom == undefined && this.selectedFilters.dateTo == undefined) {
            this.closeChipset();
        }

    }

    clearEdiChipset() {
        this.clearSearchFilter();
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        if ((this.selectedstatus == undefined || this.selectedstatus.length == 0) && !this.companyName && !this.ediAddress && !this.qualifier && !this.partnerCompanyId) {
            this.closeChipset();
        }
    }
    // need to clear only Company filter
    clearCompanyChipset() {
        this.clearSearchFilter();
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        if (this.selectedstatus == undefined && !this.companyName && !this.ediAddress && !this.qualifier && !this.partnerCompanyId) {
            this.closeChipset();
        }
    }
    clearCompanyIdChipset() {
        this.clearSearchFilter();
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        if (this.selectedstatus == undefined && !this.companyName && !this.ediAddress && !this.qualifier && !this.partnerCompanyId) {
            this.closeChipset();
        }
    }

    clearSearchFilter() {
        this.selectedFilters.displayName = '';
        this.selectedFilters.companyName = '';
        this.companyName = null;

        this.selectedFilters.partnerCompanyId = '';
        this.partnerCompanyId = null;

        this.ediAddress = "";
        this.qualifier = "";
        this.selectedFilters.ediAddress = "";
        this.selectedFilters.qualifier = "";

        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;

    }

    exportAll() {
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Export All");

        this.exportColumns = ['BUID', 'Company Name', 'Display Name', 'Company ID', 'Partner Type', 'Bpr Status', 'Edi Address', 'Address Line1', 'City', 'State', 'Country Code', 'Creation Date'];
        this.selectedFilters.after = 0;
        this.selectedFilters.limit = this.totalRecords;
        this.selectedFilters.exportColumns = this.exportColumns;
        this.selectedFilters.exportAll = true;
        this.tpService.listBusinessPartners(this.selectedFilters).subscribe(
            resp => {
                if (resp.responseDetails.success) {
                    this.selectedFilters.exportAll = false;
                    this.notificationHandler.notify({
                        severity: 'success',
                        title: this.translate.instant("common.exportSubmitted")
                    });
                }
            }, error => {
                this.selectedFilters.exportAll = false;
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });

    }

    sortTpData(event) {
        this.selectedFilters.sortField = event.field;
    }

    // need to clear both Company and status filter

    clearFilter() {
        this.closeChipset();
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
    }

    closeChipset() {
        this.isFilterApplied = false;
        this.selectedFilters.dateFrom = "";
        this.selectedFilters.dateTo = "";
        this.dateFrom = "";
        this.dateTo = "";
        this.companyName = "";
        this.partnerCompanyId = null;
        this.ediAddress = "";
        this.qualifier = "";
        this.selectedstatus = [];
        this.selectedFilters.displayName = "";
        this.selectedFilters.partnerCompanyId = '';
        this.selectedFilters.companyName = '';
        this.selectedFilters.ediAddress = "";
        this.selectedFilters.qualifier = "";
        this.selectedFilters.status = [];
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
    }
    showEdi(bprId: string, name: string): any {
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - View EDI ID(s)");
        this.ediCompanyName = name
        this.display = true;
        this.bprId = bprId;
        this.listEdis();
    }
    filterHasData(): boolean {
        return ((this.selectedstatus != null && this.selectedstatus.length > 0) || (this.selectedFilters.dateFrom != null || this.selectedFilters.dateFrom != null));
    }

    addTPModel(tpAdditionType: string) {
        this.showWorkflowModal = false;
        this.isBulkInvite = false;
        if (tpAdditionType === 'BULK_TP') {
            this.isBulkInvite = true;
        }
        this.onboardingService.listWorkflows(this.isBulkInvite).subscribe(data => {
            this.workflowList = data['responseDetails']['responseEntity'];
            if (this.workflowList.length < 1) {
                this.showWorkflowModal = false;
                if (this.isBulkInvite) {
                    this.notificationHandler.notify({ severity: 'info', title: this.notificationMsgs[1] });
                } else {
                    this.notificationHandler.notify({ severity: 'info', title: this.notificationMsgs[0] });
                }
            } else {
                this.showWorkflowModal = true;
            }
        }, error => {
            this.notificationHandler.notify({ title: 'Error', severity: 'warning', details: this.notificationMsgs[0] });
        });

    }

    selectImportCustomFields(){
        this.bulkImportType=Constants.BULK_CUSTOM_IMPORT;
        this.isImportCustomFields = true;
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Import custom fields");
    }

    redirectToEdit(company) {
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - TP Profile");

        this.cpService.setOnboardingHistory(null);
        if (company.partnerShipStatus == 'In Progress' || company.bprStatus === 'Error') {
            this.cpService.companyById(company.partnerCompanyId).subscribe(
                data => {
                    if (data['responseDetails']['success']) {
                        let comp = data['responseDetails']['responseEntity'];
                        let invitationCode = comp.buAdditionalCodes.filter(code => "INVITATION_CODE" == code.key);
                        this.tpdService.getOnboardingHistory(false, company.partnerCompanyName, comp.companyAddress.city, comp.companyAddress.countryCode, company.partnerCompanyId).subscribe(
                            data => {
                                this.cpService.setOnboardingHistory(data['responseDetails']['responseEntity']);
                                if ((data['responseDetails']['success'])) {
                                    this.navigateToOverview(company);
                                }
                                else {
                                    this.navigateToEdit(invitationCode, company);
                                }
                            }, () => {
                                this.navigateToEdit(invitationCode, company)
                            });

                    }
                });
        }
        else {
            this.router.navigate(['tpdir/company', company.partnerCompanyId, 'overview'],
                {
                    queryParams: { 'name': company.partnerCompanyDisplayName, 'bprId': company.bprId, 'facadeId': company.facadeId, 'companyId': company.facadeCompanyId, 'status': company.bprStatus },
                    skipLocationChange: true
                });
        }
    }

    //bpr changes
    bprCount() {
        this.selectedFilters.countOnly = true;
        this.tpService.bprCount(this.selectedFilters).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.totalRecords = data['responseDetails']['responseEntity'].itemCount;
                }
            }, error => {
                this.totalRecords = 0;
                if (error.status != 404)
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }

    listBusinessPartners() {
        if (!this.selectedFilters.limit) {
            this.selectedFilters.limit = this.filterOptions.rowsPerPage;
        }
        this.selectedFilters.countOnly = false;
        this.bplist = [];
        this.tpService.listBusinessPartners(this.selectedFilters).subscribe(
            data => {

                if (data['responseDetails']['success']) {
                    this.bplist = data['responseDetails']['responseEntity'].itemList;
                } else {
                    this.bplist = [];
                }
            }, error => {
                if (error.status != 404)
                    this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }

    editFacades(company) {
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Edit Display Name");

        let facadeDetails = new BusinessFacades();
        facadeDetails.uniqueId = company.facadeId;
        facadeDetails.bprId = company.bprId;
        facadeDetails.targetBuId = company.partnerCompanyId;
        facadeDetails.targetBuCustomName = company.partnerCompanyDisplayNameTemp;

        if (!(company.partnerCompanyDisplayNameTemp && (company.partnerCompanyDisplayNameTemp.trim()).length > 0)) {
            //this.notificationHandler.notify({ severity: 'error', title: 'Display name can not be blank' });
            if (company.facadeId) {
                this.tpService.deleteFacades(facadeDetails).subscribe(
                    data => {
                        this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
                        company.facadeId = data['responseDetails']['responseEntity'].uniqueId;
                        company.partnerCompanyDisplayName = company.partnerCompanyName;
                        company.editDispName = false;
                    },
                    error => {
                        this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
                    }
                );
            } else {
                this.notificationHandler.notify({ severity: 'success', title: this.translate.instant("tpdir.read.displayDefault") });
                company.editDispName = false;
            }
            return;
        }

        this.tpService.addUpdateFacades(facadeDetails).subscribe(
            data => {
                this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
                company.facadeId = data['responseDetails']['responseEntity'].uniqueId;
                company.editDispName = false;
                company.partnerCompanyDisplayName = company.partnerCompanyDisplayNameTemp
            },
            error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            }
        );

    }

    //view edi popup
    startFrom: number = 0;
    limit: number = 20;
    countOnly: boolean;
    totalEdiRecords: number = 0;

    getEdi() {
        // Get the total records first, before getting the list of logs
        if (this.totalEdiRecords == null) {
            this.countOnly = true;
            this.tpService.getEdis(this.bprId, this.countOnly, this.limit, this.startFrom).subscribe(
                data => {
                    if (data['responseDetails']['success']) {
                        this.totalEdiRecords = data['responseDetails']['responseEntity'].itemCount;
                        //console.log(this.totalRecords)
                        if (this.totalEdiRecords > 0)
                            this.listEdis();
                    }
                },
                error => {
                    this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
                }
            )
        } else {
            this.listEdis();
        }
    }

    listEdis() {
        this.edis = [];
        this.countOnly = false;
        this.tpService.getEdis(this.bprId, this.countOnly, /* this.limit */ 999, this.startFrom).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.edis = data['responseDetails']['responseEntity'].itemList;
                    //as not lazy loading
                    this.totalEdiRecords = this.edis.length;
                }
                else {
                    this.edis = [];
                    this.totalEdiRecords = 0;
                }
            },
            error => {
                if (error.status != 404)
                    this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            }
        )
    }

    resetSearcKeys() {
        this.selectedFilters.displayName = null;
        this.selectedFilters.companyName = null;
        this.selectedFilters.ediAddress = '';
        this.selectedFilters.partnerCompanyId = null;
    }


    workFlow: Workflow = new Workflow();
    retrieveRegData(company) {
        this.tpService.getRegistrationData(company.partnerCompanyId).subscribe(data => {
            this.showapprovetp = true;
            this.workFlow = data;
            this.tpService.setWorkFlow(this.workFlow, company.bprId);
            this.router.navigate(['./', 'tpdir', 'approval']);
        }, () => {
            this.notificationHandler.notify({ severity: 'error', details: this.translate.instant("common.workflowNotfound") });
        });
    }

    //For key bank we are validation with buAdditionCode which has INVITATION_CODE as key
    navigateToEdit(additionalKey, company) {
        if (additionalKey.length > 0) {
            this.tpService.setSelectedTPIdData(company.partnerCompanyId);
            this.router.navigate(['tpdir/base']);

        }
        else {
            this.navigateToOverview(company)
        }
    }
    navigateToOverview(company) {
        this.router.navigate(['tpdir/company', company.partnerCompanyId, 'overview'],
            {
                queryParams: { 'name': company.partnerCompanyDisplayName, 'bprId': company.bprId, 'facadeId': company.facadeId, 'companyId': company.facadeCompanyId, 'status': company.bprStatus },
                skipLocationChange: true
            });
    }
    isCancel() {
        this.selectedInvCode = '';
        this.invitationcodes.length = 0;
    }
    clearCreatedDateChispset() {
        this.resetDates();
        this.selectedFilters.dateFrom = "";
        this.selectedFilters.dateTo = "";
        this.selectedFilters.limit = this.dataTableComponent.paginatorComponent.rows;
        this.bprCount();
        this.listBusinessPartners();
        // this.listBusinessPartnersBySolr();// Solr api
        if (this.selectedstatus == undefined && !this.companyName && !this.ediAddress && !this.qualifier && !this.partnerCompanyId) {
            this.closeChipset();
        }
    }

    resetDates() {
        this.dateTo = new Date();
        let tempDate = new Date();
        tempDate.setMonth(tempDate.getMonth() - 3);
        this.dateFrom = tempDate;

    }
    onDateSelect() {
        this.dateValidation();

    }
    dateValidation() {
        if (this.datepipe.transform(this.dateFrom, 'MM/dd/yyyy') > this.datepipe.transform(this.dateTo, 'MM/dd/yyyy')) {
            this.dateValid = false;
        }
        else {
            this.dateValid = true;
        }
    }

    // ********************** SOLR CHANGES *************************
    listBusinessPartnersBySolr() {
        this.bplist = [];
        this.tpService.listBusinessPartnersBySolr(this.selectedFilters).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    let parsedData = JSON.parse(data.responseDetails.responseEntity);
                    let bprData = parsedData.response;
                    this.totalRecords = bprData.numFound;
                    this.prepareBPRData(bprData.docs);
                } else {
                    this.bplist = [];
                }
            }, error => {
                if (error.status != 404)
                    this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }

    prepareBPRData(data: any): any {
        this.bplist = [];
        if (data) {
            data.forEach(bpr => {
                this.bplist.push(new Company(bpr.BPR_BU_ID, bpr.PARTNER_COMPANY_ID, bpr.PARTNER_COMPANY_NAME, bpr.PARTNER_COMPANY_DISPLY_NAME,
                    bpr.PARTNER_COMPANY_DISPLY_ID, this.toTitleCase(bpr.BPR_STATUS), bpr.PARTNERSHIP_ACTIVATION_DATE, bpr.BPRS_ID, bpr.PARTNER_COMPANY_STATUS[0], bpr.BPR_EDI_LIST));
            });
        }
    }

    toTitleCase(inputData: any): any {
        if (inputData != '' && inputData != undefined) {
            return inputData.charAt(0) + inputData.slice(1).toLowerCase();
        }
    }

    showBprEdis(company: any): void {
        this.edis = [];
        this.ediCompanyName = company.partnerCompanyName
        this.display = true;
        this.bprId = company.bprId;
        if (company.ediAddresses && company.ediAddresses.length > 0) {
            this.totalEdiRecords = company.ediAddresses.length;
            this.edis = company.ediAddresses;
        } else {
            this.edis = [];
            this.totalEdiRecords = 0;
        }
    }
    disabled() {
        if ((this.dateFrom && this.dateTo && this.dateValid) || (this.selectedstatus != null && this.selectedstatus.length > 0)) {
            return false;
        }
        else {
            return true;
        }

    }

    getWorkflowParams(data:any){
        this.invitationId = data.invitationId;
        this.invitationName = data.invitationName;
        this.workflowId = data.workflowId;
        this.workflowDisplayName = data.displayName;
        this.isImportCustomFields = true;
        this.bulkImportType = Constants.BULK_INVITE_TYPE;
    }

    setBulkImport(data:any){
        if(data === false){
            this.isImportCustomFields=data;
        }
    }

    // For piwik to track add partner links
    handleClickEventForPiwik(data: any) {
      if(data == 'Single'){
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Add a new partner");
      }else {
        this.matomoTracker.trackPageView("Community Manager: My Trading Community - Invite multiple partners");
      }
    }

    //For piwik to track to track edit display name
    editDisplayName() {
      this.matomoTracker.trackPageView("Community Manager: My Trading Community - Edit Display Name");
    }

    handlePiwikEventTGPG() {
      this.matomoTracker.trackPageView("Community Manager: My Trading Community - TP Graph");
    }
}
