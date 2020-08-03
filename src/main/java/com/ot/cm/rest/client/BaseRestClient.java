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
package com.ot.cm.rest.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.HeaderConstants;
import com.ot.cm.rest.client.resolver.ServiceURLResolver;
import com.ot.cm.rest.template.TGOCPGenericRestTemplate;
import com.ot.cm.vo.UserInfo;
import com.ot.config.properties.GlobalProperties;
import com.ot.session.entity.TGOCPSMUserVO;

public class BaseRestClient {

	@Autowired
	protected TGOCPGenericRestTemplate restTemplate;

	@Autowired
	protected GlobalProperties globalProperties;

	@Autowired
	protected ApplicationProperties appProperties;

	@Autowired
	protected ServiceURLResolver serviceURLResolver;

	public HttpHeaders basicHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	public HttpHeaders cmdAppSession() {
		return cmdAppSession(MediaType.APPLICATION_JSON);
	}

	public HttpHeaders cmdAppSession(MediaType mt) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("im_community_id", globalProperties.getIm_community_id());
		headers.set("im_principal_type", "SERVICE_SESSION");
		headers.set("im_service_instance_id", globalProperties.getIm_service_instance_id());
		headers.set("Content-Type", mt.toString());
		headers.setAccept(Arrays.asList(mt));

		return headers;
	}

	public HttpHeaders cmdUserSession(UserInfo userInfo) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("im_community_id", globalProperties.getIm_community_id());
		headers.set("im_principal_type", "USER_SESSION");
		headers.set("im_service_instance_id", globalProperties.getIm_service_instance_id());
		headers.set("im_user_id", userInfo.getUserId());
		headers.set("im_bu_id", userInfo.getCompanyId());
		headers.set("Content-Type", "application/json");
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	public HttpHeaders setAppSessionHeaders(UserInfo userInfo) {
		HttpHeaders headers = new HttpHeaders();

		headers.set(HeaderConstants.CMS_PRINCIPAL_ORGID, userInfo.getCompanyId());
		headers.set(HeaderConstants.CMS_PRINCIPAL_USERID, userInfo.getUserId());
		headers.set(HeaderConstants.CMS_SERVICE_INSTANCE_ID, userInfo.getServiceInstanceID());
		headers.set(HeaderConstants.CMS_IM_COMMUNITY_ID, userInfo.getCommunityID());
		headers.set("Content-Type", "application/json");

		return headers;
	}

	public HttpHeaders cpsHeaders(TGOCPSMUserVO userInfo) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-ottg-caller-application", "TGOCP");
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		headers.set("x-ottg-caller-application-timestamp", formatDateTime);
		headers.set("x-ottg-caller-application-host", "NEO HOST");
		headers.set("x-ottg-principal-serviceid", userInfo.getServiceInstanceID());
		headers.set("x-ottg-im-community-id", userInfo.getCommunityID());
		headers.set("x-ottg-principal-userid", userInfo.getPrincipalID());
		headers.set("x-ottg-principal-authid", userInfo.getLogin());
		headers.set("x-ottg-principal-orgid", userInfo.getParentID());
		headers.set("Content-Type", "application/json");
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

}
