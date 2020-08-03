package com.ot.cm.rest.request.entity;

import java.io.Serializable;

public class InvitedCompanyRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172290921246972611L;
	private String workflowName;
	private String displayName;
	private String workflowId;
	private String requestId;
	private String serviceName;
	private String invitationCode;
	private String companyIdentifier;
	private String recipientCompanyId;
	private String invitationId;
	private String invitationName;
	private String invitationStatus;
	private String activeDate;
	private String invitedDate;
	private String creatorCompanyId;
	private String email;
	private Integer daysOpen;

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public String getInvitationName() {
		return invitationName;
	}

	public void setInvitationName(String invitationName) {
		this.invitationName = invitationName;
	}

	public String getInvitationStatus() {
		return invitationStatus;
	}

	public void setInvitationStatus(String invitationStatus) {
		this.invitationStatus = invitationStatus;
	}

	public String getCreatorCompanyId() {
		return creatorCompanyId;
	}

	public void setCreatorCompanyId(String creatorCompanyId) {
		this.creatorCompanyId = creatorCompanyId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getInvitedDate() {
		return invitedDate;
	}

	public void setInvitedDate(String invitedDate) {
		this.invitedDate = invitedDate;
	}

	public String getCompanyIdentifier() {
		return companyIdentifier;
	}

	public void setCompanyIdentifier(String companyIdentifier) {
		this.companyIdentifier = companyIdentifier;
	}

	public String getRecipientCompanyId() {
		return recipientCompanyId;
	}

	public void setRecipientCompanyId(String recipientCompanyId) {
		this.recipientCompanyId = recipientCompanyId;
	}

	public Integer getDaysOpen() {
		return daysOpen;
	}

	public void setDaysOpen(Integer daysOpen) {
		this.daysOpen = daysOpen;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
