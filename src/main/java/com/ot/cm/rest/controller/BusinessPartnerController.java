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
 May 18, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gxs.services.bsapi.rs.v3.entity.BaseTradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.TPDirectoryDetailsType;
import com.ot.cm.business.service.BusinessPartnerService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */

@RestController
@RequestMapping("/businessPartner")
public class BusinessPartnerController extends BaseRestController {

	@Autowired
	private BusinessPartnerService businessPartnerService;

	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<TPDirectoryDetailsType>, ErrorResponse> listBusinessPartners(
			HttpServletRequest httpServletRequest, @RequestBody BPRFilterSearchQuery filterObject)
			throws TGOCPBaseException, ParseException {
		TGOCPRestResponse<ListingQueryResponse<TPDirectoryDetailsType>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<TPDirectoryDetailsType>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		filterObject.setCompanyId(userInfo.getCompanyId());
		filterObject.setUserId(userInfo.getUserId());

		responseDetails.setResponseEntity(businessPartnerService.getBusinessPartnerList(filterObject));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/edis", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<BaseTradingAddressType>, ErrorResponse> getCompany(
			HttpServletRequest httpServletRequest, @RequestParam String bprId,
			@RequestParam(value = "countOnly", required = false) boolean countOnly,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "startingFrom", required = false) Integer startingFrom) throws TGOCPBaseException {

		TGOCPRestResponse<ListingQueryResponse<BaseTradingAddressType>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<BaseTradingAddressType>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		responseDetails.setResponseEntity(
				businessPartnerService.getEdis(userInfo.getCompanyId(), bprId, countOnly, limit, startingFrom));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

}
