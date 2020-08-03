package com.ot.cm.rest.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ot.cm.rest.response.entity.InvitationNeoRest;

@JsonIgnoreProperties
public class OnboardingInvitationWrapper {
	private InvitationNeoRest invitationEntity;
	private CompanyDetailsEntity companyDetailsEntity;
	private String requestorTask;
	private String serviceName;
	private Long workflowId;
	private Long taskId;

	public OnboardingInvitationWrapper(InvitationNeoRest invitationEntity, CompanyDetailsEntity companyDetailsEntity) {
		this.invitationEntity = invitationEntity;
		this.companyDetailsEntity = companyDetailsEntity;
	}

	public OnboardingInvitationWrapper() {

	}

	public InvitationNeoRest getInvitationEntity() {
		return invitationEntity;
	}

	public void setInvitationEntity(InvitationNeoRest invitationEntity) {
		this.invitationEntity = invitationEntity;
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

}
