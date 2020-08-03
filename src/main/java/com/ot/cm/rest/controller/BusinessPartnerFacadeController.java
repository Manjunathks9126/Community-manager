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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.BusinessPartnerService;
import com.ot.cm.business.service.GlobalDirectoryService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.request.entity.BusinessPartnerFacades;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */

@RestController
@RequestMapping("/businessPartner/facades")
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class BusinessPartnerFacadeController extends BaseRestController {

	@Autowired
	private BusinessPartnerService businessPartnerService;
	
	@Autowired
	private GlobalDirectoryService directoryService;

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-FACADE-CU", actionSummary = "Create/update partner company facade", actionDetail = "{0} facade as {1} for partner company: {2} of company: {3}")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> addUpdateFacades(HttpServletRequest httpServletRequest,
			@RequestBody BusinessPartnerFacades facades) throws TGOCPBaseException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		responseDetails.setResponseEntity(
				businessPartnerService.addOrUpdateBpFacades(facades, userInfo.getCompanyId(), userInfo));
		responseDetails.setHttpStatus(HttpStatus.OK);
		responseDetails.setStatusMessage(
				localizationMessages.getMessage("FACADE_DISPLAYNAME", HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setSuccess(true);
		response = new TGOCPRestResponse<>(responseDetails, facades.getUniqueId() == null ? "Created" : "Updated",
				"DisplayName : " + facades.getTargetBuCustomName()
						+ "" + (facades.getTargetBuCustomId() != null
								? " &Company ID : " + facades.getTargetBuCustomId() : ""),
				facades.getTargetBuId(), userInfo.getCompanyId());
		auditInfo.setCustBuId(facades.getTargetBuId());
		// directoryService.runDeltaQueryForBprs();// Added for solr data sync when facade data is updated
		return response;
	}

	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-FACADE-D", actionSummary = "delete partner company facade", actionDetail = "Facade Id {0} deleted")
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> deleteFacades(HttpServletRequest httpServletRequest,
			@RequestParam("id") String facadeId, @RequestBody BusinessPartnerFacades facade) throws TGOCPBaseException {
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		responseDetails.setResponseEntity(businessPartnerService.deleteFacade(facadeId, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		responseDetails.setStatusMessage(localizationMessages.getMessage("FACADE_DISPLAYNAME_DELETE",
				HttpStatus.OK.value(), userInfo.getLocale()));
		response = new TGOCPRestResponse<>(responseDetails, facadeId);
		auditInfo.setCustBuId(facade.getTargetBuId());
		return response;
	}
}
