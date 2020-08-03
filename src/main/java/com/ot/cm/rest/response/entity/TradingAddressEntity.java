package com.ot.cm.rest.response.entity;

public class TradingAddressEntity {
	String address;
	String alias;
	String qualifier;
	BaseTradingAddressEntity parentTradingAddress;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public BaseTradingAddressEntity getParentTradingAddress() {
		return parentTradingAddress;
	}
	public void setParentTradingAddress(BaseTradingAddressEntity parentTradingAddress) {
		this.parentTradingAddress = parentTradingAddress;
	}

}
