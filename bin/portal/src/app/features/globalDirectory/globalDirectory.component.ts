import { Component, OnInit, ViewChild, ChangeDetectorRef, ElementRef } from "@angular/core";
import { GlobalDirectoryService } from "../../services/global-directory.service";
import { CompanyProfileService } from "../../services/company-profile.service";
import { GlobalSearchRequest, FacetData, TreeNode } from './globalSearchRequest.entity';
import { Router, ActivatedRoute } from '@angular/router';
import { PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { OnboardingService } from '../../services/onboarding.service';
import { NotificationHandler } from '../../util/exception/notfication.handler';
import { DataTable } from "tgocp-ng/dist/components/datatable/datatable";
import { TranslateService } from '@ngx-translate/core';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { PERMISSSION } from '../../features/tradingpartner/directory/createTPMSG/entity/permission.constants';
import { MatomoTracker } from 'ngx-matomo';

// Below code needs to be cleaned as around 20% code is redundant @Rishabh needs to do it  Phase 2/2
@Component({
    styleUrls: ['./globalDirectory.css'],
    templateUrl: './globalDirectory.component.html',
    providers: [MatomoTracker]
})
export class GlobalDirectory implements OnInit {

    @ViewChild(DataTable) dataTableComponent: DataTable;
    data:any;


    permissions: any;
    isSearchClicked: boolean = true;
    filtersArray: any = [];
    isFilterClicked: boolean = false;
    filterOptions: any = {};
    nestedFilterOptions: any = {};
    company: any = {};
    companyName: string = "";
    companyList: any = [];
    isFilterApplied: boolean = false;
    bplist: any = "";
    numberOfFiltersApplied: any = "";
    filteredCompanies: any[] = [];
    filteredEDIs: any[] = [];
    filterCompany: any = "";
    edi: any = "";
    display: boolean = false;
    edis: any[];
    ediCompanyName: string;
    searchAttribute = "";
    loggedInBu = "";
    skynetUrl: string = "";
    ediCount: number = 0;
    numFound: number = 0;
    startCount: number = 0;
    limit: number = 30;
    rows: number = 30;
    displayRecordNum: number = 30;
    isRefineApplicable: boolean = true;
    selectedCompany: any = [];
    isBulkInvite: boolean;

    private lastSearchedTerm = "";
    private isRoutedFromTpDirFlag: boolean = false;
    private loadTableData = false;
    notificationMsgs: string[] = [];
    private noEDImsg: any;
    private noWFmsg: any;
    private noWrkFlowTitle: any;
    private errorOnMessageSubmit: any;
    private notificationSuccessMsg: any;

    @ViewChild('chicletContainer') chicletContainer: ElementRef;
    @ViewChild('filterScroll') filterScroll: ElementRef;
    @ViewChild('filterBody') filterBody: ElementRef;

    @ViewChild('locPsBar', { read: PerfectScrollbarDirective }) locPsBar: PerfectScrollbarDirective;
    globalFilterReq: GlobalSearchRequest = new GlobalSearchRequest();
    selectedFilterReq: GlobalSearchRequest = new GlobalSearchRequest();
    currentLanguage: string = "en";
    constructor(private cpService: CompanyProfileService, private notificationHandler: NotificationHandler,
        private onboardingService: OnboardingService, private ref: ChangeDetectorRef, private dialogueboxService: DialogueboxService,
        private router: Router, private route: ActivatedRoute,
        private globalDirectoryService: GlobalDirectoryService,
        private translate: TranslateService,
        private matomoTracker: MatomoTracker) {

    }

    ngOnInit(): void {
       this.checkAuthorization();
       this.data={
        'norecordGpd': "false",
        'linkMsg': "add the trading partner",
        'searchData': ""
        };
        this.cpService.setNoRecordGpdFlag(this.data.norecordGpd);
       //Added to track GPD Action done by users
       this.matomoTracker.trackPageView("Global Partner Directory");
       this.matomoTracker.trackEvent("Global Partner Directory", this.loggedInBu," Global Partner Directory");

    }

    private checkAuthorization(){
        this.cpService.companyGpdConsent().subscribe(response=>{
           if(response.responseDetails.responseEntity == 'No'){
                this.router.navigate(['/**']);
            } else{
                this.getRefineOptions();
        this.permissions = PERMISSSION;
                this.initializeGPD();
                this.getLoggedInUser();
                this.translate.get("tpdir.company.profile.noEDI").subscribe(res => { this.noEDImsg = res; })
                this.translate.get("globalDirectory.noWrkFlowTitle").subscribe(res => { this.noWrkFlowTitle = res; })
                this.translate.get("globalDirectory.noWrkFlow").subscribe(res => { this.noWFmsg = res; })
                this.translate.get("common.errorMessage").subscribe(res => { this.errorOnMessageSubmit = res; })
                this.translate.get("globalDirectory.notificationSuccessMsg").subscribe(res => { this.notificationSuccessMsg = res; })
                this.currentLanguage = this.translate.currentLang;
            }
        },error=>{
            this.router.navigate(['/**']);
        })
    }

    ngAfterViewInit() {
        this.dataTableComponent.paginatorComponent.activePage = this.globalDirectoryService.cached_activePage;
        this.dataTableComponent.paginatorComponent._rows = this.globalDirectoryService.cached_row;
        this.displayRecordNum = this.globalDirectoryService.cached_row;
        this.dataTableComponent.paginatorComponent.first = this.globalDirectoryService.cached_first;
        this.dataTableComponent.paginatorComponent.start = this.globalDirectoryService.cached_start;
        this.dataTableComponent.sortField = this.globalDirectoryService.cached_sortField;
        this.dataTableComponent.sortColumn = this.globalDirectoryService.cached_sortColumn;
    }

    addTPModel(tpAdditionType: string) {
        this.showWorkflowModal = false;
        this.isBulkInvite = false;
        this.cpService.setGpdFlag(true);

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

    loggedInUserEmail: string;
    private getLoggedInUser() {
        this.globalDirectoryService.getLoggedInUserDetails().subscribe(user => {
            this.loggedInUserEmail = user.responseDetails.responseEntity.contactInformation.email;
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: error.userMessage });
        })
    }

    isInternalUser: boolean = false;
    private isGxsUser(loggedInCompanyId) {
        this.globalDirectoryService.getGXScompnay().subscribe(data => {
            if (data.buId == loggedInCompanyId) {
                this.isInternalUser = true;
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: error.userMessage });
        })
    }

    displayScrollBtn: boolean = false;
    scroll_Right_chiclet() {
        this.chicletContainer.nativeElement.scrollLeft = this.chicletContainer.nativeElement.scrollLeft + 100;
    }

    filterScrl() {
        this.filterScroll.nativeElement.scrollTop = 100;
    }

    hasExceededLimit(label) {
        if (this.refineOptionsObject && Object.keys(this.refineOptionsObject).length > 0) {
            if (this.refineOptionsObject[label] && this.refineOptionsObject[label][0] && this.refineOptionsObject[label][0] && this.refineOptionsObject[label][0].children) {
                let recordsCount = 0;
                let node = this.refineOptionsObject[label];
                if (this.refineOptionsObject[label][0].children[0].children) {
                    node = this.refineOptionsObject[label][0].children;
                }
                for (let i = 0; i < node.length; i++) {
                    if (node[i].children) {
                        recordsCount += node[i].children.length;
                    }
                    if (recordsCount >= 5) {
                        return true;
                    }
                }
                return false;
            } else if (this.refineOptionsObject[label] && this.refineOptionsObject[label].length >= 5) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    scroll_Left_chiclet() {
        var scrollPosition = window.pageYOffset;
        this.chicletContainer.nativeElement.scrollLeft = this.chicletContainer.nativeElement.scrollLeft - 100;
    }

    private checkScrollWidth() {
        if (this.chicletContainer.nativeElement.scrollWidth > this.chicletContainer.nativeElement.clientWidth) {
            this.displayScrollBtn = true;
        } else {
            this.displayScrollBtn = false;
            this.expandChiclet = false;
        }
    }

    expandChiclet = false;
    expandChicletBar(){
        this.expandChiclet = !this.expandChiclet;
    }


    private setupFilterData() {
        this.selectedRefineOptions = this.globalDirectoryService.cached_selected_refineNodes;
    }
    private setupData() {//isFilterApplied
        if (this.route.snapshot.queryParamMap.has("gd")) {
            this.isRoutedFromTpDirFlag = this.loadTableData = true;
            this.setupFilterData();
            if (this.globalDirectoryService.cached_search_attribute == "bu") {
                this.searchAttribute = "companyNameRadio";
                if (this.globalDirectoryService.cached_term.endsWith("*")) {
                    let term = this.globalDirectoryService.cached_term.slice(0, this.globalDirectoryService.cached_term.length - 1);
                    this.filterCompany = { BU_NAME: term };
                    this.lastSearchedTerm = this.globalDirectoryService.cached_term;
                }
                else {
                    this.filterCompany = { id: "01", BU_POSTAL_COUNTRY: "OO", BU_NAME: this.globalDirectoryService.cached_term, ADDRESS: [], _version_: 1623291504789291000 };
                    this.lastSearchedTerm = this.globalDirectoryService.cached_term;
                }

            } else if (this.globalDirectoryService.cached_search_attribute == "edi") {
                this.searchAttribute = "ediRadio";
                this.isLastSearchedTypeEDI = true;
                if (this.globalDirectoryService.cached_term.endsWith("*")) {
                    let term = this.globalDirectoryService.cached_term.slice(0, this.globalDirectoryService.cached_term.length - 1);
                    this.edi = { term: term, facet: '1' };
                } else {
                    this.edi = { term: this.globalDirectoryService.cached_term, facet: '1' };
                }

                this.lastSearchedTerm = this.globalDirectoryService.cached_term;
            }
            if (!this.globalDirectoryService.cached_SearchPanelFlag) {
                this.isFilterClicked = true;
                this.isSearchClicked = false;
            }

            // this.companyList = this.globalDirectoryService.cached_companies;
            this.numFound = this.globalDirectoryService.cached_numFound;
            this.applyRefineCriteria();
        } else {
            this.searchDefault();
        }
    }



    private isSortField(event): string {
        let sortBy = "BU_NAME%20asc";
        if (event.sortField && event.sortField.length > 0) {
            sortBy = event.sortField;
            sortBy = sortBy.concat(event.sortOrder > 0 ? '%20asc' : '%20desc');
        }
        return sortBy;
    }

    private fetchFacadeData(company) {
        let filterReq = new GlobalSearchRequest();
        filterReq.setTerm(company.bprId);
        this.globalDirectoryService.getSuggestions(filterReq, "FACADE").subscribe(data => {
            let parsedData = JSON.parse(data.responseDetails.responseEntity);
            let val = parsedData.response.docs;
            this.globalDirectoryService.cached_companies = this.companyList;
            if (val && val.length > 0) {
                let facade = val[0];
                this.onboardingService.isGDSflag = true;
                this.router.navigate(['tpdir/company/' + company.id],
                    {
                        queryParams: {
                            'gd': 't',
                            'facadeId': facade.id,
                            'companyId': facade.FACADE_ID,
                            'name': facade.FACADE_NAME,
                            'partnerShipStatus': company.bpr_status,
                            'bprId': company.bprId,
                            'edi': this.isEdiAvailable(company)

                        },
                        skipLocationChange: true
                    });
            } else {
                this.router.navigate(['tpdir/company/' + company.id],
                    {
                        queryParams: {
                            'gd': 't',
                            'partnerShipStatus': company.bpr_status,
                            'name': company.BU_NAME,
                            'bprId': company.bprId,
                            'edi': this.isEdiAvailable(company)
                        },
                        skipLocationChange: true
                    });
            }
        }, error => {
        });


    }

    private setCache() {
        this.globalDirectoryService.cached_SearchPanelFlag = this.isFilterClicked ? false : true;
        this.globalDirectoryService.cached_globalFilterReq = this.globalFilterReq;
        this.globalDirectoryService.cached_selected_refineNodes = this.selectedRefineOptions;
        this.globalDirectoryService.refineOptions_cache = this.refineOptions;
        this.globalDirectoryService.selectedFilterType_cache = this.selectedFilterType;
        this.globalDirectoryService.FIELD_VALUES_cache = this.FIELD_VALUES;
        this.globalDirectoryService.selectedFilterReq_cache = this.selectedFilterReq;
        this.globalDirectoryService.sel_refineOption = this.refinedBy;
        this.globalDirectoryService.cached_filterOptions = this.filterOptions;
        this.globalDirectoryService.cached_sortField = this.dataTableComponent.sortField;
        this.globalDirectoryService.cached_sortColumn = this.dataTableComponent.sortColumn;
        this.globalDirectoryService.cached_nestedFilterOption = this.nestedFilterOptions;
        if (this.searchAttribute == "ediRadio") {
            if (this.edi.length > 0) {
                this.globalDirectoryService.cached_term = this.lastSearchedTerm;
                this.globalDirectoryService.cached_search_attribute = "edi";
            } else {
                this.globalDirectoryService.cached_term = this.lastSearchedTerm;
                this.globalDirectoryService.cached_search_attribute = "edi";
            }
        } else {
            this.globalDirectoryService.cached_search_attribute = "bu";
            this.globalDirectoryService.cached_term = this.lastSearchedTerm;
        }
        this.globalDirectoryService.cached_numFound = this.numFound;
        //cached paginator info
        this.globalDirectoryService.cached_activePage = this.dataTableComponent.paginatorComponent.activePage;
        this.globalDirectoryService.cached_row = this.dataTableComponent.paginatorComponent._rows;
        this.globalDirectoryService.cached_start = this.dataTableComponent.paginatorComponent.start;
        if (this.globalDirectoryService.cached_activePage != 1) {
            this.globalDirectoryService.cached_first = this.dataTableComponent.paginatorComponent.first;
        } else {
            this.globalDirectoryService.cached_first = 0;
        }

    }
    // fix below code
    private passDataToCompanyOverview(company) {
        if (company.ADDRESS) {
            this.cpService.hasEdiOrDocs = true;
            this.onboardingService.EDI_ADDRESS = company.ADDRESS;
        } else {
            this.onboardingService.EDI_ADDRESS = [];
        } if (company.DOCUMENT_TYPE) {
            this.cpService.hasEdiOrDocs = true;
            this.onboardingService.DOCUMENTS = company.DOCUMENT_TYPE;
        } else {
            this.onboardingService.DOCUMENTS = [];
        }
    }
    public redirectToCompany(company) {
        this.setCache();
        this.presetOnboardingData(company.id, false);
        this.passDataToCompanyOverview(company);
        if (!company.bpr) {
            this.onboardingService.isGDSflag = true;
            // Need to fetch FACADE IF decided to navigate to TPDIR for NON BPR compnaies too
            this.router.navigate(['tpdir/company/' + company.id], { queryParams: { 'gd': 't', 'name': company.BU_NAME, 'privateView': 'true', 'edi': this.isEdiAvailable(company) }, skipLocationChange: true });
        } else {
            this.fetchFacadeData(company);
        }

    }
    clearFilters() {
        this.filterOptions = {};
        this.companyList = [];
        this.isFilterApplied = false;
        this.edi = "";
        this.refinedBy = "";
        this.lastSearchedTerm = this.getBuTerm();
        this.filtersArray = [];
        this.numFound = 0;
        this.selectedFilterReq = new GlobalSearchRequest();
        this.selectedRefineOptions = [];
        this.searchDefault();
    }

    totalEdiRecords: number = 0;
    showEdi(company: any, input: string): any {
        this.edis = [];
        if (input == 'edi') {
            this.ediCompanyName = company.BU_NAME;
            this.edis.push.apply(this.edis, company.ADDRESS);
        }
        else {
            this.ediCompanyName = company.BU_NAME;
            this.edis.push.apply(this.edis, company.DOCUMENT_TYPE);
        }
        this.totalEdiRecords = this.edis.length;
        this.display = true;
    }


    displaySearchPanel() {//this.edi = {term:this.globalDirectoryService.cached_term,facet:'1'};
        this.syncSelectedToolBarItem("search");

        if (this.filterCompany.id) {
            this.filterCompany = { id: "01", BU_POSTAL_COUNTRY: "OO", BU_NAME: this.filterCompany.BU_NAME, ADDRESS: [], _version_: 1623291504789291000 }
        } else {
            this.filterCompany = { BU_NAME: this.filterCompany.hasOwnProperty("BU_NAME") ? this.filterCompany.BU_NAME : this.filterCompany }
        }
        if (!this.edi.facet) {
            this.edi = { term: this.edi, facet: '1' };
        }
    }




    hideFilterPanel() {
        this.isFilterClicked = !this.isFilterClicked;
        this.isSearchClicked = false;
        if (!this.isFilterClicked) {
            // this.globalFilterReq.clearFilterData();
            // this.selectedFilterReq.clearFilterData();
            this.filterOptions = {};
            this.companyList = [];
            this.clearFields();
            this.numFound = 0;
        }
    }

    clearFields() {
        this.edi = "";
        this.filterCompany = "";
        this.startCount = 0;
        this.selectedRefineOptions = [];
        this.lastSearchedTerm = this.getBuTerm();
        this.filterOptions = {};
        // this.numFound = 0;
    }



    clearSearchCriteria() {
        this.clearFields();
        this.refinedBy = "";
        this.selectedFilterReq = new GlobalSearchRequest();
        this.selectedRefineOptions = [];
        this.filtersArray = [];
        this.isFilterApplied = false;
        this.isSearchClicked = false;
        this.synchDragSettings();
        this.searchDefault();
        // this.filterOptions = {};
    }

    isEdiAvailable(company) {
        if (!company.ADDRESS || company.ADDRESS.length == 0) {
            return false;
        }
        return true;
    }

    showWorkflowModal: boolean;
    workflowList: any;
    connect(company) {

        if (!this.isEdiAvailable(company)) {
            this.notificationHandler.notify({ severity: 'info', title: this.noEDImsg });
            return;
        }
        this.presetOnboardingData(company.id, true);
        this.onboardingService.setSkynetUrl_params("&buid=" + company.id + "&bu_name=" + company.BU_NAME);
        this.cpService.setPrivateView(true);
        this.setCache();
        this.globalDirectoryService.cached_companies = this.companyList;
        this.showWorkflowModal = false;
        this.onboardingService.listWorkflows().subscribe(data => {
            this.workflowList = data['responseDetails']['responseEntity'];
            if (this.workflowList.length < 1) {
                this.notificationHandler.notify({ severity: 'info', title: this.noWrkFlowTitle , details:  this.noWFmsg});
            } else {
                this.onboardingService.isGDSflag = true;
                this.showWorkflowModal = true;
            }
        },
            error => {
                this.notificationHandler.notify({ severity: 'error', title: error.userMessage });
            });

    }
    isEnterPressed: boolean = false;
    public pressEnterKey(event) {
        if (event.keyCode == 13 && this.isTermValid()) {
            this.searchBU();
        }
    }

    clearChipsetFLAG = false;
    isLastSearchedTypeEDI = false;

    clearRefineValues() {
        this.refinedBy = "";
        this.filtersArray = [];
        this.selectedRefineOptions = [];
        this.filterOptions = {};
        this.selectedFilterReq = new GlobalSearchRequest;
    }

    private searchDefault() {
        let filterReq = this.getRefineFilterReq(false);
        filterReq.setTerm("");
        this.lastSearchedTerm = this.getBuTerm();
        this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
    }
    searchBU() {
        this.isEnterPressed = true;
        this.clearRefineValues();
        this.filtersArray = [];
        this.ediSelected = false;
        this.isFilterApplied = false;
        this.isSearchClicked = false;
        this.selectedRefineOptions = [];
        this.dataTableComponent.paginatorComponent.activePage = 1;
        this.dataTableComponent.paginatorComponent.start = 1;
        this.lastSearchedTerm = this.getSearchTerm()
        let filterReq = this.getRefineFilterReq(false);

        filterReq.appliedFilters = null;
        this.refinedBy = null;
        this.filterOptions = {};
        this.selectedFilterReq = new GlobalSearchRequest();
        this.selectedFilterReq.clearFilterData();
        this.globalDirectoryService.cached_isGroupBy = true;
        this.synchDragSettings();
        this.cpService.setEdi(this.edi)
        this.cpService.setCompanyName(this.filterCompany)
        this.cpService.setGpdFlag(true);


        // this.cpService.setPrivateView(true);

        if (filterReq.getSearchBy() == "EDI") {
            this.isLastSearchedTypeEDI = true;
            filterReq.setIsWildCardSearch(true);
            this.fetchFacetSuggestions(filterReq, "ADDRESS", this.loadDataByEdiAndUpdateTable);
        }
        else {
            this.isLastSearchedTypeEDI = false;
            filterReq.setIsWildCardSearch(true);
            this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
        }
    }

    onCompanySelect() {
        this.clearRefineValues();
        this.isLastSearchedTypeEDI = false;
        this.isFilterApplied = false;
        this.isSearchClicked = false;
        let filterReq = new GlobalSearchRequest();
        filterReq.setTerm(this.filterCompany.BU_NAME.trim());
        this.lastSearchedTerm = filterReq.getTerm();
        this.globalDirectoryService.cached_isGroupBy = false;
        filterReq.setSearchBy("BU");
        this.synchDragSettings();
        this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
    }

    presetOnboardingData(buid, isDirectConnect) {
        this.onboardingService.gdsCompanyId = buid;
        if (this.companyList.length == 1 && this.isLastSearchedTypeEDI && isDirectConnect && this.edi.facet && this.edi.facet != "1") {
            this.cpService.selectedEdi = this.lastSearchedTerm.endsWith("*") ? this.lastSearchedTerm.substring(0, this.lastSearchedTerm.length - 1) : this.lastSearchedTerm;
        } else {
            this.cpService.selectedEdi = "";
        }
        this.cpService.selectedBuId = buid;
        // this.cpService.selectedEdi
    }

    ediSelected: boolean = false;
    onEDIselect() {
        this.isEnterPressed = false;
        this.clearRefineValues();
        this.ediSelected = true;
        this.isFilterApplied = false;
        this.isSearchClicked = false;
        let filterReq = new GlobalSearchRequest();
        filterReq.setTerm(this.getEdiTerm());
        this.lastSearchedTerm = filterReq.getTerm(); // TODO
        this.isLastSearchedTypeEDI = true; // TODO
        this.globalDirectoryService.cached_isGroupBy = false;
        this.synchDragSettings();
        // this.cpService.selectedEdi = filterReq.getTerm();
        this.fetchFacetSuggestions(filterReq, "ADDRESS", this.loadDataByEdiAndUpdateTable);
    }

    // Autocomplete Companies
    filterCompanybyBUName(event) {
        if (event.query.trim().length > 3) {
            // this.filterFlag = true;
            this.startCount = 30;
            let filterReq = new GlobalSearchRequest();
            filterReq.setIsWildCardSearch(true);
            filterReq.setSearchBy("BU");
            filterReq.setTerm(event.query.trim());
            filterReq.queryConditions = "fl=BU_NAME";
            this.fetchCompanySuggestions(filterReq, this.buNameAutoComplete);
        }
    }

    // Autocomplete EDIs
    filterEDIs(event) {
        if (event.query.trim().length > 3) {
            // this.filterFlag = true;
            this.startCount = 30;
            let filterReq = new GlobalSearchRequest();
            filterReq.setTerm(event.query.trim().toUpperCase());
            filterReq.setIsWildCardSearch(true);
            filterReq.queryConditions = "rows=0"
            this.fetchFacetSuggestions(filterReq, "ADDRESS", this.ediAutoComplete);
        }
    }

    disableFilter() {
        return this.lastSearchedTerm.length > 1 ? true : false;
    }

    private getFieldValue(fieldToCheck) {
        if (fieldToCheck && fieldToCheck.indexOf("__") > 0) {
            return fieldToCheck.split("__")[0];
        } else {
            return fieldToCheck;
        }
    }

    selectedFilterType: any;
    __field: any = "";
    isFirstTimeRefine: boolean = true;

    applyRefineCriteria() {
        //this.tempInnerFiler();
        this.isFirstTimeRefine = false;
        this.formChiclet();
        let filterReq = this.getRefineReqObj(); //this.getCurrentSearchTerm(null);
        // this.formChipset(filterReq);
        // filterReq = this.getRefineFilterReq(false);
        this.lastSearchedTerm;
        let tempFilter = this.getCurrentSearchTerm(null);
        filterReq.setTerm(tempFilter.getTerm());
        filterReq.setSearchBy(tempFilter.getSearchBy());
        this.globalFilterReq = filterReq;

        if (!this.loadTableData) {
            this.dataTableComponent.paginatorComponent.activePage = 1;
            this.dataTableComponent.paginatorComponent.start = 1;
            this.isFilterApplied = true;
        } else {
            filterReq.setStartIndex(this.globalDirectoryService.cached_first);
            filterReq.setIsWildCardSearch(this.globalDirectoryService.cached_isGroupBy);
            if (filterReq.appliedFilters && filterReq.appliedFilters.length > 0) {
                this.isFilterApplied = true;
            }
        }
        this.loadTableData = false;
        if (this.edi.length > 0 || (this.edi.term && this.edi.term.length > 0)) {
            this.fetchFacetSuggestions(filterReq, "ADDRESS", this.loadDataByEdiAndUpdateTable);
        } else {
            this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
        }
        //
    }

    private isfilterExist(filterReq: GlobalSearchRequest, filterName) {
        if (filterReq.appliedFilters) {
            for (let i = 0; i < filterReq.appliedFilters.length; i++) {
                if (filterReq.appliedFilters[i][filterName]) {
                    return true;
                }
            }
            return false;

        }

    }
    private getRefineFilterReq(isCompleteField): GlobalSearchRequest {
        let filterReq = new GlobalSearchRequest();
        filterReq.setTerm(this.getSearchTerm());
        filterReq.setSearchBy(this.getSearchBY());
        if (filterReq.getSearchBy() == "BU") {
            filterReq.setIsWildCardSearch(true);
        }
        for (let i = 0; i < this.FIELD_VALUES.length; i++) {
            let val = this.FIELD_VALUES[i];
            if (val != "0" && this.selectedFilterReq[val] && this.selectedFilterReq[val].length > 0) {
                if (!this.isfilterExist(filterReq, val)) {
                    let fieldVal = this.getFieldValue(val);
                    if (isCompleteField) {
                        filterReq.appliedFilters.push({ [val]: this.selectedFilterReq[val] });
                    } else {
                        filterReq.appliedFilters.push({ [fieldVal]: this.selectedFilterReq[val] });
                    }

                }
                // break;
            }
        }
        return filterReq;
    }

    // Current Filter by search term  || Need revist again
    private getCurrentSearchTerm(searchedTerm: string): GlobalSearchRequest {
        let filterReq = new GlobalSearchRequest();
        if (this.edi.length > 0 || this.edi.term) {
            filterReq.setSearchBy("EDI");
            filterReq.setTerm(searchedTerm != null ? searchedTerm : this.lastSearchedTerm);
        } else {
            filterReq.setTerm(searchedTerm != null ? searchedTerm : this.lastSearchedTerm);
            filterReq.setSearchBy("BU");
        }
        return filterReq;
    }

    //On scroll End of Countries
    companyArrayCount: number = 0;
    onCompanyScrollEnd() {
        if (this.startCount < this.companyArrayCount) {
            let filterReq = new GlobalSearchRequest();
            filterReq.setTerm(this.filterCompany);
            filterReq.setSearchBy("BU");
            filterReq.setIsWildCardSearch(true);
            filterReq.setStartIndex(this.startCount);
            this.fetchCompanySuggestions(filterReq, this.updateBuNameAutoComplete);
        } else {
            this.notificationHandler.notify({ severity: 'info', title: 'Reached end of results' });
        }

    }

    onEDIscrollEnd() {
        if (this.startCount < this.ediCount) {
            let filterReq = new GlobalSearchRequest();
            filterReq.setTerm(this.edi.toUpperCase());
            filterReq.setStartIndex(this.startCount);
            filterReq.setIsWildCardSearch(true);
            filterReq.setSearchBy("EDI");
            filterReq.queryConditions = "rows=0"
            this.fetchFacetSuggestions(filterReq, "ADDRESS", this.updateEdiAutoComplete);
        } else {
            this.notificationHandler.notify({ severity: 'info', title: 'Reached end of results' });
        }
    }
    // REST CALL TO #SOLR
    private fetchCompanySuggestions(term: GlobalSearchRequest, callback: (data: any, num: any) => void) {
        term.setLimit(this.limit);
        this.globalDirectoryService.getSuggestions(term, "BU_NAME").subscribe(data => {
            let parsedData = JSON.parse(data.responseDetails.responseEntity);
            let val = parsedData.response.docs;
            if(val == null || val.length == 0){
                this.data.norecordGpd="true";
                this.data.searchData=this.filterCompany;
                this.cpService.setNoRecordGpdFlag(this.data.norecordGpd);


            }
            else{
                this.data.norecordGpd = false;
                this.cpService.setNoRecordGpdFlag(this.data.norecordGpd);
            }
            callback(val, parsedData.response.numFound);
            this.ref.detectChanges();
        }, error => {
        })
    }

    // REST CALL TO #SOLR
    fetchFacetSuggestions(term: GlobalSearchRequest, FIELD: any, callback: (data: any) => void) {
        term.setLimit(this.limit);
        term.queryConditions = term.queryConditions.length == 0 ? 'rows=' + this.limit : term.queryConditions;
        this.globalDirectoryService.getSuggestions(term, FIELD).subscribe(data => {
            let parsedData = JSON.parse(data.responseDetails.responseEntity);
            let val = parsedData.facet_counts.facet_fields[FIELD];
            if(val == null || val.length == 0){
                this.data.norecordGpd="true";
                this.cpService.setNoRecordGpdFlag(this.data.norecordGpd);
                this.data.searchData=this.edi;

            }else{
                this.data.norecordGpd = false;
                this.cpService.setNoRecordGpdFlag(this.data.norecordGpd);
            }
            // this.ediCount = parsedData.facets.count;
            callback(parsedData);
            this.ref.detectChanges();
        }, error => {
        })
    }

    refineOptions: any;
    refinedBy: any;
    FIELD_VALUES: any[] = [];
    private getRefineOptions() {
        this.globalDirectoryService.getRefineOptions().subscribe(data => {
            if (data) {
                this.refineOptions = [];
                data.forEach(element => {
                    let obj = { "label": "", "value": "" };
                    obj.label = element.field_name;
                    obj.value = element.field_value;
                    this.FIELD_VALUES.push(obj.value);
                    this.refineOptions.push(obj);
                });

                if (this.refineOptions.length > 0) {
                    this.refineOptions.sort(function (a, b) { return a.label.localeCompare(b.label) });
                }
                this.loadFiltersForAllcategories(this.refineOptions);
            }
        })
    }

    //  Call Back methods #BU NAME
    private updateBuNameAutoComplete = (companyArray: any) => {
        this.filteredCompanies.push.apply(this.filteredCompanies, companyArray);
        this.startCount += this.rows;
    }
    private tablePaginationData = (companyArray: any) => {
        this.companyList = this.constructCompanyList(companyArray);
    }
    private loadCompanyDataAndUpdateTable = (companyArray, companyArraySize) => {
        this.numFound = companyArraySize;
        this.companyArrayCount = companyArraySize;
        this.companyList = this.constructCompanyList(companyArray);
        this.checkScrollWidth();
        this.ref.detectChanges();
    }
    private buNameAutoComplete = (companyArray, companyArraySize) => {
        this.companyArrayCount = companyArraySize;
        this.filteredCompanies = companyArray;
    }


    // # EDI
    private loadDataByEdiAndUpdateTable = (parsedData) => {
        this.numFound = parsedData.response.numFound;
        this.companyList = this.constructCompanyList(parsedData.response.docs);
        this.companyArrayCount = this.companyList.length;
        this.checkScrollWidth();
        this.ref.detectChanges();
    }

    private ediAutoComplete = (parsedData) => {
        this.ediCount = parsedData.facets.count;
        this.filteredEDIs = this.removeCountFromEDIdata(parsedData.facet_counts.facet_fields.ADDRESS);
        if (this.filteredEDIs.length > 0 && this.filteredEDIs[this.filteredEDIs.length - 1].facet == 'EOR') {
            this.filteredEDIs.pop();
        }
        // this.filteredEDIs.pop();
    }
    private updateEdiAutoComplete = (parsedData: any) => {
        this.filteredEDIs.push.apply(this.filteredEDIs, this.removeCountFromEDIdata(parsedData.facet_counts.facet_fields.ADDRESS));
        if (this.filteredEDIs.length > 0 && this.filteredEDIs[this.filteredEDIs.length - 1].facet == 'EOR') {
            this.filteredEDIs.pop();
        }
        this.startCount += this.rows;
    }


    // utility meothds
    private constructCompanyList(docs: any[]) {
        for (let i = 0; i < docs.length; i++) {
            // check if loggedIn BU has BPR with fetched BU
            if (docs[i].BPR_RESULTS) {
                for (let j = 0; j < docs[i].BPR_RESULTS.length; j++) {
                    if (docs[i].BPR_RESULTS[j].indexOf(this.loggedInBu) > -1 && docs[i].BPR_RESULTS[j].indexOf('APPROVE') > -1) {
                        docs[i].bpr = true;
                        docs[i].bpr_status = docs[i].BPR_RESULTS[j].split(";")[3];
                        docs[i].bprId = docs[i].BPR_RESULTS[j].split(";")[0];
                        break;
                    } else {
                        docs[i].bpr = false;
                    }
                }
            }
        }
        //this check is just to verify that DB has this column or not
        if(docs[0] && docs[0].CUSTOMER_BU_GPD_VISIBILITY){
            docs = docs.filter(data => data.CUSTOMER_BU_GPD_VISIBILITY == "Y");
        }

        return docs;
    }

    private getSearchTerm(): string {
        let returnTerm = "";
        if (this.searchAttribute == "ediRadio") {
            returnTerm = this.getEdiTerm().toUpperCase();
        } else {
            returnTerm = this.getBuTerm();
        }

        if (returnTerm.trim().length == 0) {
            return "";
        }
        return returnTerm;
    }

    private getSearchBY(): string {
        if (this.searchAttribute == "ediRadio" && this.getEdiTerm().length > 1 && this.getEdiTerm() == this.lastSearchedTerm) {
            return "EDI";
        } else {
            return "BU";
        }
    }

    private getEdiTerm() {
        if (this.edi.facet && !this.isEnterPressed) {
            return this.edi.term.trim().toUpperCase();
        } else if (this.edi.facet && this.isEnterPressed) {
            return this.getActualTerm(this.edi.term.trim().toUpperCase());
        } else {
            return this.getActualTerm(this.edi.trim().toUpperCase());
        }
    }

    private getBuTerm(): string {
        if (this.filterCompany.id) {
            return this.filterCompany.BU_NAME.trim();
        } else {
            return typeof (this.filterCompany) === "string" ? this.getActualTerm(this.filterCompany.trim()) : this.getActualTerm(this.filterCompany.BU_NAME.trim());
        }
    }

    private getActualTerm(term) {
        if (term.length == 0) {
            return "";
        } else {
            return term;
        }
    }
    private isTermValid() {
        if (this.getBuTerm() && this.getBuTerm().trim().length > 1) {
            return true;
        } else if (this.getEdiTerm() && this.getEdiTerm().trim().length > 1) {
            return true;
        }
        return false;
    }

    private initializeGPD() {
        this.globalDirectoryService.getLoggedInCompanyDetails().subscribe(data => {
            if (null != data && data.success == true) {
                this.loggedInBu = data.responseEntity.companyId;
                this.isGxsUser(this.loggedInBu);
                this.setupData();
            }
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: this.errorOnMessageSubmit });
        })
    }


    private removeCountFromEDIdata(ediData): any[] {
        let ediValArray = [];
        for (let i = 0; i < ediData.length; i += 2) {

            if (ediData[i + 1] == 0) {
                let info2 = new FacetData();
                info2.term = "----End of Records----";
                info2.facet = "EOR";
                ediValArray.push(info2);
                break;
            }
            let info = new FacetData();
            info.term = ediData[i];
            info.facet = ediData[i] + " (" + ediData[i + 1] + ")";
            ediValArray.push(info);
        }
        if (ediValArray.length > 0 && ediValArray[0].facet == "EOR") {
            ediValArray.pop();
        }
        return ediValArray;
    }

    displayFilterPanel() {
        this.syncSelectedToolBarItem("filter");
        this.ref.detectChanges();
        if (this.companyList) {
            this.isRefineApplicable = this.companyList.length > 0 ? true : false;
        }
        this.loadFiltersForAllcategories(this.refineOptions);
    }

    isRowSelected: boolean = false;
    onTableRowSelect() {
        if (this.selectedCompany.length > 0) {
            this.isRowSelected = true;
        } else {
            this.isRowSelected = false;
        }
    }


    isMessageClicked: boolean = false;
    onMessageItemClick() {
        this.syncSelectedToolBarItem("message");
        this.dialogueboxService.confirm({
            dialogName: 'incompleteSurvey',
            accept: () => {
                let buidArray = [];
                buidArray.push(this.loggedInUserEmail);
                buidArray = buidArray.concat(this.selectedCompany.map(data => data.id));
                // console.log(buidArray);
                this.globalDirectoryService.sendMessageToAllBu(buidArray).subscribe(response => {
                    if (response.success) {
                        this.selectedCompany = [];
                        this.isRowSelected = false;
                        this.notificationHandler.notify({ severity: 'success', title: this.notificationSuccessMsg });
                    } else {
                        this.notificationHandler.notify({ severity: 'error', title: this.errorOnMessageSubmit });
                    }
                });
            }
            , reject: () => {
            }
        });
    }

    dragVisible=true;
    private syncSelectedToolBarItem(selectedItem) {
        if (selectedItem == "search") {
            this.isSearchClicked = !this.isSearchClicked;
            this.isFilterClicked = false;
            this.isMessageClicked = false;
        } else if (selectedItem == "filter") {
            this.isFilterClicked = !this.isFilterClicked;
            this.isSearchClicked = false;
            this.isMessageClicked = false;
        } else {
            this.isMessageClicked = !this.isMessageClicked;
            this.isSearchClicked = false;
            this.isFilterClicked = false;
        }

        this.synchDragSettings();

    }

    private synchDragSettings(){
        if(!this.isFilterClicked && !this.isSearchClicked){
            this.tableWidth = "100%";
            this.dragVisible = false;
        }else{
            if(this.tableWidth == "100%"){
                let wid = this.tableDragWidth == 0 ? 240:this.tableDragWidth;
               this.tableWidth="calc(100% - "+wid+"px)";
            }
            this.dragVisible=true;
        }
    }

    addTp(event){
        this.showWorkflowModal = false;
        this.isBulkInvite = false;
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
    // new edition

    // Table Pagination
    public paginationEvent(event) {
        this.limit = event.rows;
        if (!this.isRoutedFromTpDirFlag && event.sortField != undefined) {
            let filterReq = this.getRefineReqObj();
            filterReq.setStartIndex(event.first);
            filterReq.setSortField(this.isSortField(event));
            filterReq.setLimit(event.rows);
            let filterObjTEMP = this.getCurrentSearchTerm(null);
            if (filterObjTEMP.getSearchBy() == "EDI") {
                filterReq.setTerm(filterObjTEMP.getTerm().toUpperCase());
                this.fetchFacetSuggestions(filterReq, "ADDRESS", this.loadDataByEdiAndUpdateTable);
            } else {
                filterReq.setTerm(filterObjTEMP.getTerm());
                this.fetchCompanySuggestions(filterReq, this.tablePaginationData);
            }
        }
        this.isRoutedFromTpDirFlag = false;
    }

    selectedFilterCategoryTab = [];
    selectFilterCategory(label) {
        if (this.selectedFilterCategoryTab.indexOf(label) >= 0) {
            this.selectedFilterCategoryTab = this.selectedFilterCategoryTab.filter(data => label != data);
        } else {
            this.selectedFilterCategoryTab = [];
            this.selectedFilterCategoryTab.push(label);
            // if (!this.refineOptionsObject[label])
            let selectedTab = this.refineOptions.filter(data=> data.label == label);
            this.loadFiltersForAllcategories(selectedTab);
        }
        this.filterScrl();
    }

    showMoreClickedtabs = []
    isShowMoreExpanded(tabIndex) {
        // console.log(this.filterScroll);
        if (this.showMoreClickedtabs.filter(index => index == tabIndex)[0] >= 0) {
            return true;
        } else {
            return false;
        }
    }

    showMore(tabIndex) {
        if (this.showMoreClickedtabs.indexOf(tabIndex) >= 0)
            this.showMoreClickedtabs = this.showMoreClickedtabs.filter(index => index != tabIndex);
        else
            this.showMoreClickedtabs.push(tabIndex);

        console.log(this.showMoreClickedtabs);
        this.filterScrl();
        this.ref.detectChanges();
    }

    loadFiltersForAllcategoriesInit(categoryArray) {
        // categoryArray.forEach(category => {
        //     let filterReq = this.getRefineReqObj();
        //     filterReq.setTerm(this.lastSearchedTerm.length == 0 ? "*" : this.lastSearchedTerm);
        //     filterReq.facetData = category.label;
        //     filterReq.setIsWildCardSearch(this.globalDirectoryService.cached_isGroupBy);
        //     this.fetchRefineOptions(filterReq, category.value);

        // })
    }

    // treefying filters
    loadFiltersForAllcategories(categoryArray) {

        this.selectedFilterCategoryTab.forEach(label=>{
            let selFilterValue = categoryArray.filter(data=>data.label == label);
            if(selFilterValue[0]){
                let selFilterVal = selFilterValue[0].value;
                let filterReq = this.getRefineReqObj();
                filterReq.setTerm(this.lastSearchedTerm.length==0 ? "":this.lastSearchedTerm);
                filterReq.facetData = label;
                filterReq.queryConditions="rows=0";
                this.fetchRefineOptions(filterReq, selFilterVal);
            }
        });
        console.log(this.refineOptionsObject);
    }

    refineOptionsObject: any = {};
    private fetchRefineOptions(term: GlobalSearchRequest, field) {
        term.setLimit(3000);
        this.globalDirectoryService.getSuggestions(term, field).subscribe(data => {
            let parsedData = JSON.parse(data.responseDetails.responseEntity);
            let facetValue = parsedData.facet_counts.facet_fields[field];
            // restructuring data according to tree component
            this.refineOptionsObject[term.facetData] = this.restructureFilterValues(facetValue, field, term.facetData);

        }, error => {
            this.filterOptions = {};
            // this.notificationHandler.notify({ severity: 'info', title: 'No filters available' });
        })
    }

    private fetchRefineOptions_children(term: GlobalSearchRequest, field, parentNode: TreeNode) {
        term.setLimit(this.limit);
        this.globalDirectoryService.getSuggestions(term, field).subscribe(data => {
            let parsedData = JSON.parse(data.responseDetails.responseEntity);
            let facetValue = parsedData.facet_counts.facet_fields[field];
            parentNode.children = this.nestedRefineOptions(facetValue, field);
        }, error => {
            this.filterOptions = {};
        })
    }

    showme() {
        this.filterScrl();
    }

    private getSolrFieldName(value) {
        return value.replace(new RegExp(' ', 'g'), '_').concat('_FIELD');
    }

    selectedRefineOptions: any = [];
    private restructureFilterValues(filterData, field, fieldDisplayValue) {
        let refineOptionsArray = [];
        for (let i = 0; i < filterData.length; i += 2) {
            let element = filterData[i];
            let node = new TreeNode();

            if (field.endsWith('FIELD_LIST')) {
                if (element.indexOf("OBJECT") >= 0) {

                    let processedFilter = [];
                    for (let indx = 0; indx < filterData.length; indx += 2) {

                        let objectRoot = filterData[indx].split('=')[0];
                        if (processedFilter.indexOf(objectRoot) < 0) {
                            processedFilter.push(objectRoot);
                            let objectArray = [];
                            for (let k = 0; k < filterData.length; k += 2) {
                                if (filterData[k].startsWith(objectRoot)) {
                                    objectArray = objectArray.concat(filterData[k].split('=')[1].split(','));
                                }
                            }
                            const uniqSet = new Set(objectArray);
                            let filterNamesArr = Array.from(uniqSet);
                            filterNamesArr = filterNamesArr.filter(item => item != '');
                            let perentNode = new TreeNode();
                            perentNode.isHeading = true;
                            perentNode.children = [];
                            perentNode.label = objectRoot.split(':')[1];
                            filterNamesArr.sort();
                            filterNamesArr.forEach(value => {
                                let childNode = new TreeNode();
                                childNode.isHeading = true;
                                childNode.label = value;
                                let filterReq = this.getRefineReqObj();
                                filterReq.setTerm(this.lastSearchedTerm);
                                filterReq.queryConditions = "rows=0";
                                this.fetchRefineOptions_children(filterReq, this.getSolrFieldName(value), childNode);
                                perentNode.children.push(childNode);
                            });
                            refineOptionsArray.push(perentNode);
                        }
                    }
                    break;
                } else {
                    let filterNamesArr = this.getUniquefilterList(filterData);
                    this.fetchSectionalFilters(filterNamesArr, refineOptionsArray);
                    break;
                }
            } else {
                node.label = element;
                node.data = field;
                node.dataLabel = fieldDisplayValue;
                node.key = node.label + field;
                node.countLabel = filterData[i + 1];
                refineOptionsArray.push(node);
            }
        }
        return refineOptionsArray;
    }

    private getUniquefilterList(filterData): any[] {
        let masterArray = [];
        for (let ite = 0; ite < filterData.length; ite += 2) {
            masterArray = masterArray.concat(filterData[ite].split(','));
        }
        const uniqSet = new Set(masterArray);
        let filterNamesArr = Array.from(uniqSet);
        return filterNamesArr;
    }

    private fetchSectionalFilters(filterNamesArr, refineOptionsArray) {
        filterNamesArr.sort();
        filterNamesArr.forEach(element => {
            let perentNode = new TreeNode();
            perentNode.isHeading = true;
            // perentNode.data = this.getSolrFieldName(element);
            perentNode.label = element;
            let filterReq = this.getRefineReqObj();
            filterReq.setTerm(this.lastSearchedTerm);
            filterReq.queryConditions = "rows=0";
            this.fetchRefineOptions_children(filterReq, this.getSolrFieldName(element), perentNode);
            refineOptionsArray.push(perentNode);
        });

    }

    private nestedRefineOptions(filterData, field): any[] {
        let nestedRefineOptionsArray = [];
        for (let i = 0; i < filterData.length; i += 2) {
            let node = new TreeNode();
            node.data = field;
            node.label = filterData[i];
            node.countLabel = filterData[i + 1];
            node.key = node.label + field;
            nestedRefineOptionsArray.push(node);
        }
        return nestedRefineOptionsArray;
    }


    onRefineOptionSelect(event) {
        // console.log(event.node.data);
        if (event) {
            let array = this.refineOptions.filter(item => item.value != event.node.data);
            this.loadFiltersForAllcategories(array);
        } else {
            this.loadFiltersForAllcategories(this.refineOptions);
        }

        // console.log(array,this.refineOptions);
        let filterReq = this.getRefineReqObj();

        this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
        this.formChiclet();
    }

    onRefineOptionUnSelect(event) {
        if (event) {
            let array = this.refineOptions.filter(item => item.value != event.node.data);
            this.loadFiltersForAllcategories(array);
        } else {
            this.loadFiltersForAllcategories(this.refineOptions);
        }
        let filterReq = this.getRefineReqObj();
        this.fetchCompanySuggestions(filterReq, this.loadCompanyDataAndUpdateTable);
        this.formChiclet();
    }




    // making of chiclet new Gen
    private formChiclet() {
        let hashmap = [];
        this.isFilterApplied = true;
        this.filtersArray = [];
        if (this.selectedRefineOptions) {
            this.selectedRefineOptions.forEach(selectedNode => {
                if (hashmap.indexOf(selectedNode.data) < 0) {
                    let filterArray = this.selectedRefineOptions.filter(tempNode => tempNode.data === selectedNode.data);
                    hashmap.push(selectedNode.data);
                    filterArray[0].data
                    // console.log(selectedNode);
                    if (selectedNode.parent)
                        this.filtersArray.push({ name: selectedNode.parent.label, value: filterArray.flatMap(info => info.label), field: [filterArray[0].data].toString() });
                    else
                        this.filtersArray.push({ name: selectedNode.dataLabel, value: filterArray.flatMap(info => info.label), field: [filterArray[0].data].toString() });
                }
            });
            if (this.filtersArray.length > 0) {
                this.isFilterApplied = true;
            } else {
                this.isFilterApplied = false;
            }
        }

    }

    // clearChiclet(chiclet) {
    //     console.log(chiclet, this.selectedRefineOptions);
    //     this.selectedRefineOptions = this.selectedRefineOptions.filter(info => chiclet.field != info.data);
    //     console.log(this.selectedRefineOptions);
    //     this.onRefineOptionSelect(undefined);

    // }

    clearChiclet(chicletField,chicletValue) {
        console.log(chicletField,chicletValue.toString(), this.selectedRefineOptions);
        this.selectedRefineOptions = this.selectedRefineOptions.filter(info => chicletField != info.data || chicletValue.toString() != info.label);
        console.log(this.selectedRefineOptions);
        this.onRefineOptionSelect(undefined);

    }

    clearAllChiclet() {
        this.selectedRefineOptions = [];
        this.filtersArray = [];
        this.expandChiclet =false;
        this.onRefineOptionSelect(undefined);
    }



    private getRefineReqObj() {
        let filterReq = new GlobalSearchRequest();
        filterReq.setTerm(this.getSearchTerm());
        filterReq.setSearchBy(this.getSearchBY());

        if (filterReq.getSearchBy() == 'BU') {
            if (typeof (this.filterCompany) === "string") {
                filterReq.setIsWildCardSearch(true);
            } else {
                if (this.filterCompany.id === "01") {
                    filterReq.setIsWildCardSearch(false);
                } else {
                    filterReq.setIsWildCardSearch(true);
                }

            }
        } else {
            if (typeof (this.edi) === "string") {
                filterReq.setIsWildCardSearch(true);
            } else {
                if (this.edi.facet == "1") {
                    filterReq.setIsWildCardSearch(true);
                } else {
                    filterReq.setIsWildCardSearch(false);
                }
            }
        }

        let hashmap = [];
        if (this.selectedRefineOptions) {
            this.selectedRefineOptions.forEach(selectedNode => {
                if (hashmap.indexOf(selectedNode.data) < 0) {
                    let filterArray = this.selectedRefineOptions.filter(tempNode => tempNode.data === selectedNode.data);
                    hashmap.push(selectedNode.data);
                    filterArray[0].data
                    filterReq.appliedFilters.push({ [filterArray[0].data]: filterArray.flatMap(info => info.label) });
                }
            });
        }

        return filterReq;
    }


    isExpandedFilterCategory(label) {
        let isExist = this.selectedFilterCategoryTab.filter(data => label == data);
        if (isExist.length > 0) {
            return true;
        }
        return false;
    }


    tableWidth="calc(100% - 242px)";
    panelWidth="240px";
    leftDrag="240px";
    pageX=276;
    dragging = false;
    tableDragWidth = 0;
    cursor="default";
    dragSidePanel(event){
        event.preventDefault();
        console.log(event);
        this.cursor="col-resize";
        this.dragging = true;
    }


    stopDrag(){
        this.dragging = false;
        this.cursor = "default";
    }


    dropSidePanel(event){
        // if(event.target.outerHTML.indexOf('drag') > 0){
            if(this.dragging){
                this.cursor="col-resize";
                console.log(event.pageX,this.pageX);
                if(event.pageX > this.pageX){
                    let diff = event.pageX - this.pageX;

                    if(diff+240 < 400){
                        let sidePanelWidth = 244 + diff;
                        this.panelWidth = sidePanelWidth+"px";
                        this.tableDragWidth = sidePanelWidth+2;
                        this.tableWidth = "calc(100% - "+this.tableDragWidth+"px)";
                    }
                    this.leftDrag = this.panelWidth;
                }
                else{
                    this.tableWidth="calc(100% - 242px)";
                    this.panelWidth="240px";
                    this.leftDrag="240px";
                    this.tableDragWidth = 240;
                }
                // this.dragging = false;
            }
        // }
    }

}




