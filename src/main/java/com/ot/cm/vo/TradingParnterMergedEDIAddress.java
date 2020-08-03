package com.ot.cm.vo;

import java.util.List;

public class TradingParnterMergedEDIAddress {
	private List<String> tradingPartnerRelationshipId;
	private List<String> partnershipDirection;
	private String gsId;
	private String createTimeStamp;
	private String ediAddress;
	private List<TradingPartnerBillingSplit> billingSplit;

	public List<TradingPartnerBillingSplit> getBillingSplit() {
		return billingSplit;
	}

	public void setBillingSplit(List<TradingPartnerBillingSplit> billingSplit) {
		this.billingSplit = billingSplit;
	}

	public List<String> getTradingPartnerRelationshipId() {
		return tradingPartnerRelationshipId;
	}

	public void setTradingPartnerRelationshipId(List<String> tradingPartnerRelationshipId) {
		this.tradingPartnerRelationshipId = tradingPartnerRelationshipId;
	}

	public List<String> getPartnershipDirection() {
		return partnershipDirection;
	}

	public void setPartnershipDirection(List<String> partnershipDirection) {
		this.partnershipDirection = partnershipDirection;
	}

	public String getGsId() {
		return gsId;
	}

	public void setGsId(String gsId) {
		this.gsId = gsId;
	}

	public String getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getEdiAddress() {
		return ediAddress;
	}

	public void setEdiAddress(String ediAddress) {
		this.ediAddress = ediAddress;
	}

	// @Override
	// public String toString() {
	// StringBuilder builder = new StringBuilder();
	// builder.append("EDIAddress
	// [tradingPartnerRelationshipId=").append(tradingPartnerRelationshipId)
	// .append(", partnershipDirection=").append(partnershipDirection).append(",
	// gsId=").append(gsId)
	// .append(", createTimeStamp=").append(createTimeStamp).append(",
	// ediAddress=").append(ediAddress)
	// .append("]");
	// return builder.toString();
	// }

}
