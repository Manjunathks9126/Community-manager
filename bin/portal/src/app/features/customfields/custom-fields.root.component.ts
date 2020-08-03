import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  templateUrl: './custom-fields.root.component.html'
})
export class CustomFieldsRootComponent implements OnInit {

  constructor(private translate: TranslateService, private route: ActivatedRoute) {
  }

  ngOnInit() {
      this.translate.use(this.route.snapshot.data['userLocale']);
  }
}
