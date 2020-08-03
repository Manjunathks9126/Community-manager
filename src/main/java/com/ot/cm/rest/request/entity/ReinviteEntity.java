package com.ot.cm.rest.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ReinviteEntity {
	
	private String invitationCode;
	private Long invitationId;
	private Long workflowId;
	private Long taskId;
	private String serviceName;
	private CompanyDetailsEntity companyDetailsEntity;
	private String requestorTask;
	
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public Long getInvitationId() {
		return invitationId;
	}
	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}
	public Long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public CompanyDetailsEntity getCompanyDetailsEntity() {
		return companyDetailsEntity;
	}
	public void setCompanyDetailsEntity(CompanyDetailsEntity companyDetailsEntity) {
		this.companyDetailsEntity = companyDetailsEntity;
	}
	public String getRequestorTask() {
		return requestorTask;
	}
	public void setRequestorTask(String requestorTask) {
		this.requestorTask = requestorTask;
	}
	
	
}
