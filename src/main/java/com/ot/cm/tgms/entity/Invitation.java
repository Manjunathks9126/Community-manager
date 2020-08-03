package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invitation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invitation {

	@XmlElement
	private String invitationCode;

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	@Override
	public String toString() {
		return "ClassPojo [invitationCode = " + invitationCode + "]";
	}
}
