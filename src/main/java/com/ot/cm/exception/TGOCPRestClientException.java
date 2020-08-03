package com.ot.cm.exception;

import org.springframework.http.HttpStatus;

public class TGOCPRestClientException extends RuntimeException {
	private static final long serialVersionUID = -6653242657182904748L;
	private String statusMessage;

	private HttpStatus httpStatus;
	private String restErrorCode;
	private String errorDetails;

	public TGOCPRestClientException(HttpStatus httpStatus, String restErrorCode, String statusMessage,
			String errorDetails) {
		super(errorDetails);
		this.restErrorCode = restErrorCode;
		this.httpStatus = httpStatus;
		this.statusMessage = statusMessage;
		this.errorDetails = errorDetails;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getRestErrorCode() {
		return restErrorCode;
	}

	public void setRestErrorCode(String restErrorCode) {
		this.restErrorCode = restErrorCode;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

}
