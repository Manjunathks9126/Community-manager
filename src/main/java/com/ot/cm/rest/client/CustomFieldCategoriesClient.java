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
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.CustomFieldCategoryType;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * 
 * @author ddharmav
 *
 */
@Component
public class CustomFieldCategoriesClient extends BaseRestClient {

	@Loggable
	public CustomFieldCategoryType[] list(MultiValueMap<String, String> queryParams, UserInfo userInfo)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/customFieldCategories";
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(endPoint).queryParams(queryParams).build();
		TGOCPRestResponse<CustomFieldCategoryType[], ErrorResponse> details = restTemplate.exchange(
				uriComponents.toUriString(), HttpMethod.GET, new HttpEntity<String>("Headers", cmdAppSession()),
				CustomFieldCategoryType[].class);
		return details.getResponseDetails().getResponseEntity();
	}
}
