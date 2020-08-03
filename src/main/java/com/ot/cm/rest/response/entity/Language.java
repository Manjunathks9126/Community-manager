package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Language {
	@JsonIgnore
	private String typeName;
	@JsonIgnore
	private String languageSubtype;

	private String languageCode;
	@JsonIgnore
	private String link;

	private String languageDescription;
	@JsonIgnore
	private String typeId;
	@JsonIgnore
	private String uniqueId;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getLanguageSubtype() {
		return languageSubtype;
	}

	public void setLanguageSubtype(String languageSubtype) {
		this.languageSubtype = languageSubtype;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLanguageDescription() {
		return languageDescription;
	}

	public void setLanguageDescription(String languageDescription) {
		this.languageDescription = languageDescription;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Language [typeName=");
		builder.append(typeName);
		builder.append(", languageSubtype=");
		builder.append(languageSubtype);
		builder.append(", languageCode=");
		builder.append(languageCode);
		builder.append(", link=");
		builder.append(link);
		builder.append(", languageDescription=");
		builder.append(languageDescription);
		builder.append(", typeId=");
		builder.append(typeId);
		builder.append(", uniqueId=");
		builder.append(uniqueId);
		builder.append("]");
		return builder.toString();
	}

	
}
