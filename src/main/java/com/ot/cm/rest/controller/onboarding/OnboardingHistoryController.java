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
 11/16/2017    Dwaraka                              Modified - 16.7
 ******************************************************************************/
package com.ot.cm.rest.controller.onboarding;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.request.entity.OnboardingHistoryQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.OnboardingHistoryResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("onboarding/history")
public class OnboardingHistoryController extends BaseRestController {

	@Autowired
	private OnboardingService onboardingService;

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<OnboardingHistoryResponse, ErrorResponse> onboardingHistory(
			HttpServletRequest httpServletRequest, @RequestBody OnboardingHistoryQuery historyQuery)
			throws TGOCPBaseException, JAXBException {
		TGOCPRestResponse<OnboardingHistoryResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<OnboardingHistoryResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(onboardingService.getOnboardingHistory(userInfo.getCompanyId(),
				historyQuery.getCompanyName(), historyQuery.getCompanyCity(), historyQuery.getCompanyCountry(), historyQuery.getCompanyId()));
		
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}
