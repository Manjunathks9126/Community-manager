import { Injectable } from '@angular/core';
import { STEPS } from './steps.model';


@Injectable()
export class WorkflowTpMsgService {
    private workflow = [
        { step: STEPS.add, valid: false },
        { step: STEPS.messagedetails, valid: false },
        { step: STEPS.billingSplit, valid: false },
        { step: STEPS.summary, valid: false }
    ];


    isStepValid(step) {
        let isValid = false;
        this.workflow.forEach(data => {
            if (data.step == step && data.valid) {
                isValid = true;
            }
        });
        return isValid;
    }

    validateStep(step: string) {
        // If the state is found, set the valid field to true 
        var found = false;
        for (var i = 0; i < this.workflow.length && !found; i++) {
            if (this.workflow[i].step === step) {
                found = this.workflow[i].valid = true;
            }
        }
    }

    resetSteps() {
        // Reset all the steps in the Workflow to be invalid
        this.workflow.forEach(element => {
            element.valid = false;
        });
    }

    getFirstInvalidStep(step: string, router): string {
        // If all the previous steps are validated, return blank
        // Otherwise, return the first invalid step

        var redirectToStep = '';

        if (router.url.indexOf("tpmsgbase") > 0) {
            redirectToStep = this.getTpmsgRouter(step);
        } else {
            redirectToStep = this.getTGMSRouter(step);
        }

        return redirectToStep;
    }



    private getTGMSRouter(step) {
        var found = false;
        var valid = true;
        var redirectToStep = '';
        for (var i = 0; i < this.workflow.length && !found && valid; i++) {

            if (i == 1) {
                continue;
            }
            let item = this.workflow[i];
            if (item.step === step) {
                found = true;
                redirectToStep = '';
            }
            else if (i == 0) {
                valid = item.valid;
                redirectToStep = '/tpdir/tgmsbase/add';
            } else {
                valid = item.valid;
                redirectToStep = item.step
            }
        }
        return redirectToStep;
    }

    private getTpmsgRouter(step) {
        var found = false;
        var valid = true;
        var redirectToStep = '';
        for (var i = 0; i < this.workflow.length && !found && valid; i++) {
            if (i == 2) {
                continue;
            }
            let item = this.workflow[i];
            if (item.step === step) {
                found = true;
                redirectToStep = '';
            }
            else {
                valid = item.valid;
                redirectToStep = item.step
            }
        }
        return redirectToStep;
    }

}