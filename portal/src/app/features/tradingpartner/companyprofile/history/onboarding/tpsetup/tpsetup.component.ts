import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { PSscrollUtils } from 'tgocp-ng/dist';
import { Company } from '../../../companyProfile.entity';
import { CompanyProfileService } from '../../../../../../services/company-profile.service';
import { TradingParnterDetailsService } from '../../../../../../services/trading-partner-details.service';
import { NotificationHandler } from '../../../../../../util/exception/notfication.handler';
import { TranslateService } from '@ngx-translate/core';
import { MatomoTracker } from 'ngx-matomo';

@Component({
    selector: 'tp-setup',
    templateUrl: './tpsetup.component.html',
    providers: [MatomoTracker]
})
export class TPsetupComponent {

    onboardingHistory: any;
    scrollConfig = PSscrollUtils.scrollY();
    showMap: boolean;
    constructor(private router: Router, private cpService: CompanyProfileService,
        private tpService: TradingParnterDetailsService, private notficationHandler: NotificationHandler, private translate: TranslateService,
        private matomoTracker: MatomoTracker) {
    }

    ngOnInit() {
       this.matomoTracker.trackPageView("Community Manager: My Trading Community - Onboarding History");

        this.getOnboardingHistory(this.cpService.getCompany());
    }

    getOnboardingHistory(company: Company) {
        this.tpService.getOnboardingHistory(false, company.companyName, company.city, company.country, company.companyId).subscribe(data => {
            if (data['responseDetails']['success']) {
                this.onboardingHistory = data['responseDetails']['responseEntity'];
                if (this.onboardingHistory.provisioningDetails.workflow.provisioningRequestData.registrationData.tgtsProfiles && this.onboardingHistory.provisioningDetails.workflow.provisioningRequestData.registrationData.tgtsProfiles.maps)
                    this.onboardingHistory.provisioningDetails.workflow.provisioningRequestData.registrationData.tgtsProfiles.maps.forEach(element => {
                        if (element.mapName) {
                            this.showMap = true;
                        }
                        else {
                            this.showMap = false;
                        }

                    })
            }
            else {
                this.onboardingHistory = [];

            }
        },
            error => {
                if (error.status == 404)
                    this.notficationHandler.notify({ severity: 'warning', title: this.translate.instant("common.noDataFound") });
            }
        )
    }

    cancel() {
        this.router.navigate([{ outlets: { modal: null } }], { queryParamsHandling: 'merge',  skipLocationChange: true  });
    }
}
