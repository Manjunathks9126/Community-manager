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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.services.bsapi.rs.v2.entity.ErrorResponse;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.rest.response.PartnerRESTReponse;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.JacksonWrapper;

@Component
public class TGOCPOnboardingRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(TGOCPOnboardingRestTemplate.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JacksonWrapper mapper;

	public TGOCPOnboardingRestTemplate() {
	}


	public <T, T2> T exchange(RequestPayload<T> payload, T2 errorEntity, Object... uriVariables)
			throws TGOCPOnboardingRestClientException {

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

		} catch (TGOCPOnboardingRestClientException restException) {
			throw new TGOCPOnboardingRestClientException(restException.getStatusCode(), restException.getStatusMessage(), restException.getErrorDetails(), restException.getErrorCode());
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
					throw new TGOCPOnboardingRestClientException(status,clienthttpresponse.getStatusText(),str,str);
				}
				PartnerRESTReponse partnerRESTReponse = readPartnerRESTResponse(response);
				throw new TGOCPOnboardingRestClientException(status,
						partnerRESTReponse.getStatusMessage(), partnerRESTReponse.getErrorDetails(), partnerRESTReponse.getErrorCode());
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



	
}
