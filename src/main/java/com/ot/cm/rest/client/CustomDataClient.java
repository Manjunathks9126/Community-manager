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

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.ImCustomFieldAnswerMap;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.BpCustomFieldAndAnswersDataList;
import com.ot.cm.rest.response.entity.CMDCustomFieldGroupRole;
import com.ot.cm.rest.response.entity.CustomDataFieldGroupType;
import com.ot.cm.rest.response.entity.CustomFieldAnswersResponse;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Component
public class CustomDataClient extends BaseRestClient {

	/**
	 * @param groupId
	 * @param partnerBuId
	 * @param userInfo
	 * @return BpCustomFieldAndAnswersDataList
	 * @throws TGOCPRestException
	 */
	@Loggable
	public BpCustomFieldAndAnswersDataList getFieldAnswerDataOfPartner(String groupId, String partnerBuId,
			UserInfo userInfo) throws TGOCPRestException {
		String endPoint = serviceURLResolver.CMD("CUSTOMFIELDANSWER", "V1");
		TGOCPRestResponse<BpCustomFieldAndAnswersDataList, ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("header", cmdAppSession()),
				BpCustomFieldAndAnswersDataList.class, groupId, partnerBuId);
		return details.getResponseDetails().getResponseEntity();
	}

	public CustomDataFieldGroupType[] listForCustomData(String companyId, String partnerBuId)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getRaURL() + "/customdata/fieldGroup/list?companyId=" + companyId
				+ "&partnerBuId=" + partnerBuId;
		TGOCPRestResponse<CustomDataFieldGroupType[], ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()), CustomDataFieldGroupType[].class);
		return details.getResponseDetails().getResponseEntity();
	}

	public CMDCustomFieldGroupRole[] getRoles(String groupIds) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/neoRoles/fieldGroup?groupId={groupIds}";
		TGOCPRestResponse<CMDCustomFieldGroupRole[], ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()), CMDCustomFieldGroupRole[].class,
				groupIds);
		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param groupId
	 * @param partnerBuId
	 * @param userInfo
	 * @param answerMap
	 * @return CustomFieldAnswersResponse
	 * @throws TGOCPRestException
	 */
	public CustomFieldAnswersResponse updateFieldAnswerMap(String groupId, String partnerBuId, UserInfo userInfo,
			Map<String, List<ImCustomFieldAnswerMap>> answerMap) throws TGOCPRestException {
		String endPoint = serviceURLResolver.CMD("CUSTOMFIELDANSWER", "V1");
		TGOCPRestResponse<CustomFieldAnswersResponse, ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.PUT, new HttpEntity<Map<String, List<ImCustomFieldAnswerMap>>>(answerMap, cmdAppSession()),
				CustomFieldAnswersResponse.class, groupId, partnerBuId);
		return details.getResponseDetails().getResponseEntity();
	}

}
