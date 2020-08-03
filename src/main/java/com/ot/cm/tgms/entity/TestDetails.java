package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestDetails {

	@XmlElement
	private String yourProdAddress;
	@XmlElement
	private TpProd tpProdId;
	@XmlElement
	private String yourGsId;
	@XmlElement
	private String tpGsId;
	
	public String getYourProdAddress() {
		return yourProdAddress;
	}
	public void setYourProdAddress(String yourProdAddress) {
		this.yourProdAddress = yourProdAddress;
	}
	public TpProd getTpProdId() {
		return tpProdId;
	}
	public void setTpProdId(TpProd tpProdId) {
		this.tpProdId = tpProdId;
	}
	public String getYourGsId() {
		return yourGsId;
	}
	public void setYourGsId(String yourGsId) {
		this.yourGsId = yourGsId;
	}
	public String getTpGsId() {
		return tpGsId;
	}
	public void setTpGsId(String tpGsId) {
		this.tpGsId = tpGsId;
	}
	
	
}
