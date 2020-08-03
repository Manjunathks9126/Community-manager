package com.ot.cm.rest.response.entity;

import java.io.Serializable;
import java.util.Date;

public class OnboardingWorkflowTaskMap implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long workflowTaskId;

	
	private Long workflowId;

	
	private Long position;

	
	private String createdBy;
	private Date createdTimestamp;

	private String modifiedBy;
	private Date modifiedTimestamp;

	private OnboardingTask task;

	public Long getWorkflowTaskId() {
		return workflowTaskId;
	}

	public void setWorkflowTaskId(Long workflowTaskId) {
		this.workflowTaskId = workflowTaskId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Date modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public OnboardingTask getTask() {
		return task;
	}

	public void setTask(OnboardingTask task) {
		this.task = task;
	}

}
