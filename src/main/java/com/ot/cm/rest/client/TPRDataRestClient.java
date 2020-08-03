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
 Jun 1, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.client;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v3.entity.ModifyTradingPartnerRelationshipType;
import com.gxs.services.bsapi.rs.v3.entity.TradingPartnerRelationshipDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.CMSSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Component
public class TPRDataRestClient extends BaseRestClient {

	@Loggable
	/**
	 * 
	 * @param filterQuery
	 * @param companyId
	 * @return Integer
	 * @throws TGOCPRestException
	 */
	public Integer getTPRsCount(String filterQuery, Map<String, String> queryParams, String companyId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();

		if (null != queryParams && queryParams.size() > 0) {
			endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/tradingpartnerrelationships/");
			endPoint.append("?ownerCompanyId=").append(companyId);

			RequestPayload<Integer> payload = new RequestPayload<>(endPoint.toString(), queryParams, HttpMethod.GET,
					new HttpEntity<String>("headers", basicHeaders()), new ParameterizedTypeReference<Integer>() {
					});

			return restTemplate.exchange(payload, ErrorResponse.class);

		} else {
			endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/tradingpartnerrelationships/");
			endPoint.append("?ownerCompanyId=").append(companyId);
			endPoint.append(filterQuery);
			TGOCPRestResponse<Integer, ErrorResponse> resp = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
					new HttpEntity<String>("headers", basicHeaders()), Integer.class, companyId);
			return resp.getResponseDetails().getResponseEntity();
		}
	}

	@Loggable
	/**
	 * 
	 * @param filterQuery
	 * @param companyId
	 * @return TradingPartnerRelationshipDetailsType[]
	 * @throws TGOCPRestException
	 * 
	 */
	public TradingPartnerRelationshipDetailsType[] getTPRList(Map<String, String> queryParams, String companyId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/tradingpartnerrelationships/");
		endPoint.append("?ownerCompanyId=").append(companyId);
		RequestPayload<TradingPartnerRelationshipDetailsType[]> payload = new RequestPayload<>(endPoint.toString(),
				queryParams, HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<TradingPartnerRelationshipDetailsType[]>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class);

	}

	public TradingPartnerRelationshipDetailsEntity[] ViewTradingPartnerDetails(String tradingpartnerrelationId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/tradingpartnerrelationships/" + tradingpartnerrelationId);
		try {
			RequestPayload<TradingPartnerRelationshipDetailsEntity[]> payload = new RequestPayload<>(
					endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
					new ParameterizedTypeReference<TradingPartnerRelationshipDetailsEntity[]>() {
					});
			TradingPartnerRelationshipDetailsEntity[] ts = restTemplate.exchange(payload, ErrorResponse.class);
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param tprId
	 * @param companyId
	 * @param modifiedData
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 * 
	 */
	@Loggable
	public CMSSuccessResponse approveRejectTradingPartnerRelationship(String tprId, String companyId,
			ModifyTradingPartnerRelationshipType modifiedData) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL())
				.append("/v3/companies/{companyId}/tradingpartnerrelationships/{tprId}");
		TGOCPRestResponse<CMSSuccessResponse, ErrorResponse> response = restTemplate.exchange(endPoint.toString(),
				HttpMethod.PUT, new HttpEntity<ModifyTradingPartnerRelationshipType>(modifiedData, basicHeaders()),
				CMSSuccessResponse.class, companyId, tprId);
		return response.getResponseDetails().getResponseEntity();
	}

	/**
	 * 
	 * @param tprId
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 * 
	 */
	@Loggable
	public CMSSuccessResponse terminateTradingPartnerRelationship(String tprId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL()).append("/v2/tradingpartnerrelationships/{tprId}");
		TGOCPRestResponse<CMSSuccessResponse, ErrorResponse> response = restTemplate.exchange(endPoint.toString(),
				HttpMethod.DELETE, new HttpEntity<String>("headers", basicHeaders()), CMSSuccessResponse.class, tprId);
		return response.getResponseDetails().getResponseEntity();
	}

}
