import { Component, Input, OnInit } from "@angular/core";
import { TradingParnterService } from "../../../services/trading-partner.service";
import { FormDataService } from "./entity/formData.service";
import { FormData } from "./entity/formData.model";
import { Company } from "./entity/company.entity";
import { NotificationHandler } from "../../../util/exception/notfication.handler";
import { Router } from "@angular/router";
import { Workflow, Maps } from '../directory/createTPMSG/entity/workflow.entity';
import { OnboardingService } from "../../../services/onboarding.service";

@Component({
    selector: 'key-list',
    templateUrl: './key.component.html'
})

export class KeyMapsComponent implements OnInit {

    listkeys: any;
    workflow: Workflow = new Workflow();
    @Input() formData: FormData;
    constructor(private notficationHandler: NotificationHandler, private router: Router,
        private formDataService: FormDataService, private tpService: TradingParnterService, private onbaordingService: OnboardingService) {

    }

    ngOnInit() {
        this.workflow = this.formDataService.getCachedCompanyDetails()
        this.tpService.getKeyBankMaps().subscribe(data => {
            this.listkeys = data;
        });
    }

    selectedKeys: any[] = [];
    onSubmit() {
        let workflow: Workflow = this.formDataService.getCachedCompanyDetails();
        workflow.processingContext.invitationCode = this.tpService.selectedinvCodeSubject;

        if (this.selectedKeys.length > 0) {
            this.workflow.provisioningRequestData.registrationData.tgtsProfiles.maps = [];

            this.selectedKeys.forEach(data => {
                let map = new Maps();
                map.mapName = data;
                this.workflow.provisioningRequestData.registrationData.tgtsProfiles.maps.push((map));

            })
        }
        if (null != this.tpService.selectedTpId && this.tpService.selectedTpId != "NA") {
            this.tpService.initiateWorkflow(workflow, this.tpService.selectedTpId).subscribe(data => {
                let workflowId = data['responseDetails']['responseEntity'].targetResourceRefId;
                this.showmessage(workflowId);
                this.formDataService.clearCache();
                this.router.navigate(['tpdir']);
            }, error => {
                this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        }
        else {
            this.tpService.createTP(workflow, this.onbaordingService.getInvIdForOnboarding()).subscribe(data => {
                let workflowId = data['responseDetails']['responseEntity'].targetResourceRefId;
                this.showmessage(workflowId);
                this.formDataService.clearCache();
                this.router.navigate(['tpdir']);
            }, error => {
                this.notficationHandler.notify({ severity: 'error', details: error.userMessage });
            });
        }
    }

    showmessage(data: string) {
        let message = {
            "title": "Workflow initiated successfully",
            "severity": "success",
            "details": "Workflow id : " + data
        }
        this.notficationHandler.notify(message);
    }
    onCancel() {
        this.formDataService.clearCache();
    }

}