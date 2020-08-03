package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "provisioningRequestData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProvisioningRequestData {
	private RegistrationData registrationData;
	@XmlElement
	private String setup;
	@XmlElement
	private Customer customer;

	@XmlElement
	private String tpRequestType;

	public RegistrationData getRegistrationData ()
	{
		return registrationData;
	}

	public void setRegistrationData (RegistrationData registrationData)
	{
		this.registrationData = registrationData;
	}

	public String getSetup ()
	{
		return setup;
	}

	public void setSetup (String setup)
	{
		this.setup = setup;
	}

	public Customer getCustomer ()
	{
		return customer;
	}

	public void setCustomer (Customer customer)
	{
		this.customer = customer;
	}

	public String getTpRequestType() {
		return tpRequestType;
	}

	public void setTpRequestType(String tpRequestType) {
		this.tpRequestType = tpRequestType;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [registrationData = "+registrationData+", setup = "+setup+", customer = "+customer+" , tpRequestType="+tpRequestType+"]";
	}
}
