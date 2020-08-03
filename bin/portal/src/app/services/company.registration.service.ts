import { Injectable } from '@angular/core';
import { DialogueboxService } from 'tgocp-ng/dist/components/dialoguebox/dialoguebox.service';
import { Router } from '@angular/router';

@Injectable()
export class CompanyRegistrationService {

    constructor(private dialogueboxService: DialogueboxService, private router: Router) {
    }

    registationTasksBaseURL = "v1/onboardingservices/workflows/";
    registrationSumbitURL = "onboarding/company";

    cancelRegistration() {
        this.dialogueboxService.confirm({
            dialogName: 'cancelRegistration',
            accept: () => {
                this.router.navigate(['/tpdir']);
            }, reject: () => { }
        });
    }

    buildRegistrationTasksURL(workflowId: any, locale: any, invitingCompanyId: any) {
        return this.registationTasksBaseURL + workflowId + "/tasks" + "?locale=" + locale + "&invitingCompanyId=" + invitingCompanyId;
    }
}