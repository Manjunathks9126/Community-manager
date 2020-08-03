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
  02/16/2017	   Madan							Web	Controller Related TradingPartnerDetails
 ******************************************************************************/
package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.TradingPartnerDetailsMergedService;
import com.ot.cm.business.service.TradingPartnerDetailsService;
import com.ot.cm.business.service.TradingPartnerDirectoryServiceImpl;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.TPRSearchQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.SearchQueryResponse;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.TradingParnterEDIAddress;
import com.ot.cm.vo.TradingParnterMergedEDIAddress;
import com.ot.cm.vo.UserInfo;

/**
 * Trading Partner Details like EDIs,Transactions,maps controller to handle load
 * of data,search,export.
 */
@RestController
@RequestMapping("partner")
public class TradingPartnerDetailsController extends BaseRestController {

	@Autowired
	private TradingPartnerDetailsService tpDetailsService;
	@Autowired
	private TradingPartnerDetailsMergedService tradingPartnerDetailsMergedService;
	@Autowired
	private TradingPartnerDirectoryServiceImpl tradingPartnerDirectoryService;

	@PostMapping(value = "/merge/ediaddresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<SearchQueryResponse<TradingParnterMergedEDIAddress>, ErrorResponse> getMergedEDIAddresses(
			HttpServletRequest httpServletRequest, @RequestBody TPRSearchQuery searchQeury)
			throws CMApplicationException, TGOCPRestException {
		TGOCPRestResponse<SearchQueryResponse<TradingParnterMergedEDIAddress>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SearchQueryResponse<TradingParnterMergedEDIAddress>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SearchQueryResponse<TradingParnterMergedEDIAddress>, ErrorResponse>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(
				tradingPartnerDetailsMergedService.getPartnerMergedEDIAddresses(searchQeury, userInfo.getCompanyId()));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@PostMapping(value = "/ediaddresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<SearchQueryResponse<TradingParnterEDIAddress>, ErrorResponse> getEDIAddresses(
			HttpServletRequest httpServletRequest, @RequestBody TPRSearchQuery searchQeury)
			throws CMApplicationException, TGOCPRestException {
		TGOCPRestResponse<SearchQueryResponse<TradingParnterEDIAddress>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SearchQueryResponse<TradingParnterEDIAddress>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SearchQueryResponse<TradingParnterEDIAddress>, ErrorResponse>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails
				.setResponseEntity(tpDetailsService.getPartnerEDIAddresses(searchQeury, userInfo.getCompanyId()));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
	
	@GetMapping(value= "/viewtp/{tprId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<TradingPartnerRelationshipDetailsEntity[], ErrorResponse> viewTradingPartner(HttpServletRequest httpServletRequest, 
			@PathVariable("tprId") String tprId) throws TGOCPRestException {
		TGOCPRestResponseDetails<TradingPartnerRelationshipDetailsEntity[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TradingPartnerRelationshipDetailsEntity[] tprDetails = tradingPartnerDirectoryService.getTradingpartnerDetails(tprId);
		responseDetails.setResponseEntity(tprDetails);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return new TGOCPRestResponse<>(responseDetails);
	}
}
