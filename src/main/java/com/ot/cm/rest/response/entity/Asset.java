package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Asset {

	@JacksonXmlProperty(localName = "assetId")
	private String assetId;
	@JacksonXmlProperty(localName = "link")
	private String link;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
