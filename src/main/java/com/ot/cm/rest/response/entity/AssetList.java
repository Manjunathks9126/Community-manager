package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "assetList")
public class AssetList {

	@JacksonXmlElementWrapper(localName = "asset", useWrapping = false)
	private Asset[] asset;

	public Asset[] getAsset() {
		return asset;
	}

	public void setAsset(Asset[] asset) {
		this.asset = asset;
	}

}
