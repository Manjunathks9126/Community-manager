package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CMDNeoRole {

	private String uniqueId;
	private String roleName;
	private String roleDisplayName;
	private String roleDescription;
	private String roleQualifier;
	private OwnerBusinessUnit ownerBusinessUnit;
	private String creationDate;
	private String lastModifiedDate;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDisplayName() {
		return roleDisplayName;
	}

	public void setRoleDisplayName(String roleDisplayName) {
		this.roleDisplayName = roleDisplayName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleQualifier() {
		return roleQualifier;
	}

	public void setRoleQualifier(String roleQualifier) {
		this.roleQualifier = roleQualifier;
	}

	public OwnerBusinessUnit getOwnerBusinessUnit() {
		return ownerBusinessUnit;
	}

	public void setOwnerBusinessUnit(OwnerBusinessUnit ownerBusinessUnit) {
		this.ownerBusinessUnit = ownerBusinessUnit;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
