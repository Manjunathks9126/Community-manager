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
package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.TradingPartnerDirectoryService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.TradingPartnerModifyEntity;
import com.ot.cm.rest.request.entity.search.TPRFilterSearchQuery;
import com.ot.cm.rest.response.CMSSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.TPRData;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * Rest controller to handle TradingPartnerRelationships operations
 * 
 */
@RestController
@RequestMapping("tradingpartnerrelationships")
public class TradingPartnerRelationshipsController extends BaseRestController {

	@Autowired
	private TradingPartnerDirectoryService tpDirectoryService;

	/**
	 * 
	 * @param httpServletRequest
	 * @param filterObject
	 * @return TGOCPRestResponse<ListingQueryResponse<TPRData>,ErrorResponse>
	 * @throws CMApplicationException
	 * @throws TGOCPRestException
	 *
	 */
	@PostMapping(value = "")
	public TGOCPRestResponse<ListingQueryResponse<TPRData>, ErrorResponse> listTradingPartnerRelationships(
			HttpServletRequest httpServletRequest, @RequestBody TPRFilterSearchQuery filterObject)
			throws TGOCPRestException {

		TGOCPRestResponse<ListingQueryResponse<TPRData>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<TPRData>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		filterObject.setCompanyId(userInfo.getCompanyId());
		filterObject.setUserId(userInfo.getUserId());

		responseDetails.setResponseEntity(tpDirectoryService.getTPRData(filterObject));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	/**
	 * 
	 * @param httpServletRequest
	 * @param tprId
	 * @return TGOCPRestResponse<CMSSuccessResponse,ErrorResponse>
	 * @throws TGOCPRestException
	 * 
	 */
	@PostMapping(value = "/terminate")
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-TPR-D", actionSummary = "Terminate trading partner relationship", actionDetail = "{0} has terminated {1} trading partner relationship(s).")
	@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
	public TGOCPRestResponse<CMSSuccessResponse[], ErrorResponse[]> doTerminateTPR(
			HttpServletRequest httpServletRequest, @RequestBody TradingPartnerModifyEntity[] tprData)
			throws TGOCPRestException {
		TGOCPRestResponse<CMSSuccessResponse[], ErrorResponse[]> response = null;
		TGOCPRestResponseDetails<CMSSuccessResponse[], ErrorResponse[]> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(tpDirectoryService.terminateTradingPartnerRelationship(tprData));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		String failureCount = responseDetails.getResponseEntity()[0].getTargetResourceRefId();
		response = new TGOCPRestResponse<>(responseDetails, userInfo.getFirstName(),
				responseDetails.getResponseEntity().length - (Integer.parseInt(failureCount)));
		return response;
	}

	@PostMapping(value = "/approveReject")
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-TPR-U", actionSummary = "Approve/Reject trading partner relationship", actionDetail = "{0} has {1} {2} trading partner relationship(s).")
	@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
	public TGOCPRestResponse<CMSSuccessResponse[], ErrorResponse[]> doRejectOrApproveTPR(
			HttpServletRequest httpServletRequest, @RequestBody TradingPartnerModifyEntity[] tprData)
			throws TGOCPRestException {
		TGOCPRestResponse<CMSSuccessResponse[], ErrorResponse[]> response = null;
		TGOCPRestResponseDetails<CMSSuccessResponse[], ErrorResponse[]> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		responseDetails
				.setResponseEntity(tpDirectoryService.approveRejectTradingPartnerRelationship(tprData, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		String failureCount = responseDetails.getResponseEntity()[0].getTargetResourceRefId();
		response = new TGOCPRestResponse<>(responseDetails, userInfo.getFirstName(),
				tprData[0].getModifiedData().getModifyStatus().name() == "ACTIVE" ? "Approved" : "Rejected",
				responseDetails.getResponseEntity().length - (Integer.parseInt(failureCount)));
		return response;
	}

}
