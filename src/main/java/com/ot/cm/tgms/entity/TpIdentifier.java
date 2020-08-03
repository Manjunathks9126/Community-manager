package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tpIdentifier")
@XmlAccessorType(XmlAccessType.FIELD)
public class TpIdentifier {
    @XmlElement
    private String buId;
    @XmlElement
    private String countryCode;
    @XmlElement
    private String companyName;
    @XmlElement
    private String city;

    public String getBuId ()
    {
        return buId;
    }

    public void setBuId (String buId)
    {
        this.buId = buId;
    }

    public String getCountryCode ()
    {
        return countryCode;
    }

    public void setCountryCode (String countryCode)
    {
    	this.countryCode = countryCode;
    }

    public String getCompanyName ()
    {
        return companyName;
    }

    public void setCompanyName (String companyName)
    {
        this.companyName = companyName;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
    	this.city = city;
    }
    
	@Override
    public String toString()
    {
        return "ClassPojo [buId = "+buId+", countryCode = "+countryCode+", companyName = "+companyName+", city = "+city+"]";
    }

}