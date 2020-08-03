package com.ot.cm.tgms.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "businessUnit")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessUnit {
	@XmlElement
	private boolean allowDuplicates = true;;

	private CompanyAddress companyAddress;

	@XmlElement
	private String participationType;

	@XmlElement
	private String companyId;

	@XmlElement
	private String companyName;

	@XmlElement
	private String customCompanyId;

	@XmlElement
	private String invitingCommunityId;

	private List<ExternalReferenceType> externalref;

	public List<ExternalReferenceType> getExternalref() {
		return externalref;
	}

	public void setExternalref(List<ExternalReferenceType> externalref) {
		this.externalref = externalref;
	}

	public boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	public String getCustomCompanyId() {
		return customCompanyId;
	}

	public void setCustomCompanyId(String customCompanyId) {
		this.customCompanyId = customCompanyId;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	@XmlElement
	private String displayName;

	@XmlElement
	private String buId;

	public CompanyAddress getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(CompanyAddress companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getParticipationType() {
		return participationType;
	}

	public void setParticipationType(String participationType) {
		this.participationType = participationType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getInvitingCommunityId() {
		return invitingCommunityId;
	}

	public void setInvitingCommunityId(String invitingCommunityId) {
		this.invitingCommunityId = invitingCommunityId;
	}

	@Override
	public String toString() {
		return "BusinessUnit [allowDuplicates=" + allowDuplicates + ", companyAddress=" + companyAddress
				+ ", participationType=" + participationType + ", companyId=" + companyId + ", companyName="
				+ companyName + ", customCompanyId=" + customCompanyId + ", invitingCommunityId=" + invitingCommunityId
				+ ", externalref=" + externalref + ", displayName=" + displayName + ", buId=" + buId + "]";
	}

}
