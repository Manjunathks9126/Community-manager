package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "companyAddress")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyAddress {

	@XmlElement
	private String postalCode;

	@XmlElement
	private String website;

	@XmlElement
	private String state;

	@XmlElement
	private String countryCode;

	@XmlElement
	private String addressLine2;

	@XmlElement
	private String addressLine1;

	@XmlElement
	private String city;

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "ClassPojo [postalCode = " + postalCode + ", website = " + website + ", state = " + state
				+ ", countryCode = " + countryCode + ", addressLine2 = " + addressLine2 + ", addressLine1 = "
				+ addressLine1 + ", city = " + city + "]";
	}

}
