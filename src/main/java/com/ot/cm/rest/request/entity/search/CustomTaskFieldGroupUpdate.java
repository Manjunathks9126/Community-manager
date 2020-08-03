package com.ot.cm.rest.request.entity.search;

import java.io.Serializable;
import java.util.List;

public class CustomTaskFieldGroupUpdate implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long taskId;

	private List<String> fgId;

	public CustomTaskFieldGroupUpdate() {

	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public List<String> getFgId() {
		return fgId;
	}

	public void setFgId(List<String> fgId) {
		this.fgId = fgId;
	}

	@Override
	public String toString() {
		return "CustomTaskFieldGroupUpdate [taskId=" + taskId + ", fgId=" + fgId + "]";
	}

}
