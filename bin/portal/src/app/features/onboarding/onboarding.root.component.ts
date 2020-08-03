import { Component, OnInit } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { ActivatedRoute } from "@angular/router";
import { OnboardingService } from '../../services/onboarding.service';

@Component({
    selector: 'onboarding-profile',
    templateUrl: "./onboarding.root.component.html"
   
})
export class OnboardingRootComponent implements OnInit {
    
    constructor(private translate: TranslateService, private route: ActivatedRoute,private onboardingService: OnboardingService) {} 

    ngOnInit() {
        this.translate.use(this.route.snapshot.data['userLocale']);
        this.onboardingService.getLoggedInCompanyDetails().subscribe(loggedCompanyDetails => {
            if (null != loggedCompanyDetails && loggedCompanyDetails.success == true) {
            let loggedBuId = loggedCompanyDetails.responseEntity.companyId;
              this.onboardingService.loggedBuId=loggedBuId;
            }
    })
}
}

    
