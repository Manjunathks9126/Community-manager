package com.ot.cm.rest.response.entity;

import java.util.Date;

public class InvitationDataSentResponse {
	
	private String invitationCode;
	private String emailAddress;
	private String ediAddresses;
	private String recepientInvitationStatus;
	private String companyIdentifier;
	private String identifierType;
	private String recipientCompanyId;
	private Date createdTimestamp;
	private InvitationNeoRest invitation;
	
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEdiAddresses() {
		return ediAddresses;
	}
	public void setEdiAddresses(String ediAddresses) {
		this.ediAddresses = ediAddresses;
	}
	public String getRecepientInvitationStatus() {
		return recepientInvitationStatus;
	}
	public void setRecepientInvitationStatus(String recepientInvitationStatus) {
		this.recepientInvitationStatus = recepientInvitationStatus;
	}
	public String getCompanyIdentifier() {
		return companyIdentifier;
	}
	public void setCompanyIdentifier(String companyIdentifier) {
		this.companyIdentifier = companyIdentifier;
	}
	public String getIdentifierType() {
		return identifierType;
	}
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}
	public String getRecipientCompanyId() {
		return recipientCompanyId;
	}
	public void setRecipientCompanyId(String recipientCompanyId) {
		this.recipientCompanyId = recipientCompanyId;
	}
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public InvitationNeoRest getInvitation() {
		return invitation;
	}
	public void setInvitation(InvitationNeoRest invitation) {
		this.invitation = invitation;
	}
	
	@Override
	public String toString() {
		return "InvitationDataSentResponse [invitationCode=" + invitationCode + ", emailAddress=" + emailAddress
				+ ", ediAddresses=" + ediAddresses + ", recepientInvitationStatus=" + recepientInvitationStatus
				+ ", companyIdentifier=" + companyIdentifier + ", identifierType=" + identifierType
				+ ", recipientCompanyId=" + recipientCompanyId + ", createdTimestamp=" + createdTimestamp
				+ ", invitation=" + invitation + "]";
	}
	
	

}
