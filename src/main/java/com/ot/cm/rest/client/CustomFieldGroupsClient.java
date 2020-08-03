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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.CMDNeoRoleAssociation;
import com.ot.cm.rest.response.entity.CMDNeoRoleUpdate;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Component
public class CustomFieldGroupsClient extends BaseRestClient {

	/**
	 * @param queryParams
	 * @param userInfo
	 * @return Integer
	 * @throws TGOCPRestException
	 */
	@Loggable
	public Integer count(MultiValueMap<String, String> queryParams, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups";
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(endPoint).queryParams(queryParams).build();
		TGOCPRestResponse<Integer, ErrorResponse> details = restTemplate.exchange(uriComponents.toUriString(),
				HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()), Integer.class);
		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param queryParams
	 * @param userInfo
	 * @return CustomFieldGroupType []
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CustomFieldGroupType[] list(MultiValueMap<String, String> queryParams, UserInfo userInfo)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups";
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(endPoint).queryParams(queryParams).build();
		TGOCPRestResponse<CustomFieldGroupType[], ErrorResponse> details = restTemplate.exchange(
				uriComponents.toUriString(), HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()),
				CustomFieldGroupType[].class);
		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @return CustomFieldGroupType []
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CMDNeoRoleUpdate[] getSelectedRoles(String fieldGrpId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/neoRoles/fieldGroup?groupId=" + fieldGrpId;
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(endPoint).build();
		TGOCPRestResponse<CMDNeoRoleUpdate[], ErrorResponse> details = restTemplate.exchange(
				uriComponents.toUriString(), HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()),
				CMDNeoRoleUpdate[].class);
		return details.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public CMDSuccessResponse create(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.POST,
				new HttpEntity<CustomFieldGroupType>(groupData, cmdAppSession()), CMDSuccessResponse.class);
		return details.getResponseDetails().getResponseEntity();
	}	

	/**
	 * @param id
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	@Loggable
	public CMDSuccessResponse delete(String id, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint,
				HttpMethod.DELETE, new HttpEntity<String>("header", cmdAppSession()), CMDSuccessResponse.class, id);
		return details.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param groupData
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	public CMDSuccessResponse edit(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldGroups/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.PUT,
				new HttpEntity<CustomFieldGroupType>(groupData, cmdAppSession()), CMDSuccessResponse.class,
				groupData.getUniqueId());
		return details.getResponseDetails().getResponseEntity();
	}

	public CMDSuccessResponse grantNeoRoles(String fieldGroupId, CMDNeoRoleAssociation neoRoleAssociationList)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/neoRoles/fieldGroup/{fieldGroupId}";
		HttpEntity<CMDNeoRoleAssociation> requestType = new HttpEntity<CMDNeoRoleAssociation>(neoRoleAssociationList,
				cmdAppSession());
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> neoRolesDetails = restTemplate.exchange(endPoint,
				HttpMethod.POST, requestType, CMDSuccessResponse.class, fieldGroupId);
		return neoRolesDetails.getResponseDetails().getResponseEntity();
	}

	public CMDSuccessResponse revokeNeoRoles(String fieldGroupId, CMDNeoRoleAssociation neoRoleAssociationList)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1//neoRoles/fieldGroup/{fieldGroupId}";
		HttpEntity<CMDNeoRoleAssociation> requestType = new HttpEntity<CMDNeoRoleAssociation>(neoRoleAssociationList,
				cmdAppSession());
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> neoRolesDetails = restTemplate.exchange(endPoint,
				HttpMethod.PUT, requestType, CMDSuccessResponse.class, fieldGroupId);
		return neoRolesDetails.getResponseDetails().getResponseEntity();
	}
}
