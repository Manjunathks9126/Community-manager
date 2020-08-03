package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "splitCharges")
@XmlAccessorType(XmlAccessType.FIELD)
public class SplitCharges {

    @XmlElement
    private String receiverMBStorage;

    @XmlElement
    private String sending;

    @XmlElement
    private String receiving;

    public String getReceiverMBStorage ()
    {
        return receiverMBStorage;
    }

    public void setReceiverMBStorage (String receiverMBStorage)
    {
        this.receiverMBStorage = receiverMBStorage;
    }

    public String getSending ()
    {
        return sending;
    }

    public void setSending (String sending)
    {
        this.sending = sending;
    }

    public String getReceiving ()
    {
        return receiving;
    }

    public void setReceiving (String receiving)
    {
        this.receiving = receiving;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [receiverMBStorage = "+receiverMBStorage+", sending = "+sending+", receiving = "+receiving+"]";
    }
}
