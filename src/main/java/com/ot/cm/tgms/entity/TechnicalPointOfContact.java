package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "technicalPointOfContact")
@XmlAccessorType(XmlAccessType.FIELD)
public class TechnicalPointOfContact {

	@XmlElement
	private String lastName;

	private ContactInformation contactInformation;

	@XmlElement
	private String firstName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ContactInformation getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "ClassPojo [lastName = " + lastName + ", contactInformation = " + contactInformation + ", firstName = "
				+ firstName + "]";
	}

}
