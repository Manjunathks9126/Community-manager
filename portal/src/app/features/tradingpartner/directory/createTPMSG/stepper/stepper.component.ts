import { Component } from "@angular/core";
import { TPMSGService } from "../../../../../services/tpmsg.service";

@Component({
    selector: 'ot-stepper',
    templateUrl: './stepper.component.html'
})

export class StepperComponent {

    constructor(private tpmsgService: TPMSGService) {

    }

    ngOnInit() {
        this.tpmsgService.TPMG_ROUTER_SUB.subscribe(step => {
            if (step == "CD") {
                this.companyActive();
            } else if (step == "MD") {
                this.msgActive();
            } else {
                this.summActive();
            }
        })
    }

    COMP_STATUS = ['cm-stepper-active-state', 'cm-stepper-completed-state'];
    MSG_STATUS = ['disabled', 'cm-stepper-active-state', 'cm-stepper-completed-state'];
    SUMM_STATUS = ['disabled', 'cm-stepper-active-state'];

    comp_step_counter = 0;
    msg_step_counter = 0;
    sum_step_counter = 0;

    ROUTE_STEP = "companyDetails";


    private companyActive() {
        this.comp_step_counter = 0;
        this.msg_step_counter = 0;
        this.sum_step_counter = 0;
    }
    private msgActive() {
        this.comp_step_counter = 1;
        this.msg_step_counter = 1;
        this.sum_step_counter = 0;
    }
    private summActive() {
        this.comp_step_counter = 1;
        this.msg_step_counter = 2;
        this.sum_step_counter = 1;
    }


    nextStep(stepName) {
        this.tpmsgService.nextStep(stepName);
    }
}