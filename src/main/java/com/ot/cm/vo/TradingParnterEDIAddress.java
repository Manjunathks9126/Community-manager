package com.ot.cm.vo;

import com.gxs.services.bsapi.rs.v2.entity.BillingSplitsType;

public class TradingParnterEDIAddress {
	private String tradingPartnerRelationshipId;
	private String partnershipDirection;
	private String ownerEDIAddress;
	private String gsId;
	private String createTimeStamp;
	private String partnerEDIAddress;
	private BillingSplitsType billingSplit;

	public String getTradingPartnerRelationshipId() {
		return tradingPartnerRelationshipId;
	}

	public void setTradingPartnerRelationshipId(String tradingPartnerRelationshipId) {
		this.tradingPartnerRelationshipId = tradingPartnerRelationshipId;
	}

	public String getPartnershipDirection() {
		return partnershipDirection;
	}

	public void setPartnershipDirection(String partnershipDirection) {
		this.partnershipDirection = partnershipDirection;
	}

	public String getOwnerEDIAddress() {
		return ownerEDIAddress;
	}

	public void setOwnerEDIAddress(String ownerEDIAddress) {
		this.ownerEDIAddress = ownerEDIAddress;
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

	public String getPartnerEDIAddress() {
		return partnerEDIAddress;
	}

	public void setPartnerEDIAddress(String partnerEDIAddress) {
		this.partnerEDIAddress = partnerEDIAddress;
	}

	public BillingSplitsType getBillingSplit() {
		return billingSplit;
	}

	public void setBillingSplit(BillingSplitsType billingSplit) {
		this.billingSplit = billingSplit;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradingParnterEDIAddress [tradingPartnerRelationshipId=").append(tradingPartnerRelationshipId)
				.append(", partnershipDirection=").append(partnershipDirection).append(", ownerEDIAddress=")
				.append(ownerEDIAddress).append(", gsId=").append(gsId).append(", createTimeStamp=")
				.append(createTimeStamp).append(", partnerEDIAddress=").append(partnerEDIAddress)
				.append(", billingSplit=").append(billingSplit).append("]");
		return builder.toString();
	}

	

}
