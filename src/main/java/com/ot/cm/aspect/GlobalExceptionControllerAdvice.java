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
package com.ot.cm.aspect;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * Advice to handle different kind of exceptions which will be thrown across
 * application.
 * 
 */

@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionControllerAdvice.class);
	String lineSeparator = System.getProperty("line.separator");

	@Autowired
	LocalizationMessages localizationMessages;

	/**
	 * This method handles all the exceptions of type CMApplicationException
	 * which will be thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(CMApplicationException.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(CMApplicationException cause,
			HttpServletRequest httpServletRequest) {
		logger.error(cause.getErrorLog(), cause);
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		return buildResponseEntity(cause.getErrorCode(), null, null,
				localizationMessages.getMessage(cause.getErrorCode(), userInfo != null ? userInfo.getLocale() : null),
				cause.getHttpStatus(), stackTraceToString(cause));
	}
	
	
	/**
	 * This method handles all the exceptions of type TGOCPOnboardingRestClientException
	 * which will be thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(TGOCPOnboardingRestClientException.class)
	public ErrorResponse exception(TGOCPOnboardingRestClientException onboardingException,
			HttpServletRequest httpServletRequest) {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
				
		return buildOnboardingResponseEntity(onboardingException, userInfo);
	}

	private ErrorResponse buildOnboardingResponseEntity(TGOCPOnboardingRestClientException onboardingException, UserInfo userInfo) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatusCode(onboardingException.getStatusCode());
		errorResponse.setStatusMessage(onboardingException.getStatusMessage());
		errorResponse.setErrorCode(onboardingException.getErrorCode());
		errorResponse.setErrorDetails(onboardingException.getErrorDetails());
		errorResponse.setResponseStatus(ApplicationConstants.RESPONSE_STATUS_ERROR);
		errorResponse.setResponseMessage(localizationMessages.getMessage("EDI_SEARCH_FAIL", HttpStatus.INTERNAL_SERVER_ERROR.value(), userInfo != null ? userInfo.getLocale() : null));
		if(onboardingException.getStatusCode() != null && onboardingException.getStatusCode().value() < 500) {
			errorResponse.setResponseStatus(ApplicationConstants.RESPONSE_STATUS_INFO);
			errorResponse.setResponseMessage(localizationMessages.getMessage("EDI_SEARCH_FAIL", HttpStatus.NOT_FOUND.value(), userInfo != null ? userInfo.getLocale() : null));
		}


		return errorResponse;
	}


	/**
	 * This method handles all the exceptions of type TGOCPRestException which
	 * will be thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(TGOCPRestException.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(TGOCPRestException cause,
			HttpServletRequest httpServletRequest) {
		logger.error(cause.getErrorLog(), cause);
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		String errorText = localizationMessages.getMessage(cause.getErrorCode(), cause.getMsgParams(),
				userInfo != null ? userInfo.getLocale() : null);
		return buildResponseEntity(cause.getErrorCode(), cause.getRestErrorCode(), cause.getMessage(), errorText,
				cause.getHttpStatus(), stackTraceToString(cause));
	}

	/**
	 * This method handles all the exceptions of type JSON Parser which will be
	 * thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(HttpMessageNotReadableException cause,
			HttpServletRequest httpServletRequest) {
		logger.error(cause.getLocalizedMessage(), cause);
		return buildResponseEntity(ErrorCodes.INVALID_INPUT, null, null,
				/* TODO --user locale msg */"Non-readble/Invalid json data received.", HttpStatus.BAD_REQUEST,
				stackTraceToString(cause));
	}

	/**
	 * This method handles all the exceptions of type IllegalArgumentException
	 * which will be thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(IllegalArgumentException cause,
			HttpServletRequest httpServletRequest) {
		logger.error(cause.getLocalizedMessage(), cause);
		return buildResponseEntity(ErrorCodes.INVALID_INPUT, null, null, cause.getMessage(),
				HttpStatus.EXPECTATION_FAILED, stackTraceToString(cause));
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(MissingServletRequestParameterException e) {
		return buildResponseEntity(ErrorCodes.UNACCEPTABLE_DATA_RECEIVED,  HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.getReasonPhrase(),e.getMessage(),
				HttpStatus.BAD_REQUEST, stackTraceToString(e));
	}

	/**
	 * This method handles all the exceptions of type Exception which will be
	 * thrown across the application.
	 * 
	 * @param cause
	 * @param httpServletRequest
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> exception(Exception cause) {
		logger.error(cause.getMessage(), cause);

		return buildResponseEntity(ErrorCodes.INTERNAL_SERVER_ERROR, null, null,
				localizationMessages.getMessage(ErrorCodes.INTERNAL_SERVER_ERROR, null),
				HttpStatus.INTERNAL_SERVER_ERROR, stackTraceToString(cause));
	}

	private <T> ResponseEntity<TGOCPRestResponse<T, ErrorResponse>> buildResponseEntity(String appErrorCode,
			String restErrorCode, String restErrorDetails, String userMessage, HttpStatus status,
			String errorStackTrace) {
		TGOCPRestResponseDetails<T, ErrorResponse> tgoResponseDetails = new TGOCPRestResponseDetails<>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setAppErrorCode(appErrorCode);
		errorResponse.setRestErrorCode(restErrorCode);
		errorResponse.setRestErrorDetails(restErrorDetails);
		errorResponse.setUserMessage(userMessage);

		tgoResponseDetails.setSuccess(false).setHttpStatus(status).setErrorEntity(errorResponse);

		return new ResponseEntity<>(new TGOCPRestResponse<T, ErrorResponse>(tgoResponseDetails),
				tgoResponseDetails.getHttpStatus());
	}

	private String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.getMessage());
		sb.append(lineSeparator);
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(lineSeparator);
		}
		return sb.toString();
	}
}