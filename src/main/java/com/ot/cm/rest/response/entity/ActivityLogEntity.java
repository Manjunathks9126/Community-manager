package com.ot.cm.rest.response.entity;

public class ActivityLogEntity {
	String modifyTimeStamp;
	String createTimeStamp;
	String createdBy;
	String modifiedBy;
	public String getModifyTimeStamp() {
		return modifyTimeStamp;
	}
	public void setModifyTimeStamp(String modifyTimeStamp) {
		this.modifyTimeStamp = modifyTimeStamp;
	}
	public String getCreateTimeStamp() {
		return createTimeStamp;
	}
	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
