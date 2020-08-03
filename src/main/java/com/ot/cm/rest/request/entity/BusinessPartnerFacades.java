/**
 * 
 */
package com.ot.cm.rest.request.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ssen
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessPartnerFacades {

 private String uniqueId; 
 private String bprId;
 private String ownerBuId;
 private String targetBuId;
 private String targetBuCustomName;
 private String targetBuCustomId;
 


public String getTargetBuCustomId() {
	return targetBuCustomId;
}
public void setTargetBuCustomId(String targetBuCustomId) {
	this.targetBuCustomId = targetBuCustomId;
}
public String getUniqueId() {
	return uniqueId;
}
public void setUniqueId(String uniqueId) {
	this.uniqueId = uniqueId;
}
public String getBprId() {
	return bprId;
}
public void setBprId(String bprId) {
	this.bprId = bprId;
}
public String getOwnerBuId() {
	return ownerBuId;
}
public void setOwnerBuId(String ownerBuId) {
	this.ownerBuId = ownerBuId;
}
public String getTargetBuId() {
	return targetBuId;
}
public void setTargetBuId(String targetBuId) {
	this.targetBuId = targetBuId;
}
public String getTargetBuCustomName() {
	return targetBuCustomName;
}
public void setTargetBuCustomName(String targetBuCustomName) {
	this.targetBuCustomName = targetBuCustomName;
}
@Override
public String toString() {
	return "BusinessPartnerFacades [uniqueId=" + uniqueId + ", bprId=" + bprId + ", ownerBuId=" + ownerBuId
			+ ", targetBuId=" + targetBuId + ", targetBuCustomName=" + targetBuCustomName + "]";
}
 
 
	
}
