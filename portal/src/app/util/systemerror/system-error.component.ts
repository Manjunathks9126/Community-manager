import { Component} from '@angular/core';
import { TranslateService } from "@ngx-translate/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  templateUrl: 'system-error.component.html',
})
export class SystemErrorComponent {

  errorMessage: string;
  pageNotFound:boolean;
  constructor(private translate: TranslateService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.data.subscribe(data => {
      if (data.userLocale) {
        this.translate.use(data.userLocale);
      }
      if(data.PageNotFound)
      {
        this.pageNotFound=true;
      }
    });
  }
}