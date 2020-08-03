package com.ot.cm.rest.response.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class TPDirectory extends CompanyAddress{
	private String bprId;
	@JsonProperty("BUID")
	private String businessUnitId;
	@JsonProperty("Company Name")
	private String name;
	@JsonProperty("Display Name")
	private String displayName;
	@JsonProperty("Partner Type")
	private String tradingPartnerType;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Creation Date")
	private String createTimeStamp;
	@JsonProperty("Edi Address")
	private List<String> ediAddresses;
	@JsonProperty("Company ID")
	private String facadeCompanyId;
	@JsonProperty("Bpr Status")
	private String bprStatus;

	public String getFacadeCompanyId() {
		return facadeCompanyId;
	}

	public void setFacadeCompanyId(String facadeCompanyId) {
		this.facadeCompanyId = facadeCompanyId;
	}

	public String getBprStatus() {
		return bprStatus;
	}

	public void setBprStatus(String bprStatus) {
		this.bprStatus = bprStatus;
	}

	public String getBprId() {
		return bprId;
	}

	public void setBprId(String bprId) {
		this.bprId = bprId;
	}

	public String getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public List<String> getEdiAddresses() {
		return ediAddresses;
	}

	public void setEdiAddresses(List<String> ediAddresses) {
		this.ediAddresses = ediAddresses;
	}

	public String getTradingPartnerType() {
		return tradingPartnerType;
	}

	public void setTradingPartnerType(String tradingPartnerType) {
		this.tradingPartnerType = tradingPartnerType;
	}

	@Override
	public String toString() {
		return "TPDirectory [bprId=" + bprId + ", businessUnitId=" + businessUnitId + ", name=" + name
				+  ", status=" + status + ", createTimeStamp=" + createTimeStamp
				+ ", ediAddresses=" + ediAddresses + "]";
	}

}
