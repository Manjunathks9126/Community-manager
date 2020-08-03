package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vanDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class VanDetails {
	@XmlElement
	private String connectivityType;
	
	@XmlElement
	private String vanProviders;

	public String getConnectivityType() {
		return connectivityType;
	}

	public void setConnectivityType(String connectivityType) {
		this.connectivityType = connectivityType;
	}

	public String getVanProviders() {
		return vanProviders;
	}

	public void setVanProviders(String vanProviders) {
		this.vanProviders = vanProviders;
	}
	
	
}
