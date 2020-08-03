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
 02/16/2017	   Madan								Defined separate Rest client for EDI related calls
 ******************************************************************************/
package com.ot.cm.rest.client;

import java.io.IOException;
import java.net.URI;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.services.bsapi.rs.v1.entity.RetrieveVANProviderResponse;
import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.session.annotation.Loggable;

/**
 * The {@code EDIAddressRestClient} class interface with CMS/CMD/RARest REST
 * APIs to get EDI information.
 * 
 */
@Component
public class EDIAddressRestClient extends BaseRestClient {

	/**
	 * Method to fetch edi address info from cms
	 * 
	 * @param Edi
	 *            Address
	 * @return
	 * @throws TGOCPRestException
	 */

	@Autowired
	private RestTemplate restTemplateNative;

	@Loggable
	public TradingAddressDetailsType getEdiInfo(String ediAddress) throws TGOCPRestException {

		String url = globalProperties.getRest().getCmsURL() + "/v2/tradingaddresses/";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).path(CommonUtil.encodeParam(ediAddress));
		UriComponents components = builder.build(true);
		URI uri = components.toUri();

		ResponseEntity<TradingAddressDetailsType> ediInfo = restTemplateNative.exchange(uri, HttpMethod.GET,
				new HttpEntity<String>("headers", basicHeaders()), TradingAddressDetailsType.class);

		return ediInfo.getBody();
	}

	@Loggable
	public RetrieveVANProviderResponse getVANProvidersList()
			throws TGOCPRestException, JsonGenerationException, JsonMappingException, IOException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/vanproviderservice");
		TGOCPRestResponse<RetrieveVANProviderResponse, ErrorResponse> vanProvidersList = restTemplate.exchange(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				RetrieveVANProviderResponse.class, RestActionContext.GET_BU_VANPROVIDERS, null);

		return vanProvidersList.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String getEDICount(String companyID, String searchQuery) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/{companyID}/tradingaddresses?");
		endPoint.append(searchQuery);
		TGOCPRestResponse<String, ErrorResponse> response = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
				new HttpEntity<String>("headers", basicHeaders()), String.class, RestActionContext.GET_EDI_COUNT, null,
				companyID);

		return response.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public TradingAddressDetailsType[] getEDIAddresses(String companyID, String serarchParameters)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/{companyID}/tradingaddresses");
		endPoint.append(serarchParameters);
		TGOCPRestResponse<TradingAddressDetailsType[], ErrorResponse> ediInfo = restTemplate.exchange(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				TradingAddressDetailsType[].class, RestActionContext.GET_EDI_ADDRESS, null, companyID);
		return ediInfo.getResponseDetails().getResponseEntity();
	}

}
