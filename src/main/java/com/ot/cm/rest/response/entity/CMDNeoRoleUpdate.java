package com.ot.cm.rest.response.entity;

public class CMDNeoRoleUpdate {
	private String roleId;
	private boolean newValue = false;
	private boolean oldValue;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean isNewValue() {
		return newValue;
	}

	public void setNewValue(boolean newValue) {
		this.newValue = newValue;
	}

	public boolean isOldValue() {
		return oldValue;
	}

	public void setOldValue(boolean oldValue) {
		this.oldValue = oldValue;
	}

}
