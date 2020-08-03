import { Injectable } from "@angular/core";
import { Subject } from "rxjs";


@Injectable()
export class TileToolTipService {
    toolTipSubject = new Subject<any>();
    showTooltip(eventData) {
        this.toolTipSubject.next(eventData);
    }
}