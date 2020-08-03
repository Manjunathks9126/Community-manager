import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { CompanyProfileService } from '../../../../../services/company-profile.service';
import { PSscrollUtils } from 'tgocp-ng/dist';

@Component({
    selector: 'tp-onboarding-history',
    templateUrl: './onboarding.history.component.html'
})
export class OnboardingHistoryComponent {
    companyName: string;
    scrollConfig = PSscrollUtils.scrollY();
    constructor(private router: Router, private cpService: CompanyProfileService) {
        this.companyName = this.cpService.getCompany().companyName;
    }


}