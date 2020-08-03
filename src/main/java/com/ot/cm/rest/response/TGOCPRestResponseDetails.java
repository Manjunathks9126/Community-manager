/******************************************************************************
 All rights reserved. All information contained in this software is confidential
 and proprietary to opentext. No part of this software may be
 reproduced or transmitted in any form or any means, electronic, mechanical,
 photocopying, recording or otherwise stored in any retrieval system of any
 nature without the prior written permission of opentext.
 This material is a trade secret and its confidentiality is strictly maintained.
 Use of any copyright notice does not imply unrestricted public access to this
 material.

 (c) opentext

 *******************************************************************************
 Change Log:
 Date          Name                Defect#           Description
 -------------------------------------------------------------------------------
 08/03/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response;

import org.springframework.http.HttpStatus;

/**
 * Rest response details which contains error,response details.
 * 
 * @author Dwaraka
 *
 */
public class TGOCPRestResponseDetails<SuccessType, ErrorType> {

	protected boolean success;
	private String statusMessage;
	protected HttpStatus httpStatus;
	private SuccessType responseEntity; // entity in case of success
	private ErrorType errorEntity; // entity in case of error

	public TGOCPRestResponseDetails<SuccessType, ErrorType> setSuccess(boolean s) {
		this.success = s;
		return this;
	}

	public TGOCPRestResponseDetails<SuccessType, ErrorType> setHttpStatus(HttpStatus h) {
		this.httpStatus = h;
		return this;
	}

	public void setResponseEntity(SuccessType responseEntity) {
		this.responseEntity = responseEntity;
	}

	public void setErrorEntity(ErrorType errorEntity) {
		this.errorEntity = errorEntity;
	}

	public boolean isSuccess() {
		return success;
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

	public SuccessType getResponseEntity() {
		return responseEntity;
	}

	public ErrorType getErrorEntity() {
		return errorEntity;
	}

}
