package com.ot.cm.rest.response.entity;

public class BillingSplitsEntity {
	String connectOutPayee;
	String sendPayee;
	String receivePayee;
	String storagePayee;
	public String getConnectOutPayee() {
		return connectOutPayee;
	}
	public void setConnectOutPayee(String connectOutPayee) {
		this.connectOutPayee = connectOutPayee;
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

}
