import { Component, Input, OnInit, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";
import { TileContent } from "../../entity/tile.entity";
import { TileContentDetail } from "../../entity/TileContentDetail.entity";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { CommunityPageService } from "../../../../../services/community-page.service";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";


@Component({
  selector: 'ot-tile-content',
  templateUrl: './tile.content.component.html'
})
export class TileContentComponent implements OnInit, OnDestroy {

  public config: PerfectScrollbarConfigInterface = {};
  contentLoaded: boolean = false;
  @Input() tileContent: TileContent;
  tileContentDetails: TileContentDetail;
  tpListDataSUB: Subscription;
  tpListData_OnSCROLL_SUB: Subscription;
  loadBPRListAfter: number = 0;
  totalBPRCount: number;
  constructor(private communityPageService: CommunityPageService,private notificationHandler:NotificationHandler) {

  }
  ngOnDestroy(): void {
    if (!!this.tpListData_OnSCROLL_SUB)
      this.tpListData_OnSCROLL_SUB.unsubscribe();
  }

  ngOnInit() {
    this.contentLoaded = false;
    if (this.tileContent && this.tileContent.displaySrc == "1210718") {
      this.loadBPRListAfter = 0;
      this.communityPageService.listTradingPartners(0);
      this.config.wheelPropagation = true; // To enable load on scroll

      this.tpListDataSUB = this.communityPageService.tpListDataSubject.subscribe(() => {
        this.tileContentDetails = this.communityPageService.tileContentDetails;
        this.contentLoaded = true;
        if (!!this.tpListDataSUB)
          this.tpListDataSUB.unsubscribe();
      });

    }
    this.communityPageService.bprCountSubject.subscribe(count => {
      this.totalBPRCount = count;
    });
  }
  public onScrollEvent(event: any): void {
    if (event.target.className.indexOf("active-y") > 0) {
      this.contentLoaded = false;
      this.loadBPRListAfter += 20;
      if (this.loadBPRListAfter < this.totalBPRCount) {
        this.loadBPR(this.loadBPRListAfter);
      }else{
        this.notificationHandler.notify({ severity: 'success', title: "Reached end of results" });
      }
    }
  }
  private loadBPR(after: number) {
    this.communityPageService.listTradingPartners(after);
    this.tpListData_OnSCROLL_SUB = this.communityPageService.tpListDataSubject.subscribe(() => {
      this.tileContentDetails.elements = this.tileContentDetails.elements.concat(this.communityPageService.tileContentDetails.elements);
      this.contentLoaded = true;
    })
  }



}