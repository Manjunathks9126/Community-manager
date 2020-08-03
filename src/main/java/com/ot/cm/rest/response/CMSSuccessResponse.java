package com.ot.cm.rest.response;

public class CMSSuccessResponse {

	private String targetResourceRefId;
	private String statusCode;
	private String statusMessage;

	public String getTargetResourceRefId() {
		return targetResourceRefId;
	}

	public CMSSuccessResponse() {
	}

	public CMSSuccessResponse(String statusCode, String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

	public void setTargetResourceRefId(String targetResourceRefId) {
		this.targetResourceRefId = targetResourceRefId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
