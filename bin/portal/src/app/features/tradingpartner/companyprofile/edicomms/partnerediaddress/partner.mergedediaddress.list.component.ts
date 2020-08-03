import { Component, OnInit } from "@angular/core";
import { TranslateService } from '@ngx-translate/core';
import { LazyLoadEvent } from 'tgocp-ng/dist/components/common/lazyloadevent';
import { TPRSearchQuery } from "../../../tpr.search.entity";
import { CompanyProfileService } from "../../../../../services/company-profile.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { TradingParnterDetailsService } from "../../../../../services/trading-partner-details.service";

@Component({
  templateUrl: './partner.mergedediaddress.list.component.html'
})

export class PartnerMergedEDIListComponent implements OnInit {
  ediComms: any;
  billingSplit: any[] = [];
  ediList: any[] = [];
  limit: any = 30;
  itemCount: any;
  tprSearchQuery: TPRSearchQuery = new TPRSearchQuery();
  displayBilling: boolean = false;
  displayComms: boolean = false;
  currentLanguage: string = "en";
 
  constructor(private cpService: CompanyProfileService,
    private notficationHandler: NotificationHandler,
    private tradingParnterDetails: TradingParnterDetailsService,private translate:TranslateService) {
  }
  ngOnInit() {
    this.tprSearchQuery.partnerCompanyId = this.cpService.getCompany().companyId;;
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
      this.tradingParnterDetails.searchPartnerMergedEDIAddresses(this.tprSearchQuery).subscribe(
        data => {
          if (data['responseDetails']['success']) {
        //    this.itemCount = data['responseDetails']['responseEntity'].itemCount;
            if (data['responseDetails']['responseEntity'].itemCount > 0)
              this.getPartnerEDIAddresses();
          }
        }, error => {
          if (error.status != 404)
          this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
        });
    } else {
      this.getPartnerEDIAddresses();
    }
  }
  getPartnerEDIAddresses() {
    if (!this.tprSearchQuery.limit)
      this.tprSearchQuery.limit = Math.min(this.itemCount, this.limit);
    else
      this.tprSearchQuery.limit = Math.min(this.itemCount, this.tprSearchQuery.after + this.limit);

    this.tprSearchQuery.countOnly = false;

    this.tradingParnterDetails.searchPartnerMergedEDIAddresses(this.tprSearchQuery).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.ediList = data['responseDetails']['responseEntity'].itemList;
          this.itemCount =  this.ediList.length;
        }

      }, error => {
        if (error.status != 404)
        this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
      }
    )
  }
  showBillingSplit(event, edis: any): any {
    this.displayBilling = true;
    this.billingSplit = edis.billingSplit;
    event.stopPropagation();
  }
  showComms(event, edis: any): any {
    this.tradingParnterDetails.getPartnerEDIAddressesComms(edis.ediAddress).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.displayComms = true;
          this.ediComms = data['responseDetails']['responseEntity'];
        }
      }, error => {
        if (error.status != 404)
        this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
      }
    )

    event.stopPropagation();
  }
}