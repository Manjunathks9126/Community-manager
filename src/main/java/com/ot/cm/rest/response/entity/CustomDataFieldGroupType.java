package com.ot.cm.rest.response.entity;

public class CustomDataFieldGroupType extends CustomFieldGroupType {

	private boolean isHidden;
	private String dependentGroupId;
	private boolean isReadOnly;
	private CustomDataFieldtype dependentCustomField;

	public Boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getDependentGroupId() {
		return dependentGroupId;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public void setDependentGroupId(String dependentGroupId) {
		this.dependentGroupId = dependentGroupId;
	}

	public CustomFieldEntity getDependentCustomField() {
		return dependentCustomField;
	}

	public void setDependentCustomField(CustomDataFieldtype dependentCustomField) {
		this.dependentCustomField = dependentCustomField;
	}

	@Override
	public String toString() {
		return "CustomDataFieldGroupType [isHidden=" + isHidden + ", dependentGroupId=" + dependentGroupId
				+ ", dependentCustomField=" + dependentCustomField + "]";
	}

}
