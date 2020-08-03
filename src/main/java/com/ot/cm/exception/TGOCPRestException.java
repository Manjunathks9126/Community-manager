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
package com.ot.cm.exception;

import org.springframework.http.HttpStatus;

public final class TGOCPRestException extends TGOCPBaseException {

	private static final long serialVersionUID = 5761380089718291576L;

	private HttpStatus httpStatus;
	private String restErrorCode;
	private String errorLog;

	public TGOCPRestException(HttpStatus httpStatus, String restErrorCode, String appErroCode, String errorMessage,
			String errorLog) {
		super(appErroCode, errorMessage);
		this.httpStatus = httpStatus;
		this.errorLog = errorLog;
		this.restErrorCode = restErrorCode;
	}
	
	public TGOCPRestException(HttpStatus httpStatus, String restErrorCode, String appErroCode, String errorMessage,
			String errorLog, String[] msgParams) {
		super(appErroCode, errorMessage, msgParams);
		this.httpStatus = httpStatus;
		this.errorLog = errorLog;
		this.restErrorCode = restErrorCode;

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

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

}