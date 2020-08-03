import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from "@angular/core";
import { DomSanitizer } from '@angular/platform-browser';
import { TradingParnterService } from "../../../../services/trading-partner.service";

@Component({
    selector: 'tpgraph',
    templateUrl: './tp-graph.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class GraphComponent implements OnInit {

    url: string = "";

    constructor(private ref: ChangeDetectorRef,
        public sanitizer: DomSanitizer, private tpService: TradingParnterService) {
    }

    ngOnInit(): void {
 

        this.tpService.graphApplnData().subscribe(
            data => {
                let rawURL = "";
                if (data.success) {
                    rawURL = data.responseEntity.url;
                    rawURL = rawURL.concat("?param=").concat(data.responseEntity.param);
                    this.url = rawURL;
                    this.ref.detectChanges();
                }
            })
    }

    getUrl() {
        return this.url;
    }


}
