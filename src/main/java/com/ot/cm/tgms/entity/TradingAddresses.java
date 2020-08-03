package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tradingAddresses")
@XmlAccessorType(XmlAccessType.FIELD)
public class TradingAddresses {

	private Test test;

	private Prod prod;

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Prod getProd() {
		return prod;
	}

	public void setProd(Prod prod) {
		this.prod = prod;
	}

	@Override
	public String toString() {
		return "ClassPojo [test = " + test + ", prod = " + prod + "]";
	}

}
