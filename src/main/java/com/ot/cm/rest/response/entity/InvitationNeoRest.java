package com.ot.cm.rest.response.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlAccessorType(XmlAccessType.FIELD)
public class InvitationNeoRest {
	private String approvalNotify;
	private Long invitationId;
    private String approvalType;
    private String ccRecipients;
    private String communityId;
    private String createdTimestamp;   
     private String creatorCompanyId;
    private String creatorCompanyName;
    private String creatorUserId;
    private String creatorFirstName;
    private String creatorLastName;
    private String ediAddrCreateOption;
    private String emailMessage;
    private String emailNotify;
    private String invitationComments;
    private String invitationActivationDate;
    private String invitationExpirationDate;
    private String invitationName;
    private String invitationStatus;
    private String invitationType;
    private String inviteeType;
    private String invitingCoEdiAddr;
    private String invitingCompanyId;
    private String invitingCompanyName;
    private String lastModifiedTimestamp;
    private String notificationEmailRecipients;
    private List<InvitationExtendedRequestNeoRest> invitationExtendedRequest;
    private EmailMessageResponse invitationMessage;
    private byte[] imageContent;
    private String imageName;
    private boolean editMode;
	public boolean isEditMode() {
		return editMode;
	}
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public byte[] getImageContent() {
		return imageContent;
	}
	public void setImageContent(byte[] imageContent) {
		this.imageContent = imageContent;
	}
	public EmailMessageResponse getInvitationMessage() {
		return invitationMessage;
	}
	public void setInvitationMessage(EmailMessageResponse invitationMessage) {
		this.invitationMessage = invitationMessage;
	}
	public String getApprovalNotify() {
		return approvalNotify;
	}
	public void setApprovalNotify(String approvalNotify) {
		this.approvalNotify = approvalNotify;
	}
	public Long getInvitationId() {
		return invitationId;
	}
	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	public String getCcRecipients() {
		return ccRecipients;
	}
	public void setCcRecipients(String ccRecipients) {
		this.ccRecipients = ccRecipients;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getCreatorCompanyId() {
		return creatorCompanyId;
	}
	public void setCreatorCompanyId(String creatorCompanyId) {
		this.creatorCompanyId = creatorCompanyId;
	}
	public String getCreatorCompanyName() {
		return creatorCompanyName;
	}
	public void setCreatorCompanyName(String creatorCompanyName) {
		this.creatorCompanyName = creatorCompanyName;
	}
	public String getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public String getCreatorFirstName() {
		return creatorFirstName;
	}
	public void setCreatorFirstName(String creatorFirstName) {
		this.creatorFirstName = creatorFirstName;
	}
	public String getCreatorLastName() {
		return creatorLastName;
	}
	public void setCreatorLastName(String creatorLastName) {
		this.creatorLastName = creatorLastName;
	}
	public String getEdiAddrCreateOption() {
		return ediAddrCreateOption;
	}
	public void setEdiAddrCreateOption(String ediAddrCreateOption) {
		this.ediAddrCreateOption = ediAddrCreateOption;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public String getEmailNotify() {
		return emailNotify;
	}
	public void setEmailNotify(String emailNotify) {
		this.emailNotify = emailNotify;
	}
	public String getInvitationComments() {
		return invitationComments;
	}
	public void setInvitationComments(String invitationComments) {
		this.invitationComments = invitationComments;
	}
	public String getInvitationActivationDate() {
		return invitationActivationDate;
	}
	public void setInvitationActivationDate(String invitationActivationDate) {
		this.invitationActivationDate = invitationActivationDate;
	}
	public String getInvitationExpirationDate() {
		return invitationExpirationDate;
	}
	public void setInvitationExpirationDate(String invitationExpirationDate) {
		this.invitationExpirationDate = invitationExpirationDate;
	}
	public String getInvitationName() {
		return invitationName;
	}
	public void setInvitationName(String invitationName) {
		this.invitationName = invitationName;
	}
	public String getInvitationStatus() {
		return invitationStatus;
	}
	public void setInvitationStatus(String invitationStatus) {
		this.invitationStatus = invitationStatus;
	}
	public String getInvitationType() {
		return invitationType;
	}
	public void setInvitationType(String invitationType) {
		this.invitationType = invitationType;
	}
	public String getInviteeType() {
		return inviteeType;
	}
	public void setInviteeType(String inviteeType) {
		this.inviteeType = inviteeType;
	}
	public String getInvitingCoEdiAddr() {
		return invitingCoEdiAddr;
	}
	public void setInvitingCoEdiAddr(String invitingCoEdiAddr) {
		this.invitingCoEdiAddr = invitingCoEdiAddr;
	}
	public String getInvitingCompanyId() {
		return invitingCompanyId;
	}
	public void setInvitingCompanyId(String invitingCompanyId) {
		this.invitingCompanyId = invitingCompanyId;
	}
	public String getInvitingCompanyName() {
		return invitingCompanyName;
	}
	public void setInvitingCompanyName(String invitingCompanyName) {
		this.invitingCompanyName = invitingCompanyName;
	}
	public String getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}
	public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	public String getNotificationEmailRecipients() {
		return notificationEmailRecipients;
	}
	public void setNotificationEmailRecipients(String notificationEmailRecipients) {
		this.notificationEmailRecipients = notificationEmailRecipients;
	}
	
	public List<InvitationExtendedRequestNeoRest> getInvitationExtendedRequest() {
		return invitationExtendedRequest;
	}
	public void setInvitationExtendedRequest(List<InvitationExtendedRequestNeoRest> invitationExtendedRequest) {
		this.invitationExtendedRequest = invitationExtendedRequest;
	}

    
}
