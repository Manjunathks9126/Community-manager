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

public class CMApplicationException extends TGOCPBaseException {

	private static final long serialVersionUID = 5761380089718291576L;

	private HttpStatus httpStatus;
	private String errorLog;

	public CMApplicationException(String errorCode, String errorLog) {
		super(errorCode);
		this.errorLog = errorLog;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public CMApplicationException(HttpStatus httpStatus, String errorCode, String errorMessage, String errorLog) {
		super(errorCode, errorMessage);
		this.errorLog = errorLog;
		this.httpStatus = httpStatus;
	}

	public CMApplicationException(HttpStatus httpStatus, String errorCode, String errorLog, Throwable e) {
		super(errorCode, e);
		this.errorLog = errorLog;
		this.httpStatus = httpStatus;
	}

	public CMApplicationException(String errorCode, String errorLog, Throwable e) {
		super(errorCode, e);
		this.errorLog = errorLog;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

}