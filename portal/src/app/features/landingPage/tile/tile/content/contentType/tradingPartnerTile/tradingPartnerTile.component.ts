import { Component, Input, OnInit, ViewChild, OnDestroy, ElementRef } from "@angular/core";
import { TileToolTipService } from "../../../../content-tooltip/tooltip.service";
import { Router } from "@angular/router";

@Component({
    selector: 'tradingPartner-tile',
    templateUrl: './tradingPartnerTile.component.html'
})
export class TradingPartnerTile implements OnInit, OnDestroy {

    @Input() tpDirectoryContent: any;
    eventData: any;
    
    @ViewChild('tileItem')
    tileItem: ElementRef;

    ngOnDestroy(): void {
    }
    ngOnInit(): void {
       
    }
    constructor(private router: Router,private tooltipService:TileToolTipService){}
 

    showTooltip(event, bprMetaData, row) {
        this.eventData = {};
        this.eventData.event = event;
        this.eventData.tileContainer = this.tileItem;
        let data = { "companyId":"", "companyName":"","displayName":"" ,"status":"","bprId":"" };
        data.companyId = bprMetaData.buid;
        data.bprId = bprMetaData.bprId;
        data.companyName = row[0].subtext;
        data.status = row[1].headerText;
        data.displayName = row[0].headerText;
        this.eventData.contentDetails = data;
        this.tooltipService.showTooltip(this.eventData);
      }

      clickUrl(url: string) {
        if (url) {
          if (url.indexOf('openAs=popup') > -1) {
            this.openNewTab(url);
          } else {
            this.navigate(url);
          }
        }
      }

      private openNewTab(url: string) {
        window.open(url);
      }
    
      private openInCurrentTab(url: string) {
        window.location.href = url;
      }
    
      private navigate(url: string) {
        let navigationExtras = {
          queryParams: { serviceUrl: url }
        }
        this.router.navigate(['./', 'homepage', 'component'], navigationExtras);
      }

}