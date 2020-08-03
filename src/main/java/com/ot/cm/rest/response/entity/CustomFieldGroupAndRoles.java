package com.ot.cm.rest.response.entity;

public class CustomFieldGroupAndRoles {

	private CustomFieldGroupType fgData;
	private CMDNeoRoleUpdate[] selectedRoles;

	public CustomFieldGroupType getFgData() {
		return fgData;
	}

	public void setFgData(CustomFieldGroupType fgData) {
		this.fgData = fgData;
	}

	public CMDNeoRoleUpdate[] getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(CMDNeoRoleUpdate[] selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

}
