import { Component, Input, OnInit } from "@angular/core";
 
import { trigger, style, animate, transition } from '@angular/animations';
import { Router } from "@angular/router";
import { TileContentElement } from "../../../entity/TileContentDetail.entity";

@Component({
    selector: 'ot-tile-accordion',
    templateUrl: './tile.content.accordion.component.html',
    animations: [
        trigger(
            'enterAnimation', [
                transition(':enter', [
                    style({ transform: 'translateX(100%)', opacity: 0 }),
                    animate('500ms', style({ transform: 'translateX(0)', opacity: 1 }))
                ]),
                transition(':leave', [
                    style({ transform: 'translateX(0)', opacity: 1 }),
                    animate('500ms', style({ transform: 'translateX(100%)', opacity: 0 }))
                ])
            ]
        )
    ]
})
export class TileContentAccordionComponent {

    @Input()
    displayText: string;
    @Input()
    children: [TileContentElement];
    isOpen: boolean = false;
    constructor(private router: Router) {
    }

    clickAccordion() {
        this.isOpen = !this.isOpen;
    }

    clickUrl(url: string) {
    if (url) {
      if (url.indexOf('openAs=popup')>-1) {
        this.openNewTab(url);
      } else {
        this.navigate(url);
      }
    }
  }

   private openNewTab(url: string) {
    window.open(url);
  }

  private navigate(url: string) {
    let navigationExtras = {
      queryParams: { serviceUrl: url }
    }
    this.router.navigate(['./', 'homepage', 'component'], navigationExtras);
  }
}