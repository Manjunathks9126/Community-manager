package com.ot.cm.vo;

public class TradingPartnerBillingSplit {

	private String partnershipDirection;
	private String sendPayee;
	private String receivePayee;
	private String storagePayee;
	private String connectOutPayee;

	public String getPartnershipDirection() {
		return partnershipDirection;
	}

	public void setPartnershipDirection(String partnershipDirection) {
		this.partnershipDirection = partnershipDirection;
	}

	public String getSendPayee() {
		return sendPayee;
	}

	public void setSendPayee(String sendPayee) {
		this.sendPayee = sendPayee;
	}

	public String getReceivePayee() {
		return receivePayee;
	}

	public void setReceivePayee(String receivePayee) {
		this.receivePayee = receivePayee;
	}

	public String getStoragePayee() {
		return storagePayee;
	}

	public void setStoragePayee(String storagePayee) {
		this.storagePayee = storagePayee;
	}

	public String getConnectOutPayee() {
		return connectOutPayee;
	}

	public void setConnectOutPayee(String connectOutPayee) {
		this.connectOutPayee = connectOutPayee;
	}

}
