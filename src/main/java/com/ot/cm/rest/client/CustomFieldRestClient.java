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
 Aug 23, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Component
public class CustomFieldRestClient extends BaseRestClient {

	/**
	 * @param groupId
	 * @param userInfo
	 * @return CustomFieldEntity []
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CustomFieldEntity[] getCustomFieldList(String[] groupIds, UserInfo userInfo, String[] type, String[] sortby)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields?groupId="
				+ String.join(",", groupIds);
		if (!ObjectUtils.isEmpty(type)) {
			endPoint += "&type=" + String.join(",", type);
		}
		if (!ObjectUtils.isEmpty(sortby)) {
			endPoint += "&sort=" + String.join(",", sortby);
		}
		TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("Headers", cmdAppSession()), CustomFieldEntity[].class);

		return details.getResponseDetails().getResponseEntity();
	}

	public CustomFieldEntity getCustomField(String fieldId, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields/" + fieldId;

		TGOCPRestResponse<CustomFieldEntity, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("Headers", cmdAppSession()), CustomFieldEntity.class);

		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param fieldData
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CMDSuccessResponse addCustomField(CustomFieldEntity fieldData, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.POST,
				new HttpEntity<CustomFieldEntity>(fieldData, cmdAppSession()), CMDSuccessResponse.class);
		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param id
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CMDSuccessResponse deleteCustomField(String id, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.DELETE, new HttpEntity<String>("header", cmdAppSession()), CMDSuccessResponse.class, id);
		return details.getResponseDetails().getResponseEntity();

	}

	/**
	 * @param fieldData
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	public CMDSuccessResponse editCustomField(CustomFieldEntity fieldData, UserInfo userInfo)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.PUT,
				new HttpEntity<CustomFieldEntity>(fieldData, cmdAppSession()), CMDSuccessResponse.class,
				fieldData.getUniqueId());
		return details.getResponseDetails().getResponseEntity();
	}

	public byte[] getFile(String fileId, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields/fileDownload/" + fileId;
		RequestPayload<byte[]> payload = new RequestPayload<>(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession(MediaType.APPLICATION_OCTET_STREAM)),
				new ParameterizedTypeReference<byte[]>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class);

	}
	
	
	public CustomFieldEntity[] getCustomFieldInfoBasedId(String fieldId, UserInfo userInfo, String groupIds) throws TGOCPRestException {
		
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFields?groupId="+ groupIds+"&customFieldId=" + fieldId;

		TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("Headers", cmdAppSession()), CustomFieldEntity[].class);

		return details.getResponseDetails().getResponseEntity();
	}
	
	
	public CustomFieldGroupType[] getCustomFieldGroupsBasedonBuid(String businessUnitId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups?businessUnitId="+ businessUnitId;

		TGOCPRestResponse<CustomFieldGroupType[], ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("Headers", cmdAppSession()), CustomFieldGroupType[].class);

		return details.getResponseDetails().getResponseEntity();
	}
	

}
