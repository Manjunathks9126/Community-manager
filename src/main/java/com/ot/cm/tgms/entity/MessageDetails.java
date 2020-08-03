package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "messageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageDetails {

	@XmlElement
	private String segmentTerminator;

	@XmlElement
	private String customerTestISAindicator;

	@XmlElement
	private String functionalAck;

	@XmlElement
	private String elementSeparator;

	@XmlElement
	private String subelementTerminator;

	@XmlElement
	private String tpTestISAindicator;

	public String getSegmentTerminator() {
		return segmentTerminator;
	}

	public void setSegmentTerminator(String segmentTerminator) {
		this.segmentTerminator = segmentTerminator;
	}

	public String getCustomerTestISAindicator() {
		return customerTestISAindicator;
	}

	public void setCustomerTestISAindicator(String customerTestISAindicator) {
		this.customerTestISAindicator = customerTestISAindicator;
	}

	public String getFunctionalAck() {
		return functionalAck;
	}

	public void setFunctionalAck(String functionalAck) {
		this.functionalAck = functionalAck;
	}

	public String getElementSeparator() {
		return elementSeparator;
	}

	public void setElementSeparator(String elementSeparator) {
		this.elementSeparator = elementSeparator;
	}

	public String getSubelementTerminator() {
		return subelementTerminator;
	}

	public void setSubelementTerminator(String subelementTerminator) {
		this.subelementTerminator = subelementTerminator;
	}

	public String getTpTestISAindicator() {
		return tpTestISAindicator;
	}

	public void setTpTestISAindicator(String tpTestISAindicator) {
		this.tpTestISAindicator = tpTestISAindicator;
	}

	@Override
	public String toString() {
		return "ClassPojo [segmentTerminator = " + segmentTerminator + ", customerTestISAindicator = "
				+ customerTestISAindicator + ", functionalAck = " + functionalAck + ", elementSeparator = "
				+ elementSeparator + ", subelementTerminator = " + subelementTerminator + ", tpTestISAindicator = "
				+ tpTestISAindicator + "]";
	}

}
