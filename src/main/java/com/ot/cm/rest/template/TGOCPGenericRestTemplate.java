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
package com.ot.cm.rest.template;

import java.io.IOException;
import java.net.URI;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.services.bsapi.rs.v2.entity.ErrorResponse;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.PartnerRESTReponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.JacksonWrapper;

@Component
public class TGOCPGenericRestTemplate implements BaseRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(TGOCPGenericRestTemplate.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JacksonWrapper mapper;

	public TGOCPGenericRestTemplate() {
	}

	@Override
	public <T1, T2> TGOCPRestResponse<T1, T2> exchange(String endpoint, HttpMethod httpMethod,
			HttpEntity<?> requestEntity, Class<T1> responseType, Object... uriVariables) throws TGOCPRestException {

		TGOCPRestResponseDetails<T1, T2> responseDetails = new TGOCPRestResponseDetails<T1, T2>();
		TGOResponseErrorHandler<T2> errorHandler = new TGOResponseErrorHandler<T2>();
		restTemplate.setErrorHandler(errorHandler);
		ResponseEntity<String> response = null;

		try {
			response = restTemplate.exchange(endpoint, httpMethod, requestEntity, String.class, uriVariables);
			responseDetails.setSuccess(true).setHttpStatus(response.getStatusCode())
					.setResponseEntity(mapper.readValue(response.getBody(), responseType));

		} catch (TGOCPRestClientException restException) {
			String appErroCode = deriveApplicationErrorCode(null, restException.getRestErrorCode(),
					restException.getHttpStatus());
			throw new TGOCPRestException(restException.getHttpStatus(), restException.getRestErrorCode(), appErroCode,
					restException.getErrorDetails(), "Error while getting details for : " + endpoint + " : "
							+ uriVariablesToString(uriVariables) + restException.getErrorDetails());
		} catch (Exception exception) {
			throw exception;
		}
		return new TGOCPRestResponse<T1, T2>(responseDetails);
	}

	@Override
	public <T1, T2> TGOCPRestResponse<T1, T2> exchange(String endpoint, HttpMethod httpMethod,
			HttpEntity<?> requestEntity, Class<T1> responseType, RestActionContext actionContext, String[] msgParams,
			Object... uriVariables) throws TGOCPRestException {

		TGOCPRestResponseDetails<T1, T2> responseDetails = new TGOCPRestResponseDetails<T1, T2>();
		TGOResponseErrorHandler<T2> errorHandler = new TGOResponseErrorHandler<T2>();
		restTemplate.setErrorHandler(errorHandler);
		ResponseEntity<String> response = null;

		try {
			response = restTemplate.exchange(endpoint, httpMethod, requestEntity, String.class, uriVariables);
			responseDetails.setSuccess(true).setHttpStatus(response.getStatusCode())
					.setResponseEntity(mapper.readValue(response.getBody(), responseType));

		} catch (TGOCPRestClientException restException) {
			String appErroCode = deriveApplicationErrorCode(actionContext, restException.getRestErrorCode(),
					restException.getHttpStatus());
			throw new TGOCPRestException(restException.getHttpStatus(), restException.getRestErrorCode(), appErroCode,
					restException.getErrorDetails(), "Error while getting details for : " + endpoint + " : "
							+ uriVariablesToString(uriVariables) + restException.getErrorDetails(),
					msgParams);
		} catch (Exception exception) {
			throw exception;
		}
		return new TGOCPRestResponse<T1, T2>(responseDetails);
	}

	@Override
	public <T, T2> T exchange(RequestPayload<T> payload, T2 errorEntity, Object... uriVariables)
			throws TGOCPRestException {

		if (logger.isDebugEnabled()) {
			logger.debug("Invoking Rest call :'" + payload.getEndPoint(), "with param" + uriVariables);
		}
		TGOResponseErrorHandler<ErrorResponse> errorHandler = new TGOResponseErrorHandler<ErrorResponse>();
		restTemplate.setErrorHandler(errorHandler);

		ResponseEntity<T> restResponse = null;
		try {
			if (null != payload.getQueryParams()) {
				UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(payload.getEndPoint().toString());
				payload.getQueryParams().forEach((k, v) -> {
					urlBuilder.queryParam(k, CommonUtil.encodeParam(v));
				});
				URI locationUri = urlBuilder.build(true).toUri();
				restResponse = restTemplate.exchange(locationUri, payload.getHttpMethod(), payload.getRequestType(),
						payload.getResponseType());
			} else {
				restResponse = restTemplate.exchange(payload.getEndPoint(), payload.getHttpMethod(),
						payload.getRequestType(), payload.getResponseType(), uriVariables);
			}

		} catch (TGOCPRestClientException restException) {
			String appErroCode = deriveApplicationErrorCode(payload.getActionContext(),
					restException.getRestErrorCode(), restException.getHttpStatus());
			throw new TGOCPRestException(restException.getHttpStatus(), restException.getRestErrorCode(), appErroCode,
					restException.getErrorDetails(),
					"Error while getting details for : " + payload.getEndPoint() + " : "
							+ uriVariablesToString(uriVariables) + restException.getErrorDetails(),
					payload.getMsgparams());
		}
		return restResponse.getBody();
	}

	private class TGOResponseErrorHandler<T> implements ResponseErrorHandler {

		private HttpStatus status;

		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			HttpStatus statusCode = response.getStatusCode();
			return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR
					|| statusCode.series() == HttpStatus.Series.SERVER_ERROR);
		}

		@Override
		public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {

			this.status = clienthttpresponse.getStatusCode();
			if (null != clienthttpresponse.getBody()) {
				JSONObject response = mapper.readValue(clienthttpresponse.getBody(), JSONObject.class);
				if(null == response){
					String str = mapper.readValue(clienthttpresponse.getBody(), String.class);
					throw new TGOCPRestClientException(this.status,clienthttpresponse.getStatusText(),str,str);
				}
				PartnerRESTReponse partnerRESTReponse = readPartnerRESTResponse(response);
				throw new TGOCPRestClientException(this.status, partnerRESTReponse.getErrorCode(),
						partnerRESTReponse.getStatusMessage(), partnerRESTReponse.getErrorDetails());
			}

		}

		private PartnerRESTReponse readPartnerRESTResponse(JSONObject clientErrorResponse) {
			PartnerRESTReponse partnerRESTReponse = new PartnerRESTReponse();
			partnerRESTReponse.setStatus(status);
			partnerRESTReponse.setErrorCode((String) clientErrorResponse.get("errorCode"));
			if ((String) clientErrorResponse.get("errorDetails") != null) {
				partnerRESTReponse.setErrorDetails((String) clientErrorResponse.get("errorDetails"));
			} else if ((String) clientErrorResponse.get("errorDescription") != null) {
				partnerRESTReponse.setErrorDetails((String) clientErrorResponse.get("errorDescription"));
			}
			partnerRESTReponse.setStatusMessage((String) clientErrorResponse.get("statusMessage"));
			return partnerRESTReponse;
		}

	}

	private String uriVariablesToString(Object... uriVariables) {
		StringBuilder result = new StringBuilder();

		for (Object uriVariable : uriVariables) {
			result.append(String.valueOf(uriVariable) + ", ");
		}

		return result.toString();
	}

	private String deriveApplicationErrorCode(RestActionContext actionContext, String restErrorCode,
			HttpStatus httpStatus) {
		StringBuilder appErroCode = new StringBuilder();
		if (actionContext != null) {
			appErroCode.append(actionContext.getContext() + "_");
			appErroCode.append(httpStatus.value());
		} else {
			switch (httpStatus) {
			case NOT_FOUND:
				appErroCode.append(ErrorCodes.ENTITY_NOT_EXISTS_IN_IM);
				break;
			case NOT_ACCEPTABLE:
				appErroCode.append(ErrorCodes.UNACCEPTABLE_DATA_RECEIVED);
				break;
			case BAD_REQUEST:
				appErroCode.append(ErrorCodes.INVALID_INPUT);
				break;
			default:
				appErroCode.append(ErrorCodes.INTERNAL_SERVER_ERROR);
			}
		}
		return appErroCode.toString();
	}
}
