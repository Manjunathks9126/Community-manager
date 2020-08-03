import { Component } from "@angular/core";
import { ActivatedRoute } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

@Component({
    selector: 'tp-list-container',
    templateUrl: './tplist.container.component.html'

})

export class TradingPartnerListContainerComponent {
    currentView: string = "BPR";
    inputTypes = [];

    constructor(private route: ActivatedRoute, private translate: TranslateService) {

    }

    ngOnInit() {
        this.translate.get("tpdir.options").subscribe(res => {
            this.inputTypes.push({ 'label': res.bpr, 'value': "BPR" });
            this.inputTypes.push({ 'label': res.tpr, 'value': "TPR" });
            this.inputTypes.push({ 'label': res.itp, 'value': "INVTD" });

            if (this.route.snapshot.queryParamMap.has("listView")) {//'listView': 'INVTD'
                this.currentView = this.route.snapshot.queryParamMap.get('listView');
            }
        });
    }
}