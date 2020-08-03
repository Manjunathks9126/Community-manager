package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tpProdId")
@XmlAccessorType(XmlAccessType.FIELD)
public class TpProd {
	
	@XmlElement
    private String address;
    @XmlElement
    private String qualifier;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	@Override
	public String toString() {
		return "TpProd [address=" + address + ", qualifier=" + qualifier + "]";
	}
	
}
