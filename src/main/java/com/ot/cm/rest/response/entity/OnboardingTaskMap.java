package com.ot.cm.rest.response.entity;

import java.util.Date;
import java.util.List;

import com.ot.cm.rest.request.entity.search.CustomTaskFieldGroupUpdate;

public class OnboardingTaskMap {

	private Long workflowTaskId;
	private Long workflowId;
	private Long taskId;
	private Long position;
	private String createdBy;
	private Date createdTimestamp;
	private String modifiedBy;
	private Date modifiedTimestamp;
	private List<String> fgId;
	private OnboardingTask task;
	private CustomTaskFieldGroupUpdate fieldgroup;

	public CustomTaskFieldGroupUpdate getFieldgroup() {
		return fieldgroup;
	}

	public void setFieldgroup(CustomTaskFieldGroupUpdate fieldgroup) {
		this.fieldgroup = fieldgroup;
	}

	public List<String> getFgId() {
		return fgId;
	}

	public void setFgId(List<String> fgId) {
		this.fgId = fgId;
	}

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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	@Override
	public String toString() {
		return "OnboardingTaskMap [workflowTaskId=" + workflowTaskId + ", workflowId=" + workflowId + ", taskId="
				+ taskId + ", position=" + position + ", createdBy=" + createdBy + ", createdTimestamp="
				+ createdTimestamp + ", modifiedBy=" + modifiedBy + ", modifiedTimestamp=" + modifiedTimestamp
				+ ", task=" + task + "]";
	}

}
