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
package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.vo.AuditInfo;
import com.ot.cm.business.service.AuditLogService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("auditlog")
public class AuditLogController extends BaseRestController {

	@Autowired
	private AuditLogService auditLogService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<AuditInfo>, ErrorResponse> getCompany(HttpServletRequest httpServletRequest, @RequestParam String buId, 
			@RequestParam(value="countOnly",required=false) boolean countOnly,@RequestParam(value="limit",required=false) Integer limit,@RequestParam(value="startingFrom",required=false) Integer startingFrom) throws TGOCPBaseException {

		TGOCPRestResponse<ListingQueryResponse<AuditInfo>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<AuditInfo>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<ListingQueryResponse<AuditInfo>, ErrorResponse>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		responseDetails.setResponseEntity(auditLogService.getAuditLogs(userInfo.getCompanyId(), buId,countOnly,limit,startingFrom));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}
