import { Injectable }                        from '@angular/core';
import { FormData}       from './formData.model';
import { WorkflowService }                   from '../workflow/workflow.service';
import { STEPS }                             from '../workflow/workflow.model';
import { Company } from './company.entity';
import { Workflow } from '../../directory/createTPMSG/entity/workflow.entity';


@Injectable()
export class FormDataService {

    private formData: FormData = new FormData();
    private isPersonalFormValid: boolean = false;
    private isWorkFormValid: boolean = false;
    private isAddressFormValid: boolean = false;

    cachedworkFlow: Workflow = new Workflow();
    constructor(private workflowService: WorkflowService) { 
    }

    getcompanyDetails(): Company {
        // Return the Personal data
        var company: Company = {
            displayName:this.formData.displayName,
            companyName:this.formData.companyName,
            addressLine1:this.formData.addressLine1,
            addressLine2:this.formData.addressLine2,
            city:this.formData.city,
            state:this.formData.state,
            postalcode:this.formData.postalcode,
            tptype:this.formData.tptype,
            companyId:this.formData.companyId,
            country:this.formData.country,
            companyWebsiteURL:this.formData.companyWebsiteURL,
            phone:this.formData.phone,
            email:this.formData.email,
            contact:this.formData.contact,
            selectedInvCode:this.formData.selectedInvCode,
            keys:this.formData.keys
        };
        return company;
    }

    setCompanyDetails(data: Company) {
        // Update the Personal data only when the Personal Form had been validated successfully
        this.isPersonalFormValid = true;
            this.formData.displayName=data.displayName;
            this.formData.companyName=data.companyName;
            this.formData.addressLine1=data.addressLine1;
            this.formData.addressLine2=data.addressLine2;
           this.formData.city=data.city;
            this.formData.state=data.state;
            this.formData.postalcode=data.postalcode;
            this.formData.tptype=data.tptype;
           this.formData.companyId=data.companyId;
           this.formData.country=data.country;
            this.formData.companyWebsiteURL=data.companyWebsiteURL;
            this.formData.phone=data.phone;
            this.formData.email=data.email;
            this.formData.contact=data.contact;
        // Validate Personal Step in Workflow
        this.workflowService.validateStep(STEPS.add);
    }


    getFormData(): FormData {
        // Return the entire Form Data
        return this.formData;
    }

    resetFormData(): FormData {
        // Reset the workflow
        this.workflowService.resetSteps();
        // Return the form data after all this.* members had been reset
        this.formData.clear();
        this.isPersonalFormValid = this.isWorkFormValid = this.isAddressFormValid = false;
        return this.formData;
    }

    isFormValid() {
        // Return true if all forms had been validated successfully; otherwise, return false
        return this.isPersonalFormValid &&
                this.isWorkFormValid && 
                this.isAddressFormValid;
    }

     setcompanyDetailsEditMode(displayName:string,companyName:string,addressLine1:string,addressLine2:string,city:string,state:string
        ,postalcode:string,country:string,companyWebsiteURL:string) {
            this.formData.displayName=displayName;
            this.formData.companyName=companyName;
            this.formData.addressLine1=addressLine1;
            this.formData.addressLine2=addressLine2;
            this.formData.city=city;
            this.formData.state=state;
            this.formData.postalcode=postalcode;
           this.formData.country=country;
            this.formData.companyWebsiteURL=companyWebsiteURL;
           }
    setCachedCompanyDetails(workflow: Workflow) {
        this.cachedworkFlow = workflow;
       /*  if(country) {
            this.cachedworkFlow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode=country;
        } */
        this.workflowService.validateStep(STEPS.add);

    }
    getCachedCompanyDetails() {
        return this.cachedworkFlow;
    }
    clearCache() {
        this.workflowService.resetSteps();
        // Return the form data after all this.* members had been reset
        this.cachedworkFlow = new Workflow();       
        this.isPersonalFormValid = this.isWorkFormValid = this.isAddressFormValid = false;
        return this.cachedworkFlow;

    }
}