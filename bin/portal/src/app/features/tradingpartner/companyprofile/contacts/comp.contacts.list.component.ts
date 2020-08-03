import { Component, ViewChild, OnInit } from "@angular/core";
import { DataTable } from "tgocp-ng/dist";
import { DatePipe } from "@angular/common";
import { LazyLoadEvent } from "tgocp-ng/dist/components/common/lazyloadevent";
import { CompanyProfileService } from "../../../../services/company-profile.service";
import { NotificationHandler } from "../../../../util/exception/notfication.handler";
import { ContactsFilterEntity } from "./contact-filter-entity";
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'tp-profile-contacts',
  templateUrl: './comp.contacts.list.component.html'
})

export class CompanyContactsComponent implements OnInit {

  constructor(private cpService: CompanyProfileService, public datepipe: DatePipe,
    private notficationHandler: NotificationHandler, private translate: TranslateService) {
  }
  @ViewChild(DataTable) dataTableComponent: DataTable;
  dateTime = new Date();
  userList: any[];
  selectedData: any[];
  msgs: any[] = [];
  selectedFilters: ContactsFilterEntity = new ContactsFilterEntity();
  totalRecords: number = 0;
  companyId: any;
  limit: number = 30;
  isFilterClicked = false;
  isFilterApplied = false;
  isSearchClicked = false;
  selectedstatus: string[] = [];
  searchObject: any = { 'status': [] };
  statusOptions: string[] = [];
  numberOfFiltersApplied: number = 0;
  dateValid: boolean = true;
  currentLanguage:string="en";
  ngOnInit() {
    this.initVariables();
    this.translate.get("tpdir.company.contacts.statusOptions").subscribe(res => { this.statusOptions = res })
    this.currentLanguage=this.translate.currentLang;
  }

  initVariables() {
    this.companyId = this.cpService.getCompany().companyId;
  }

  listUsers() {
    this.msgs = [];
    // send the limit as Minimum of totalRecords & limit(as per CMS API)
    if (!this.selectedFilters.limit)
      this.selectedFilters.limit = Math.min(this.totalRecords, this.limit);
    else
      this.selectedFilters.limit = Math.min(this.limit, this.totalRecords - this.selectedFilters.after);

    this.selectedFilters.countOnly = false;
    this.cpService.getUsers(this.companyId, this.selectedFilters).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.userList = data['responseDetails']['responseEntity'].itemList;
          this.cpService.setUsers(data['responseDetails']['responseEntity'].itemList);
        } else {
          this.userList = [];
        }
      }, error => {
        if (error.status != 404)
          this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
      }
    );
  }

  getUsers() {
    // Get the total records first, before getting the list of users
    if (this.totalRecords == 0) {
      this.msgs = [];
      this.selectedFilters.countOnly = true;
      this.cpService.getUsers(this.companyId, this.selectedFilters).subscribe(
        data => {
          if (data['responseDetails']['success']) {
            this.totalRecords = data['responseDetails']['responseEntity'].itemCount;
            if (this.totalRecords > 0)
              this.listUsers();
          }
        }, error => {
          if (error.status != 404)
            this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    } else {
      this.listUsers();
    }
  }

  lazyLoadUserList(event: LazyLoadEvent) {
    this.selectedFilters.after = event.first;
    this.selectedFilters.limit = event.rows;
    this.limit = event.rows;
    this.getUsers();
  }

  resetDates() {
    this.searchObject.dateTo = new Date();
    let tempDate = new Date();
    tempDate.setMonth(tempDate.getMonth() - 3);
    this.searchObject.dateFrom = tempDate;
  }

  displayFilterPanel() {
    this.isFilterClicked = !this.isFilterClicked;
    this.isSearchClicked = false;
    if (!this.searchObject.dateFrom && !this.searchObject.dateTo) {
      this.resetDates();
    }
  }
  displaySearchPanel() {
    this.isSearchClicked = !this.isSearchClicked;
    this.isFilterClicked = false;
  }

  clearFilterCriteria() {
    this.isFilterApplied = false;
    this.isFilterClicked = false;
    this.isSearchClicked = false;
    this.selectedFilters.dateFrom = "";
    this.selectedFilters.dateTo = "";
    this.resetDates();
    this.selectedFilters.firstName = "";
    this.selectedFilters.lastName = "";
    this.selectedFilters.email = "";
    this.selectedFilters.status = [];
    this.selectedFilters.after = 0;
    this.searchObject = {};
    this.searchObject.status = [];

    // this.searchObject = {};
    this.totalRecords = 0;
    this.getUsers();
    // this.dataTableComponent.paginatorElement['activePage']=1; 
    this.dataTableComponent.reset();
  }
  clearChispsetElement(element: any) {
    if (element == 'status') {
      this.selectedFilters[element] = [];
      this.searchObject[element] = [];
    } else if (element == 'dateRange') {
      this.resetDates();
      this.selectedFilters.dateFrom = "";
      this.selectedFilters.dateTo = "";
    } else {
      this.selectedFilters[element] = "";
      this.searchObject[element] = "";
    };
    this.selectedFilters.after = 0;
    // this.searchObject = {};
    this.totalRecords = 0;
    this.getUsers();
    //this.dataTableComponent.paginatorElement['activePage']=1; 
    this.dataTableComponent.reset();

    this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(x => this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0).length;
    if (this.numberOfFiltersApplied < 1) {
      this.isFilterApplied = false;
    }
  }

  filterData() {
    this.numberOfFiltersApplied = 0;
    this.isFilterApplied = true;
    this.selectedFilters.after = 0;
    this.selectedFilters.status = this.searchObject.status;
    this.selectedFilters.dateFrom = this.datepipe.transform(this.searchObject.dateFrom, 'MM/dd/yyyy');
    this.selectedFilters.dateTo = this.datepipe.transform(this.searchObject.dateTo, 'MM/dd/yyyy');

    this.userList = [];
    this.totalRecords = 0;
    this.getUsers();
    this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(x => this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0).length;
    this.dataTableComponent.reset();
  }
  searchData() {
    this.numberOfFiltersApplied = 0;
    this.isFilterApplied = true;
    this.selectedFilters.after = 0;
    this.selectedFilters.firstName = this.searchObject.firstName ? this.searchObject.firstName.trim() : "";
    this.selectedFilters.lastName = this.searchObject.lastName ? this.searchObject.lastName.trim() : "";
    this.selectedFilters.email = this.searchObject.email ? this.searchObject.email.trim() : "";
    this.userList = [];
    this.totalRecords = 0;
    this.getUsers();
    this.numberOfFiltersApplied = Object.keys(this.selectedFilters).filter(x => this.selectedFilters[x] !== null && this.selectedFilters[x].length > 0).length;
    this.dataTableComponent.reset();
  }
  clearSearchCriteria(type: any) {
    if (type == "filter") {
      this.isFilterClicked = false;
    } else if (type == 'search') {
      this.isSearchClicked = false;
    }
  }

  filterHasData(): boolean {
    this.isFilterApplied = true;
    this.selectedFilters.after = 0;
    return true;
  }
  onDateSelect() {
    this.dateValidation();
  }

  dateValidation() {
    if (this.datepipe.transform(this.searchObject.dateFrom, 'MM/dd/yyyy') > this.datepipe.transform(this.searchObject.dateTo, 'MM/dd/yyyy')) {
      this.dateValid = false;
    }
    else {
      this.dateValid = true;
    }
  }
}
