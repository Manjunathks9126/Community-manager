import { Component, OnInit } from "@angular/core";
import { TranslateService } from '@ngx-translate/core';
import { LazyLoadEvent } from 'tgocp-ng/dist/components/common/lazyloadevent';
import { DatePipe } from '@angular/common';
import { TPRSearchQuery } from "../../../tpr.search.entity";
import { FilterEntity } from "../../../filter-entity";
import { CompanyProfileService } from "../../../../../services/company-profile.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { TradingParnterDetailsService } from "../../../../../services/trading-partner-details.service";

@Component({
  templateUrl: './partner.ediaddress.list.component.html'
})

export class PartnerEDIListComponent implements OnInit {
  ediComms: any;
  billingSplit: any;
  ediList: any[] = [];
  limit: any = 30;
  itemCount: any = 0;
  tprSearchQuery: TPRSearchQuery = new TPRSearchQuery();
  displayBilling: boolean = false;
  displayComms: boolean = false;
  isSearchClicked = false;
  isFilterApplied = false;
  partnerediaddress: string = "";
  qualifier: string = "";
  selectedFilters: FilterEntity;
  searchAttribute: string = '';
  numberOfFiltersApplied: number = 0;
  dateValid: boolean = true;
  dateTime = new Date();
  searchObject: any = { 'createddate': [], "dateFrom": "", "dateTo": "" };
  currentLanguage: string = "en";
  
  constructor(private cpService: CompanyProfileService,
    private translate: TranslateService, private notficationHandler: NotificationHandler,
    private tradingParnterDetails: TradingParnterDetailsService, public datepipe: DatePipe, ) {
  }
  ngOnInit() {
    this.tprSearchQuery.partnerCompanyId = this.cpService.getCompany().companyId;
    this.currentLanguage=this.translate.currentLang;
  }
  lazyEDIList(event: LazyLoadEvent) {
    this.tprSearchQuery.after = event.first;
    this.limit = event.rows;
    this.tprSearchQuery.limit = event.rows;
    this.searchPartnerEDIAddresses();
  }
  searchPartnerEDIAddresses() {
    if (this.itemCount == null) {
      this.tprSearchQuery.countOnly = true;

      this.tradingParnterDetails.searchPartnerEDIAddresses(this.tprSearchQuery).subscribe(
        data => {
          if (data['responseDetails']['success']) {
            //    this.itemCount = data['responseDetails']['responseEntity'].itemCount;
            if (data['responseDetails']['responseEntity'].itemCount > 0)
              this.getPartnerEDIAddresses();
          }
        }, error => {
          if (error.status != 404)
            this.notficationHandler.notify({ severity: 'error', userMessage: error.userMessage });
        });
    } else {
      this.getPartnerEDIAddresses();
    }
  }
  getPartnerEDIAddresses() {
    this.isFilterApplied = false;
    this.ediList = [];
    this.itemCount = 0;
    if (!this.tprSearchQuery.limit)
      this.tprSearchQuery.limit = Math.min(this.itemCount, this.limit);
    else
      this.tprSearchQuery.limit = Math.min(this.itemCount, this.tprSearchQuery.after + this.limit);
    this.tprSearchQuery.countOnly = false;
    if (this.searchAttribute == 'ediAddress') {
      this.partnerediaddress = this.partnerediaddress ? this.partnerediaddress.trim() : '';
      this.qualifier = this.qualifier ? this.qualifier.trim() : '';
      this.isFilterApplied = true;
      this.resetSearcKeys()
      if (this.qualifier != null && this.qualifier.length > 0 || this.partnerediaddress != null && this.partnerediaddress.length > 0) {
        this.tprSearchQuery.partnerediaddress = ((this.partnerediaddress ? this.qualifier : '') + ':' + this.partnerediaddress).toUpperCase();
      }
      else {
        this.tprSearchQuery.partnerediaddress = '';
      }
    }
    if (this.searchAttribute == 'createddate') {
      this.resetSearcKeys()
      this.isFilterApplied = true;
      this.tprSearchQuery.dateFrom = this.datepipe.transform(this.searchObject.dateFrom, 'MM/dd/yyyy');
      this.tprSearchQuery.dateTo = this.datepipe.transform(this.searchObject.dateTo, 'MM/dd/yyyy');
    }

    this.tradingParnterDetails.searchPartnerEDIAddresses(this.tprSearchQuery).subscribe(

      data => {

        if (data['responseDetails']['success']) {
          this.ediList = data['responseDetails']['responseEntity'].itemList;
          this.itemCount = this.ediList.length;
        }

      }, error => {
        if (error.status != 404)
          this.notficationHandler.notify({ severity: 'error', userMessage: error.userMessage });
      }
    )
  }
  showBillingSplit(event, edis: any): any {
    this.displayBilling = true;
    this.billingSplit = edis.billingSplit;
    event.stopPropagation();
  }
  showComms(event, edis: any): any {
    this.tradingParnterDetails.getPartnerEDIAddressesComms(edis.partnerEDIAddress).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.displayComms = true;
          this.ediComms = data['responseDetails']['responseEntity'];
        }
      }, error => {
        if (error.status != 404)
          this.notficationHandler.notify({ severity: 'error', userMessage: error.userMessage });
      }
    )

    event.stopPropagation();
  }
  displaySearchPanel() {
    this.isSearchClicked = !this.isSearchClicked;

  }
  clearEdiChipset() {
    this.clearSearchFilter();
    this.getPartnerEDIAddresses();

    if ((!this.partnerediaddress && !this.qualifier && !this.searchObject.dateFrom && !this.searchObject.dateTo)) {
      this.isFilterApplied = false;
    }
  }
  clearDateChispset() {

    this.resetDates();
    this.clearSearchFilter();
    this.getPartnerEDIAddresses();
    if ((!this.searchObject.dateFrom && !this.searchObject.dateTo && !this.partnerediaddress && !this.qualifier)) {
      this.isFilterApplied = false;
    }

  }

  clearSearchFilter() {
    this.searchObject.dateFrom = "";
    this.searchObject.dateTo = "";

    this.partnerediaddress = "";
    this.qualifier = "";
    this.tprSearchQuery.partnerediaddress = "";
    this.tprSearchQuery.qualifier = "";
    this.searchAttribute == '';


  }
  resetDates() {
    this.searchObject.dateTo = new Date();
    let tempDate = new Date();
    tempDate.setMonth(tempDate.getMonth() - 3);
    this.searchObject.dateFrom = tempDate;
  }
  closeChipset() {
    this.isFilterApplied = false;

    this.partnerediaddress = "";
    this.qualifier = "";

    this.tprSearchQuery.partnerediaddress = "";
    this.tprSearchQuery.qualifier = "";
  }
  isApplyBtnDisable() {
    if (this.searchAttribute == "ediAddress") {
      if (this.partnerediaddress.length > 0) {
        return false;
      } else {
        return true;
      }
    } else if (this.searchAttribute == "createddate") {
      if (this.searchObject.dateFrom && this.searchObject.dateFrom.length > 0 && this.searchObject.dateTo && this.searchObject.dateTo.length > 0 && this.dateValid) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  clearSearchCriteria() {
    this.displaySearchPanel();
  }

  resetSearcKeys() {
    this.tprSearchQuery.partnerediaddress = '';
    this.tprSearchQuery.dateFrom = '';
    this.tprSearchQuery.dateTo = '';

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