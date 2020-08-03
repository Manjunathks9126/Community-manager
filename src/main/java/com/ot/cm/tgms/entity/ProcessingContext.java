package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "processingContext")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessingContext {

	@XmlElement
	private String invitationCode;

	@XmlElement
	private TpIdentifier tpIdentifier;

	@XmlElement
	private InitiatorIdentifier initiatorIdentifier;

	@XmlElement
	private String action;

	@XmlElement
	private String invitationSource;

	public String getInvitationSource() {
		return invitationSource;
	}

	public void setInvitationSource(String invitationSource) {
		this.invitationSource = invitationSource;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public TpIdentifier getTpIdentifier() {
		return tpIdentifier;
	}

	public void setTpIdentifier(TpIdentifier tpIdentifier) {
		this.tpIdentifier = tpIdentifier;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public InitiatorIdentifier getInitiatorIdentifier() {
		return initiatorIdentifier;
	}

	public void setInitiatorIdentifier(InitiatorIdentifier initiatorIdentifier) {
		this.initiatorIdentifier = initiatorIdentifier;
	}

	@Override
	public String toString() {
		return "ProcessingContext [invitationCode=" + invitationCode + ", tpIdentifier=" + tpIdentifier
				+ ", initiatorIdentifier=" + initiatorIdentifier + ", action=" + action + ", invitationSource="
				+ invitationSource + "]";
	}

}
