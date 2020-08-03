package com.ot.cm.rest.response.onboarding.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "workflow")
public class RegistrationWorkflow {
	
	private RegistartionProcessingContext processingContext;
	
	private RegistrationProvisioningData provisioningRequestData;

	public RegistartionProcessingContext getProcessingContext() {
		return processingContext;
	}

	public void setProcessingContext(RegistartionProcessingContext processingContext) {
		this.processingContext = processingContext;
	}

	public RegistrationProvisioningData getProvisioningRequestData() {
		return provisioningRequestData;
	}

	public void setProvisioningRequestData(RegistrationProvisioningData provisioningRequestData) {
		this.provisioningRequestData = provisioningRequestData;
	}

}
