package com.ot.cm.rest.request.entity;

import com.gxs.services.bsapi.rs.v3.entity.ModifyTradingPartnerRelationshipType;

public class TradingPartnerModifyEntity {
	String companyId;
	String tprId;
	String companyName;
	String displayName;
	String yourRole;
	ModifyTradingPartnerRelationshipType modifiedData;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getTprId() {
		return tprId;
	}
	public void setTprId(String tprId) {
		this.tprId = tprId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getYourRole() {
		return yourRole;
	}
	public void setYourRole(String yourRole) {
		this.yourRole = yourRole;
	}
	public ModifyTradingPartnerRelationshipType getModifiedData() {
		return modifiedData;
	}
	public void setModifiedData(ModifyTradingPartnerRelationshipType modifiedData) {
		this.modifiedData = modifiedData;
	}

}
