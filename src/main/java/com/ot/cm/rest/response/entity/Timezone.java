package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Timezone {
	
	private String timeZoneCodeDescription;
	private String timeZoneCode;
	
	@JsonIgnore
	private String uniqueId;
	@JsonIgnore
	private String link;
	@JsonIgnore
	private String languageSubtype;
	@JsonIgnore
	private int timeZoneRawOffset;
	@JsonIgnore
	private String typeName;
	@JsonIgnore
	private int typeId;

	
	public String getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLanguageSubtype() {
		return this.languageSubtype;
	}

	public void setLanguageSubtype(String languageSubtype) {
		this.languageSubtype = languageSubtype;
	}

	public String getTimeZoneCode() {
		return this.timeZoneCode;
	}

	public void setTimeZoneCode(String timeZoneCode) {
		this.timeZoneCode = timeZoneCode;
	}

	public String getTimeZoneCodeDescription() {
		return this.timeZoneCodeDescription;
	}

	public void setTimeZoneCodeDescription(String timeZoneCodeDescription) {
		this.timeZoneCodeDescription = timeZoneCodeDescription;
	}

	public int getTimeZoneRawOffset() {
		return this.timeZoneRawOffset;
	}

	public void setTimeZoneRawOffset(int timeZoneRawOffset) {
		this.timeZoneRawOffset = timeZoneRawOffset;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
