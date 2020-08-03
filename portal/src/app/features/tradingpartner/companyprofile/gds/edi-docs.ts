import { Component, OnInit } from '@angular/core';
import { OnboardingService } from '../../../../services/onboarding.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  templateUrl: './edi-docs.html'
})
export class EdiAndDocs implements OnInit {

  constructor(private onboardingService: OnboardingService, private route: ActivatedRoute) { }

  EDI_DATA = [];
  DOC_DATA = [];
  isGDFlag: boolean = false;
  ngOnInit() {
    this.EDI_DATA = this.onboardingService.EDI_ADDRESS;
    this.DOC_DATA = this.onboardingService.DOCUMENTS;

    if (this.route.snapshot.queryParamMap.has("gd")) {
      this.isGDFlag = true;
    }
  }

}
