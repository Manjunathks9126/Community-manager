import { Component } from "@angular/core";
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    templateUrl: './tpdir.root.component.html'
})

export class TPDirRootComponent{
    
    constructor(private translate: TranslateService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.translate.use(this.route.snapshot.data['userLocale']); 
    }

 
}