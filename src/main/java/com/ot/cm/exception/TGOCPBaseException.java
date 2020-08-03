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

public class TGOCPBaseException extends Exception {

	private static final long serialVersionUID = 5761380089718291576L;
	private String errorCode;
	private String[] msgParams;

	/**
	 * @param errorCode
	 */

	public TGOCPBaseException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public TGOCPBaseException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public TGOCPBaseException(String errorcode, String errorMessage, String... msgParams) {
		super(errorMessage);
		errorCode = errorcode;
		this.msgParams = msgParams;
	}

	/**
	 * @param errorcode
	 * @param msgParams
	 */
	public TGOCPBaseException(String errorcode, String... msgParams) {
		super();
		this.errorCode = errorcode;
		this.msgParams = msgParams;

	}

	/**
	 * @param throwable
	 */
	public TGOCPBaseException(Throwable throwable) {
		super(throwable);

	}

	/**
	 * @param errorCode
	 * @param throwable
	 */
	public TGOCPBaseException(String errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getMsgParams() {
		return msgParams;
	}

	public void setMsgParams(String[] msgParams) {
		this.msgParams = msgParams;
	}

}
