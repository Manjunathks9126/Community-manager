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
 02/16/2017	   Madan								Defined separate Rest client for User Info related calls
 ******************************************************************************/
package com.ot.cm.rest.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.UserSubcriptionDetailsType;
import com.ot.cm.cms.response.entity.User;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.ListingFilterQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.session.annotation.Loggable;

/**
 * The {@code UsersRestClient} class interface with CMS/CMD/RARest REST APIs to
 * get users information.
 * 
 */
@Component
public class UsersRestClient extends BaseRestClient {

	@Loggable
	public User[] getUsers(String filterQuery) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/users?");
		endPoint.append(filterQuery);
		TGOCPRestResponse<User[], ErrorResponse> userDetails = restTemplate.exchange(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), User[].class,
				RestActionContext.GET_USER, null);
		return userDetails.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String getUsersCount(String filterQuery) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/users?");
		endPoint.append(filterQuery);
		TGOCPRestResponse<String, ErrorResponse> userDetails = restTemplate.exchange(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), String.class,
				RestActionContext.USERS_SEARCH, null);
		return userDetails.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String buildUserQuery(ListingFilterQuery filterObject) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/users?where_company_id=");
		endPoint.append(filterObject.getId());
		endPoint.append(
				CommonUtil.isEmpty(filterObject.getEmailId()) ? "" : "&where_user_email=" + filterObject.getEmailId());

		TGOCPRestResponse<String, ErrorResponse> userDetails = restTemplate.exchange(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), String.class,
				RestActionContext.USERS_SEARCH, null);
		return userDetails.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param userId
	 * @return
	 */
	@Loggable
	public User getUser(String userId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v2/users/{userId}";

		TGOCPRestResponse<User, ErrorResponse> userDetails = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", basicHeaders()), User.class, RestActionContext.GET_USER, null,
				userId);
		return userDetails.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public UserSubcriptionDetailsType getRoles(String companyId, String userId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/");
		endPoint.append(companyId);
		endPoint.append("/users/");
		endPoint.append(userId).append("/subscriptions");

		TGOCPRestResponse<UserSubcriptionDetailsType, ErrorResponse> userDetails = restTemplate.exchange(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				UserSubcriptionDetailsType.class);
		return userDetails.getResponseDetails().getResponseEntity();
	}

}
