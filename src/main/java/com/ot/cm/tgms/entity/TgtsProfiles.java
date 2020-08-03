package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tgtsProfiles")
@XmlAccessorType(XmlAccessType.FIELD)
public class TgtsProfiles {

	private Maps[] maps;

	private MessageDetails messageDetails;
	private String[] keys;
	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public Maps[] getMaps() {
		return maps;
	}

	public void setMaps(Maps[] maps) {
		this.maps = maps;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(MessageDetails messageDetails) {
		this.messageDetails = messageDetails;
	}

	@Override
	public String toString() {
		return "ClassPojo [maps = " + maps + ", messageDetails = " + messageDetails + "]";
	}
}
