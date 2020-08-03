package com.ot.cm.rest.response.entity;

import java.io.Serializable;
import java.util.Date;

public class OnboardingWorkflowRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long requestorId;
	private Long workflowId;
	private Long invitationId;
    private String invitationCode;
	private String status; 
	private String createdBy;
	private Date createdTimestamp;
	private String modifiedBy;
	private Date modifiedTimeStamp;

	public Long getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(Long requestorId) {
		this.requestorId = requestorId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Long getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}

	public String getinvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTimeStamp() {
		return createdTimestamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimestamp = createdTimeStamp;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(Date modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public OnboardingWorkflowRequest() {
		
	}

	public OnboardingWorkflowRequest(Long requestorId, Long workflowId, Long invitation_Id, String dataOutput,
			String status, String createdBy, Date createdTimeStamp, String modifiedBy, Date modifiedTimeStamp) {
		super();
		this.requestorId = requestorId;
		this.workflowId = workflowId;
		this.invitationId = invitation_Id;
		this.invitationCode = dataOutput;
		this.status = status;
		this.createdBy = createdBy;
		this.createdTimestamp = createdTimeStamp;
		this.modifiedBy = modifiedBy;
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	@Override
	public String toString() {
		return "OnboardingWorkflowRequest [requestorId=" + requestorId + ", workflowId=" + workflowId
				+ ", invitation_Id=" + invitationId + ", invitationCode=" + invitationCode + ", status=" + status
				+ ", createdBy=" + createdBy + ", createdTimeStamp=" + createdTimestamp + ", modifiedBy=" + modifiedBy
				+ ", modifiedTimeStamp=" + modifiedTimeStamp + "]";
	}
}