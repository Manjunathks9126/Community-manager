package com.ot.cm.exception;

import org.springframework.http.HttpStatus;

public class TGOCPOnboardingRestClientException extends RuntimeException {
	private static final long serialVersionUID = 3631342468701836129L;
	
	private HttpStatus statusCode;
	private String statusMessage;
	private String errorDetails;
	private String errorCode;
	
	public TGOCPOnboardingRestClientException(HttpStatus statusCode, String statusMessage, String errorDetails, String errorCode) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.errorDetails = errorDetails;
		this.errorCode = errorCode;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	

}
