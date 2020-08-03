package com.ot.cm.rest.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvitationDataSentRequest {

	private Long invitationId;

	private String emailAddress;

	private String ediAddresses;

	private String recepientInvitationStatus;

	private String companyIdentifier;

	private String identifierType;

	private String recipientCompanyId;

	public InvitationDataSentRequest(String ediAddresses, String recepientInvitationStatus, String companyIdentifier,
			String identifierType, String recipientCompanyId, String emailAddress, Long invitationId) {
		this.ediAddresses = ediAddresses;
		this.recepientInvitationStatus = recepientInvitationStatus;
		this.companyIdentifier = companyIdentifier;
		this.identifierType = identifierType;
		this.recipientCompanyId = recipientCompanyId;
		this.emailAddress = emailAddress;
		this.invitationId = invitationId;
	}

	public InvitationDataSentRequest() {

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

	public Long getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		InvitationDataSentRequest that = (InvitationDataSentRequest) o;

		if (!ediAddresses.equals(that.ediAddresses))
			return false;
		if (!recepientInvitationStatus.equals(that.recepientInvitationStatus))
			return false;
		if (!companyIdentifier.equals(that.companyIdentifier))
			return false;
		if (!identifierType.equals(that.identifierType))
			return false;
		return recipientCompanyId.equals(that.recipientCompanyId);
	}

	@Override
	public int hashCode() {
		int result = invitationId.hashCode();
		result = 31 * result + emailAddress.hashCode();

		return result;
	}
}
