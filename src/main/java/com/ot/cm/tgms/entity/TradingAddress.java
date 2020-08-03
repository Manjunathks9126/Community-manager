package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tradingAddress")
@XmlAccessorType(XmlAccessType.FIELD)
public class TradingAddress {

	private Testtp testtp;
	@XmlElement
	private String ediStadard;
	@XmlElement
	private String vanMailboxId;
	@XmlElement
	private String connectivityType;
	@XmlElement
	private String vanProvider;

	private Prodtp prodtp;

	public Testtp getTesttp ()
	{
		return testtp;
	}

	public void setTesttp (Testtp testtp)
	{
		this.testtp = testtp;
	}

	public String getEdiStadard ()
	{
		return ediStadard;
	}

	public void setEdiStadard (String ediStadard)
	{
		this.ediStadard = ediStadard;
	}

	public String getVanMailboxId ()
	{
		return vanMailboxId;
	}

	public void setVanMailboxId (String vanMailboxId)
	{
		this.vanMailboxId = vanMailboxId;
	}

	public String getConnectivityType ()
	{
		return connectivityType;
	}

	public void setConnectivityType (String connectivityType)
	{
		this.connectivityType = connectivityType;
	}

	public String getVanProvider ()
	{
		return vanProvider;
	}

	public void setVanProvider (String vanProvider)
	{
		this.vanProvider = vanProvider;
	}

	public Prodtp getProdtp ()
	{
		return prodtp;
	}

	public void setProdtp (Prodtp prodtp)
	{
		this.prodtp = prodtp;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [testtp = "+testtp+", ediStadard = "+ediStadard+", vanMailboxId = "+vanMailboxId+", connectivityType = "+connectivityType+", vanProvider = "+vanProvider+", prodtp = "+prodtp+"]";
	}
}
