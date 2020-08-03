package com.ot.cm.constants;

public enum BulkImportType {

	INVITE("BULK_INVITE_AO"), CUSTOM_FIELD("BULK_CUSTOM_FIELDS_IMPORT");

	private String value;

	private BulkImportType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
