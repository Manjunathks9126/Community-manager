package com.ot.cm.rest.response.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OnboardingTask implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long taskId;

	private String taskName;

	private String description;

	private String taskType;

	private String taskStage;
	
	private String jsonSchema;

	private String schemaName;

	private String createdBy;
	private Date createdTimestamp;

	private String modifiedBy;
	private Date modifiedTimestamp;

	private Set<OnboardingTaskL10N> taskL10ns;

	private String displayName;

	public OnboardingTask() {

	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskStage() {
		return taskStage;
	}

	public void setTaskStage(String taskStage) {
		this.taskStage = taskStage;
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

	public Set<OnboardingTaskL10N> getTaskL10ns() {
		return taskL10ns;
	}

	public void setTaskL10ns(Set<OnboardingTaskL10N> taskL10ns) {
		this.taskL10ns = taskL10ns;
	}

	public void addTaskL10ns(OnboardingTaskL10N taskL10n) {
		if (null == taskL10ns) {
			taskL10ns = new HashSet<OnboardingTaskL10N>();
		}
		taskL10ns.add(taskL10n);
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

}
