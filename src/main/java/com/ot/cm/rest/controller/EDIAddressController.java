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
  02/16/2017	   Madan							Web	Controller Related EDI Address Details
 ******************************************************************************/
package com.ot.cm.rest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.EDIAddressService;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("ediaddresses")
public class EDIAddressController {

	@Autowired
	private EDIAddressService ediAddressService;

	@GetMapping(value = "/comms", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<String, ErrorResponse> getEDIAddresses(HttpServletRequest httpServletRequest,
			@RequestParam("id") String ediAddress) throws CMApplicationException, TGOCPRestException {
		TGOCPRestResponse<String, ErrorResponse> response = null;
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String, ErrorResponse>();
		responseDetails.setResponseEntity(ediAddressService.getEDIAddressesComms(ediAddress));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<Map<String,String>>, ErrorResponse> getTradingAddresses(HttpServletRequest httpServletRequest,
			@RequestParam(value="countOnly",required=false) boolean countOnly,@RequestParam(value="limit",required=false) Integer limit,
			@RequestParam(value="after",required=false) Integer after) throws TGOCPBaseException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		TGOCPRestResponseDetails<ListingQueryResponse<Map<String,String>>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		responseDetails.setResponseEntity(ediAddressService.getBasicEdiAddresses(userInfo.getCompanyId(),countOnly,limit,after));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		TGOCPRestResponse<ListingQueryResponse<Map<String,String>>, ErrorResponse> response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}
	
	@GetMapping(value="/{companyId}/vanprovider", produces=MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<String, ErrorResponse> getVanProvider(HttpServletRequest httpServletRequest, @PathVariable String companyId) throws TGOCPBaseException {
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		responseDetails.setResponseEntity(ediAddressService.getVanProvider(companyId));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return new TGOCPRestResponse<>(responseDetails);
	}
}
