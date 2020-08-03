package com.ot.cm.rest.response.entity;

public class CMDCustomFieldGroupRole {

	private String roleId;

	private String fieldGroupId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getFieldGroupId() {
		return fieldGroupId;
	}

	public void setFieldGroupId(String fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldGroupId == null) ? 0 : fieldGroupId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CMDCustomFieldGroupRole other = (CMDCustomFieldGroupRole) obj;
		if (fieldGroupId == null) {
			if (other.fieldGroupId != null)
				return false;
		} else if (!fieldGroupId.equals(other.fieldGroupId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}

}
