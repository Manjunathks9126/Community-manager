import { Component, OnInit } from "@angular/core";
import { TradingParnterService } from "../../services/trading-partner.service";
import { CommunityPageService } from "../../services/community-page.service";

@Component({
    templateUrl: "./community-page.component.html"
})
export class CommunityPageComponent implements OnInit {


    tabsList: any[] = ['Overview'];
    tiles: any;
    graphData: any;
    totalBPRCount: any;
    title: string = "Community Manager";
    chartOptions = {
        legend: {
            display: true,
            position: 'right',
            labels: {
                fontColor: '#fff',
                fontSize: 14,
                boxWidth: 20,
                padding: 10,
                fontFamily: 'OpenTextSans-Light'
            },
            onClick: function (e, legendItem) {
                var index = legendItem.index;
                var chart = this.chart;
                var i, ilen, meta;
                for (i = 0, ilen = (chart.data.datasets || []).length; i < ilen; ++i) {
                    meta = chart.getDatasetMeta(i);
                    // toggle visibility of index if exists and if it is not Total Items legend
                    if (index < 5 && meta.data[index]) {
                        meta.data[index].hidden = !meta.data[index].hidden;
                    }
                }
                chart.update();
            }
        },
        tooltips: {
            display: false
        }
    };

    constructor(private tradingPartnerService: TradingParnterService,
        private communityPageService: CommunityPageService) {
    }

    ngOnInit(): void {
        this.getTiles();
        this.statsByBPRStatus();
    }

    getTiles() {
        this.communityPageService.listCommunityPageTiles().subscribe(data => {
            this.tiles = data.responseEntity;
        })
    }

    statsByBPRStatus() {
        this.tradingPartnerService.statsByBPRStatus().subscribe(data => {
            if (data.responseDetails && data['responseDetails']['responseEntity']) {
                this.totalBPRCount = data['responseDetails']['responseEntity'].TOTAL;
                this.communityPageService.updateBprCountToTiles(this.totalBPRCount);
                let statusArray = [];
                statusArray[0] = data['responseDetails']['responseEntity'].APPROVED;
                statusArray[1] = data['responseDetails']['responseEntity'].TESTING;
                statusArray[2] = data['responseDetails']['responseEntity'].REQUESTED;
                statusArray[3] = data['responseDetails']['responseEntity'].REJECTED;
                statusArray[4] = data['responseDetails']['responseEntity'].ERROR;
                statusArray[5] = this.totalBPRCount;
                this.graphData = this.communityPageService.getDataBPRChart(statusArray);
            }
        })
    }
}