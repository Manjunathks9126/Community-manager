import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef, HostListener } from "@angular/core";
import { TradingParnterService } from "../../../../services/trading-partner.service";
import { TileToolTipService } from "./tooltip.service";

@Component({
    selector: 'tooltip',
    templateUrl: './tooltip.component.html'
})
export class TilesToolTipComponent {

    toolTipContentDetail = [];
    toolTipHeader_text: string;

    private TOOL_TIP_HEIGHT = 416;
    private TOOL_TIP_ARROW = 'tooltip-arrow';

    @ViewChild('tileTooltip')
    private toolTip: ElementRef;

    constructor(private tpServcie: TradingParnterService, private tooltipService: TileToolTipService, public el: ElementRef, ) {
        this.tooltipService.toolTipSubject.subscribe(event => {
            this.showTooltip(event);
        })
    }


    //Main
    public showTooltip(eventData) {
        this.tpServcie.getEdis(eventData.contentDetails.bprId, false, 10, 0).subscribe(data => {
            this.adjustToolTip(eventData, data);
        }, error => {
            this.adjustToolTip(eventData, null);
        })
    }

    // Data for tooltip
    private createToolTipContentDetailData(data, ediListData) {
        this.toolTipContentDetail = [];
        this.toolTipHeader_text = data.displayName;
        this.toolTipContentDetail[0] = { "key": "Trading Partner Name", "value": data.companyName };
        this.toolTipContentDetail[1] = { "key": "Status", "value": data.status };
        let ediListString = "";
        if (ediListData && ediListData['responseDetails'] && ediListData['responseDetails']['responseEntity']['itemList']) {
            let ediList = ediListData['responseDetails']['responseEntity']['itemList'];
            for (let i = 0; i < ediList.length; i++) {
                if (null != ediList[i].qualifier)
                    ediListString = ediListString.concat(ediList[i].qualifier);
                ediListString = ediListString.concat(":");
                ediListString = ediListString.concat(ediList[i].address).concat(",");
            }
        }
        ediListString = ediListString.slice(0, ediListString.lastIndexOf(","));
        this.toolTipContentDetail[2] = { "key": "EDI Addresses", "value": ediListString }
    }

    //Adjust Tooltip Position
    private adjustToolTip(eventData, ediList) {
        this.createToolTipContentDetailData(eventData.contentDetails, ediList);
        let body = document.body,
            html = document.documentElement;
        var pageHeight = Math.max(body.scrollHeight, body.offsetHeight,
            html.clientHeight, html.scrollHeight, html.offsetHeight);
        var pageWidth = Math.max(body.scrollWidth, body.offsetWidth,
            html.clientWidth, html.scrollWidth, html.offsetWidth);
        let tileWidth = eventData.tileContainer.nativeElement.offsetWidth;
        pageHeight = pageHeight - 16;
        //console.log(pageHeight);
        let leftPX = eventData.tileContainer.nativeElement.getBoundingClientRect().left + eventData.tileContainer.nativeElement.offsetWidth + 64;
        let leftEM = leftPX / 16;

        var _src_element = eventData.event.target || eventData.event.srcElement;
        let topPX = _src_element.getBoundingClientRect().top - 300;

        let topEM = topPX / 16;
        this.toolTip.nativeElement.style.visibility = 'visible';
        this.toolTip.nativeElement.style.opacity = 1;

        let toolTipHeight = topPX + this.TOOL_TIP_HEIGHT;
        if (pageHeight <= toolTipHeight) {
            let adjustHeightEM = (toolTipHeight - pageHeight) / 16;
            this.toolTip.nativeElement.style.top = topEM - adjustHeightEM + .2 + "em";
        } else {
            this.toolTip.nativeElement.style.top = topEM + "em";
        }
        this.adjustToolTipArrowPosition(eventData, pageHeight, leftPX, tileWidth, pageWidth, leftEM);
    }



    // Adjust Tooltip Arrow Postion
    private adjustToolTipArrowPosition(eventData, pageHeight, leftPX, tileWidth, pageWidth, leftEM) {
        // arrow Position - TOP
        if (this.toolTip.nativeElement.contains(this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0])) {
            var _src_element = eventData.event.target || eventData.event.srcElement;
            let arrowTop = _src_element.getBoundingClientRect().top;
            if (arrowTop + 32 > pageHeight) {
                arrowTop = arrowTop - 30;
            }
            let targetElem = _src_element.className;
            if (targetElem.indexOf("sub") < 0)
                this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0].style.top = arrowTop - 5 + "px";
            else
                this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0].style.top = arrowTop - 10 + "px";
        }
        // arrow Position - Left | Right
        let toolTipWidth = leftPX + 400;
        let tileWidthEM = tileWidth / 16;
        if (pageWidth <= toolTipWidth) {
            this.toolTip.nativeElement.style.left = leftEM - tileWidthEM - 25.5 + "em";
            if (this.toolTip.nativeElement.contains(this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0])) {
                this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0].className = "tooltip-arrow right";
            }
        } else {
            this.toolTip.nativeElement.style.left = leftEM + "em";
            if (this.toolTip.nativeElement.contains(this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0])) {
                this.toolTip.nativeElement.getElementsByClassName(this.TOOL_TIP_ARROW)[0].className = "tooltip-arrow left";
            }
        }
    }

    @HostListener('document:click', ['$event'])
    handleClick(event: Event) {
        if (!this.el.nativeElement.contains(event.target)) {
            this.toolTip.nativeElement.style.visibility = 'hidden';
            this.toolTip.nativeElement.style.opacity = 0;
        }
    }
}