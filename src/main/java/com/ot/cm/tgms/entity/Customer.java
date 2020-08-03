package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {

	private ListAddress[] listAddress;

	private TradingAddresses tradingAddresses;

	public TradingAddresses getTradingAddresses() {
		return tradingAddresses;
	}

	public void setTradingAddresses(TradingAddresses tradingAddresses) {
		this.tradingAddresses = tradingAddresses;
	}

	public ListAddress[] getListAddress ()
	{
		return listAddress;
	}

	public void setListAddress (ListAddress[] listAddress)
	{
		this.listAddress = listAddress;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [listAddress = "+listAddress+", tradingAddresses = "+tradingAddresses+"]";
	}

}
