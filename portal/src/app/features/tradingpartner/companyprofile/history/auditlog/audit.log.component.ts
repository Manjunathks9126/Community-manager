import { Component, OnInit } from "@angular/core";
import { Company } from '../../companyProfile.entity';
import { CompanyProfileService } from "../../../../../services/company-profile.service"
import { TradingParnterDetailsService } from "../../../../../services/trading-partner-details.service"

import { LazyLoadEvent } from 'tgocp-ng/dist/components/common/lazyloadevent';
import { NotificationHandler } from '../../../../../util/exception/notfication.handler';
import { TranslateService } from '@ngx-translate/core';
import { MatomoTracker } from 'ngx-matomo';


@Component({
  selector: 'tp-audit-log',
  templateUrl: './audit.log.component.html',
  styleUrls: ['./audit.log.component.css'],
  providers: [MatomoTracker]
})

export class AuditLogComponent implements OnInit {

  company: Company;
  selectedData: any[];
  limit: number = 30;
  auditLogs: any[];
  totalRecords: number = null;
  countOnly: boolean;
  startFrom: number = 0;
  showDetails: boolean = false;
  auditDetails: any;
  currentLanguage:string="en";
  constructor(
    private cpService: CompanyProfileService,
    private tradingParnterDetails: TradingParnterDetailsService,
    private notficationHandler: NotificationHandler,private translate: TranslateService, private matomoTracker: MatomoTracker) {
  }
  ngOnInit(): void {
    this.matomoTracker.trackPageView("Community Manager: My Trading Community - Audit Log");

    this.company = this.cpService.getCompany();
    this.currentLanguage=this.translate.currentLang;
  }

  lazyLoadLogData(event: LazyLoadEvent) {
    this.startFrom = event.first;
    this.limit = event.rows;
    this.getAuditLog();
  }

  getAuditLog() {
    // Get the total records first, before getting the list of logs
    if (this.totalRecords == null) {
      this.countOnly = true;
      this.tradingParnterDetails.getAuditLogs(this.company.companyId, this.countOnly, this.limit, this.startFrom).subscribe(
        data => {
          if (data['responseDetails']['success']) {
            this.totalRecords = data['responseDetails']['responseEntity'].itemCount;
            //console.log(this.totalRecords)
            if (this.totalRecords > 0)
              this.listAuditLog();
          }
        },
        error => {
          if (error.status != 404)
            this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
        }
      )
    } else {
      this.listAuditLog();
    }
  }

  listAuditLog() {
    this.countOnly = false;
    this.tradingParnterDetails.getAuditLogs(this.company.companyId, this.countOnly, this.limit, this.startFrom).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.auditLogs = data['responseDetails']['responseEntity'].itemList;
        }
        else {
          this.auditLogs = [];
        }
      },
      error => {
        if (error.status != 404)
          this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
      }
    )
  }

  showDetailsPopup(event, auditDetails: any): any {
    this.showDetails = true;
    this.auditDetails = JSON.parse(auditDetails);
    event.stopPropagation();
  }

}
