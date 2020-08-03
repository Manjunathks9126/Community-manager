package com.ot.cm.rest.response.entity;

import java.util.Map;

public class ImportCustomDataSave {
	
	private String groupId;
	private Map<String, String> value;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Map<String, String> getValue() {
		return value;
	}
	public void setValue(Map<String, String> value) {
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return "ImportCustomDataSave [groupId=" + groupId + ", value=" + value + "]";
	}
	
	

}
