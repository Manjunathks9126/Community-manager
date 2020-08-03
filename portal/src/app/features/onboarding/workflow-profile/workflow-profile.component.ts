import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OnboardingService } from "../../../services/onboarding.service";
import * as _ from "lodash";
import { BreadCrumbsService } from "tgocp-ng/dist";
import { Constants } from '../../../services/AppConstant';


@Component({
    templateUrl: "./workflow-profile.component.html"
})
export class WorkflowProfileComponent implements OnInit {

    workflowName: string = "";
    showTaskTab: boolean = true;
    showInvTab: boolean = true;
    workflowTypes: any;

    constructor(private route: ActivatedRoute, private onboardingService: OnboardingService,
        private breadCrumbService: BreadCrumbsService, private router: Router) { }

    ngOnInit() {
        let workflowId = this.route.snapshot.params['id'];
        this.workflowTypes = Constants;
        let data = _.find(this.onboardingService.workflowList, function (data) { return data.workflowId == workflowId });
        if (_.isEqual(data.type, this.workflowTypes.Skynet) || _.isEqual(data.type, this.workflowTypes.Teambook)) {
            this.showTaskTab = false;
            this.showInvTab = false;
        }
        else if (!_.isEqual(data.type, this.workflowTypes.TaskBased)) {
            this.showTaskTab = false;
        }
		 if (!_.isEqual(data.ownerBuId, this.onboardingService.loggedBuId)) {
           this.showInvTab = false;
       }

        this.onboardingService.selectedWorkFlow = data;
        this.workflowName = data.displayName;
        this.breadCrumbService.addBreadCrumbItem({ label: this.workflowName, url: this.router.url });
        this.router.navigate([this.router.url], { skipLocationChange: true });
    }
}