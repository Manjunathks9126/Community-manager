package com.ot.cm.rest.response.onboarding.entity;

import java.util.List;
import java.util.Map;

public class RegistrationTaskOutputWrapper {
	public List<RegistrationTaskOutput> tasks;
	public Map<String, Object> contextData;

	public List<RegistrationTaskOutput> getTasks() {
		return tasks;
	}

	public void setTasks(List<RegistrationTaskOutput> tasks) {
		this.tasks = tasks;
	}

	public Map<String, Object> getContextData() {
		return contextData;
	}

	public void setContextData(Map<String, Object> contextData) {
		this.contextData = contextData;
	}
}
