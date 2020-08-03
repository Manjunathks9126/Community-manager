import { Component, OnInit, Input } from "@angular/core";
import { Router } from "@angular/router";
import { TileHeader } from "../../entity/tile.entity";
import { Subscription } from "rxjs";
import { CommunityPageService } from "../../../../../services/community-page.service";


@Component({
  selector: 'ot-tile-header',
  templateUrl: './tile.header.component.html'
})
export class TileHeaderComponent implements OnInit {
  tprCount: any;
  tprCountSUB: Subscription;
  constructor(private router: Router, private communityPageService: CommunityPageService, ) { }
  isSearchEnabled: Boolean;
  searchString: string;

  @Input() tileHeader: TileHeader;

  ngOnInit() {
    this.tprCountSUB = this.communityPageService.bprCountSubject.subscribe(count => {
      this.tprCount = " (" + count + ")";
      if (!!this.tprCountSUB)
        this.tprCountSUB.unsubscribe();
    });
  }

  handleSearchEnableEvent(isEnabled: Boolean): void {
    this.isSearchEnabled = isEnabled;
  }

  handleOnClick(value: string): void {
    if (value) {
      this.router.navigate([value]);
    }
  }

}