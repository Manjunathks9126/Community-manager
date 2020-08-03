package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "prod")
@XmlAccessorType(XmlAccessType.FIELD)
public class Prodtp {

    @XmlElement
    private String address;
    @XmlElement
    private String qualifier;
    @XmlElement
    private String gsId;

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getQualifier ()
    {
        return qualifier;
    }

    public void setQualifier (String qualifier)
    {
        this.qualifier = qualifier;
    }

    public String getGsId ()
    {
        return gsId;
    }

    public void setGsId (String gsId)
    {
        this.gsId = gsId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [address = "+address+", qualifier = "+qualifier+", gsId = "+gsId+"]";
    }

}
