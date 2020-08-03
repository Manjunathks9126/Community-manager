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
 Aug 17, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v1.entity.RetrieveVANProviderResponse;
import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.imapi.common.entities.ImRestSolution;
import com.gxs.services.imapi.common.entities.ImRestSolutionList;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.response.entity.Language;
import com.ot.cm.rest.response.entity.Timezone;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.rest.template.TGOCPOnboardingRestTemplate;
import com.ot.session.annotation.Loggable;


@Component
public class OnboardingDataRestClient extends BaseRestClient {
	
	@Autowired
	private TGOCPOnboardingRestTemplate onboardingRestTemplate;
	
	@Loggable
	public ArrayList<ImRestSolution> getComputeZone(String buId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmdURL())
				.append("/rest/v1/solutions/attrs?buid=");
		endPoint.append(buId);
		
		TGOCPRestResponse<ImRestSolutionList, ErrorResponse> details = restTemplate.exchange(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()),
				ImRestSolutionList.class);
		
		return details.getResponseDetails().getResponseEntity().getSolutionList();
		
	}
	
	
	@Loggable
	public TradingAddressDetailsType getEdiInfo(String ediAddress) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("TRADING_ADDRESSES", "V2");
		
		RequestPayload<TradingAddressDetailsType> payload = new RequestPayload<>(endPoint, null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<TradingAddressDetailsType>() {
				});
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class,ediAddress);
	}
	
	
	@Loggable
	public BusinessUnitDetailsType getBusinessUnitDetailsType(String companyId) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("BUSINESS_UNIT_DETAILS", "V3");
		RequestPayload<BusinessUnitDetailsType> payload = new RequestPayload<>(endPoint, null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<BusinessUnitDetailsType>() {
				});
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class, companyId);
	}
	
	
	@Loggable
	public TradingAddressDetailsType[] getEDIAddresses(String companyID, String searchParams)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("TRADING_ADDRESSES_LIST", "V2");
		RequestPayload<TradingAddressDetailsType[]> payload = new RequestPayload<>(endPoint.toString(), null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<TradingAddressDetailsType[]>() {
				});
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class,companyID,searchParams);
	}
	
	
	@Loggable
	public TradingAddressDetailsType[] getEDIAddresses(String companyID)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("TRADING_ADDRESSES_LIST", "V2");
		RequestPayload<TradingAddressDetailsType[]> payload = new RequestPayload<>(endPoint.toString(), null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<TradingAddressDetailsType[]>() {
				});
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class,companyID);
	}
	
	
	@Loggable
    public List<Country> getCountries(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException {
        String endPoint = serviceURLResolver.CMD("COUNTRY", "V1");
        HttpEntity<String> requestType = new HttpEntity<>(cmdAppSession());
        String param = locale.getLanguage();
        RequestPayload<List<Country>> payload = new RequestPayload<>(endPoint,
                HttpMethod.GET,
                requestType,
                new ParameterizedTypeReference<List<Country>>() {
                });
        return onboardingRestTemplate.exchange(payload, ErrorResponse.class, param);

    }


	public Language[] getLanguages(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("LANGUAGES", "V2");
		
        HttpEntity<String> requestType = new HttpEntity<>(cmdAppSession());
        String param = locale.getLanguage();
        RequestPayload<Language[]> payload = new RequestPayload<>(endPoint,
                HttpMethod.GET,
                requestType,
                new ParameterizedTypeReference<Language[]>() {
                });
        return onboardingRestTemplate.exchange(payload, ErrorResponse.class, param);
	}


	public List<Timezone> getTimezones(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMD("TIME_ZONES", "V1");
		HttpEntity<String> requestType = new HttpEntity<String>(cmdAppSession());
		String param = locale.getLanguage();
		RequestPayload<List<Timezone>> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestType,
				new ParameterizedTypeReference<List<Timezone>>() {
				});

		return onboardingRestTemplate.exchange(payload, ErrorResponse.class, param);
	}
	
	
	public RetrieveVANProviderResponse getVANProvidersList()
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("VAN_PROVIDERS_LIST", "V3");
		
		RequestPayload<RetrieveVANProviderResponse> payload = new RequestPayload<>(endPoint.toString(), null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<RetrieveVANProviderResponse>() {
				});

		return onboardingRestTemplate.exchange(payload, ErrorResponse.class);
	}
	
	@Loggable
	public BusinessUnitDetailsType[] validateTPExist(String companyName, String city,
			String country) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.CMS("VALIDATE_TP", "V3");
		
		RequestPayload<BusinessUnitDetailsType[]> payload = new RequestPayload<>(endPoint.toString(), null,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<BusinessUnitDetailsType[]>() {
				});
		
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class,companyName,city,country);
	}
	
}
