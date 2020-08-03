package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listAddress")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListAddress {

    @XmlElement
    private String ediAdress;

    public String getEdiAdress ()
    {
        return ediAdress;
    }

    public void setEdiAdress (String ediAdress)
    {
        this.ediAdress = ediAdress;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ediAdress = "+ediAdress+"]";
    }
}
