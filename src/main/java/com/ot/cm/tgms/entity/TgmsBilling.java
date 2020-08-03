package com.ot.cm.tgms.entity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tgmsBilling")
@XmlAccessorType(XmlAccessType.FIELD)
public class TgmsBilling {


    private SplitCharges splitCharges;

    @XmlElement
    private String allCharges;

    public SplitCharges getSplitCharges ()
    {
        return splitCharges;
    }

    public void setSplitCharges (SplitCharges splitCharges)
    {
        this.splitCharges = splitCharges;
    }

    public String getAllCharges ()
    {
        return allCharges;
    }

    public void setAllCharges (String allCharges)
    {
        this.allCharges = allCharges;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [splitCharges = "+splitCharges+", allCharges = "+allCharges+"]";
    }
}
