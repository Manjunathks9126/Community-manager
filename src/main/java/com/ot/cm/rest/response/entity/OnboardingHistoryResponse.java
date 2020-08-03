package com.ot.cm.rest.response.entity;

import java.io.Serializable;

public class OnboardingHistoryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OnboardingCompanyInformation companyInfo;
	private OnboardingProvisioningDetails provisioningDetails;

	public OnboardingCompanyInformation getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(OnboardingCompanyInformation companyInfo) {
		this.companyInfo = companyInfo;
	}

	public OnboardingProvisioningDetails getProvisioningDetails() {
		return provisioningDetails;
	}

	public void setProvisioningDetails(OnboardingProvisioningDetails provisioningDetails) {
		this.provisioningDetails = provisioningDetails;
	}

}
