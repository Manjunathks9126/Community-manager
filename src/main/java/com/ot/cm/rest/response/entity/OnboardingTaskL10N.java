package com.ot.cm.rest.response.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OnboardingTaskL10N implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long taskL10nId;

	private String displayName;

	private String locale;

	private String createdBy;
	private Date createdTimestamp;

	private String modifiedBy;
	private Date modifiedTimestamp;

	private OnboardingTask task;

	
	public OnboardingTaskL10N() {
		super();
	}

	public OnboardingTaskL10N(Long taskL10nId, String displayName, String locale, String createdBy,
			Date createdTimestamp, String modifiedBy, Date modifiedTimestamp, OnboardingTask task) {
		super();
		this.taskL10nId = taskL10nId;
		this.displayName = displayName;
		this.locale = locale;
		this.createdBy = createdBy;
		this.createdTimestamp = createdTimestamp;
		this.modifiedBy = modifiedBy;
		this.modifiedTimestamp = modifiedTimestamp;
		this.task = task;
	}

	public Long getTaskL10nId() {
		return taskL10nId;
	}

	public void setTaskL10nId(Long taskL10nId) {
		this.taskL10nId = taskL10nId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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
