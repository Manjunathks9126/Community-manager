package com.ot.cm.rest.response.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.JsonDateSerializer;

@JsonPropertyOrder({ "Workflow Name", "Invitation Template", "Invitation Code", "Service", "Company Display Name",
		"Company ID", "Company Name", "Company City", "Country Code", "Company Status", "Requester Name",
		"Initiated On", "Go live (active) Date", "Tracking ID", "Workflow ID", "Provisioning Status", "Details" })
public class OnboardingWorkflowReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172290921246972611L;
	// display Name is facade name for workflow
	// which is displayed to the user as Worflow name hence mapping displayName
	// as workflow name
	@JsonProperty("Workflow Name")
	private String displayName;
	@JsonProperty("Service")
	private String serviceName;
	@JsonProperty("Company Display Name")
	private String companyDisplayName;
	@JsonProperty("Company ID")
	private String companyId;
	@JsonProperty("Company Name")
	private String companyName;
	@JsonProperty("Company City")
	private String companyCity;
	@JsonProperty("Country Code")
	private String countryCode;
	@JsonProperty("Company Status")
	private String companyStatus;
	@JsonProperty("Requester Name")
	private String requesterName;
	@JsonProperty("Initiated On")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date invitedDate;
	@JsonProperty("Go live (active) Date")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date completedDate;
	@JsonProperty("Invitation Template")
	private String invName;
	@JsonProperty("Invitation Code")
	private String invCode;
	@JsonProperty("Tracking ID")
	private String trackingId;
	@JsonProperty("Workflow ID")
	private String workflowId;
	@JsonProperty("Provisioning Status")
	private String reqStatus;
	@JsonProperty("Details")
	private String description;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = CommonUtil.toTitleCase(companyStatus);
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public Date getInvitedDate() {
		return invitedDate;
	}

	public void setInvitedDate(Date invitedDate) {
		this.invitedDate = invitedDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getInvName() {
		return invName;
	}

	public void setInvName(String invName) {
		this.invName = invName;
	}

	public String getInvCode() {
		return invCode;
	}

	public void setInvCode(String invCode) {
		this.invCode = invCode;
	}

	public String getCompanyCity() {
		return companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCompanyDisplayName() {
		return companyDisplayName;
	}

	public void setCompanyDisplayName(String companyDisplayName) {
		this.companyDisplayName = companyDisplayName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
