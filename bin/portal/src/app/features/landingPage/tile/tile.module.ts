import { TilesContainerComponent } from "./tile.container.component";
import { NgModule } from "@angular/core";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
//import { DragulaModule } from "ng2-dragula";
import { TranslateModule } from "@ngx-translate/core";
import { SearchModule } from "tgocp-ng/dist";
import { TileComponent } from "./tile/tile.component";
import { TileHeaderComponent } from "./tile/header/tile.header.component";
import { TileFooterComponent } from "./tile/footer/tile.footer.component";
import { TileContentComponent } from "./tile/content/tile.content.component";
import { TileContentAccordionComponent } from "./tile/content/accordion/tile.content.accordion.component";
import { TilesToolTipComponent } from "./content-tooltip/tooltip.component";
import { OtDateTimePipeModule } from "tgocp-ng/dist";
import { TradingPartnerTile } from "./tile/content/contentType/tradingPartnerTile/tradingPartnerTile.component";
import { TpDistributionTile } from "./tile/content/contentType/tpDistributionTile/tpDistribution.component";
import { OnboardStatsTile } from "./tile/content/contentType/onboardingStatsTile/onboardStatistics.component";

@NgModule({ 
  exports: [TilesContainerComponent, TileComponent],
  declarations: [TradingPartnerTile, TpDistributionTile, OnboardStatsTile, TilesContainerComponent, TilesToolTipComponent, TileComponent, TileHeaderComponent, TileContentComponent, TileFooterComponent, TileContentAccordionComponent],
  imports: [ PerfectScrollbarModule, /* DragulaModule.forRoot() , */ TranslateModule, SearchModule, OtDateTimePipeModule]
})
export class TileModule { }
