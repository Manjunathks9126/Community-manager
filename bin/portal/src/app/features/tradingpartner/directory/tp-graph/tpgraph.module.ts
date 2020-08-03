import { NgModule } from '@angular/core';
import { GraphComponent } from './tp-graph.component';
import { TPGraphRoutingModule } from './tpgraph-routing.module';
import { TradingParnterService } from '../../../../services/trading-partner.service';



@NgModule({
  imports: [
    TPGraphRoutingModule
  ],
  declarations: [GraphComponent],
  providers: [TradingParnterService]
})
export class TPGraphModule { }
