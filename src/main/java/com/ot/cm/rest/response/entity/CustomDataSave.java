package com.ot.cm.rest.response.entity;

import java.util.Map;

public class CustomDataSave {
	
	private String groupId;
	private Map<String, ?> value;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Map<String, ?> getValue() {
		return value;
	}
	public void setValue(Map<String, ?> value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "CustomDataSave [groupId=" + groupId + ", value=" + value + "]";
	}
	
	

}
