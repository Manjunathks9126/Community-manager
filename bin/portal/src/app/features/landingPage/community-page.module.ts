import { NgModule } from "@angular/core";
import { CommonModule } from '@angular/common';

import { CommunityPageComponent } from "./community-page.component";
import { TileModule } from "./tile/tile.module";
import { TileToolTipService } from "./tile/content-tooltip/tooltip.service";
import { CommunityPageRoutingModule } from "./community-page-routing.module";
import { CommunityPageService } from "../../services/community-page.service";
import { WorkspaceModule } from "tgocp-ng/dist/components/workspace/workspace.component";
import { ChartModule } from "tgocp-ng/dist/components/PiChart/uiChart.component";
import { TranslateModule } from "@ngx-translate/core";
import { TradingParnterService } from "../../services/trading-partner.service";

@NgModule({
    imports: [CommunityPageRoutingModule, WorkspaceModule, TileModule, CommonModule, ChartModule,TranslateModule],
    declarations: [CommunityPageComponent],
    providers: [CommunityPageService, TileToolTipService,TradingParnterService]
})
export class CommunityPageModule { }