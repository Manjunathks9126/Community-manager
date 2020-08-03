package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	@XmlElement
	private String lastName;

	@XmlElement
	private String isRequiredMarketingMaterials;

	@XmlElement
	private String isDayLightSavingsTimeObserved;

	@XmlElement
	private String userLoginPassword;

	private ContactInformation contactInformation;

	private Address address;

	@XmlElement
	private String firstName;

	@XmlElement
	private String userLogin;

	@XmlElement
	private String preferredTimezone;

	@XmlElement
	private String preferredDateFormat;

	@XmlElement
	private String preferredLanguage;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIsRequiredMarketingMaterials() {
		return isRequiredMarketingMaterials;
	}

	public void setIsRequiredMarketingMaterials(String isRequiredMarketingMaterials) {
		this.isRequiredMarketingMaterials = isRequiredMarketingMaterials;
	}

	public String getIsDayLightSavingsTimeObserved() {
		return isDayLightSavingsTimeObserved;
	}

	public void setIsDayLightSavingsTimeObserved(String isDayLightSavingsTimeObserved) {
		this.isDayLightSavingsTimeObserved = isDayLightSavingsTimeObserved;
	}

	public String getUserLoginPassword() {
		return userLoginPassword;
	}

	public void setUserLoginPassword(String userLoginPassword) {
		this.userLoginPassword = userLoginPassword;
	}

	public ContactInformation getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getPreferredTimezone() {
		return preferredTimezone;
	}

	public void setPreferredTimezone(String preferredTimezone) {
		this.preferredTimezone = preferredTimezone;
	}

	public String getPreferredDateFormat() {
		return preferredDateFormat;
	}

	public void setPreferredDateFormat(String preferredDateFormat) {
		this.preferredDateFormat = preferredDateFormat;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	@Override
	public String toString() {
		return "ClassPojo [lastName = " + lastName + ", isRequiredMarketingMaterials = " + isRequiredMarketingMaterials
				+ ", isDayLightSavingsTimeObserved = " + isDayLightSavingsTimeObserved + ", userLoginPassword = "
				+ userLoginPassword + ", contactInformation = " + contactInformation + ", address = " + address
				+ ", firstName = " + firstName + ", userLogin = " + userLogin + ", preferredTimezone = "
				+ preferredTimezone + ", preferredDateFormat = " + preferredDateFormat + ", preferredLanguage = "
				+ preferredLanguage + "]";
	}

}
