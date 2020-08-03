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
 Mar 12, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.controller.onboarding.bulk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.constants.BulkImportType;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkImportFactory;
import com.ot.cm.rest.request.entity.BulkImportData;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.FileMetaData;

/**
 * @author ssen
 *
 */
@RestController
@RequestMapping("bulk/invitations")
public class BulkInvitationsController {

	@Autowired
	BulkImportFactory bulkImportFactory;

	@Autowired
	OnboardingService onboardingService;

	@GetMapping(value = "/{workflowId}/template")
	@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
	public ResponseEntity<Resource> downloadTemplate(HttpServletRequest httpServletRequest,
			@RequestParam("templateName") String templateName, @PathVariable() Long workflowId)
			throws TGOCPBaseException, MalformedURLException {
		UserInfo user = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		Resource fileResource = bulkImportFactory.getService(BulkImportType.INVITE).download(templateName, workflowId,
				user);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
				.body(fileResource);
	}

	@PostMapping(value = "/{workflowId}/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
	public TGOCPRestResponse<FileMetaData, ErrorResponse> validateBulkInivitationsFile(
			HttpServletRequest httpServletRequest, @RequestPart("file") MultipartFile uploadedFile,
			@RequestParam("invitationId") Long invitationId, @PathVariable(value = "workflowId") Long workflowId)
			throws CMApplicationException, TGOCPRestException {

		TGOCPRestResponse<FileMetaData, ErrorResponse> response = null;
		TGOCPRestResponseDetails<FileMetaData, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(
				bulkImportFactory.getService(BulkImportType.INVITE).validateFile(uploadedFile, null, workflowId, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMAPNY-INVITE-BULK", actionSummary = "Bulk Trading Parenters Invite", actionDetail = "Bulk Invitations started with request id - {0,number,#}", hasContent = true)
	@PostMapping(value = "/{workflowId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
	public TGOCPRestResponse<FileMetaData, ErrorResponse> invitationProcessStart(HttpServletRequest httpServletRequest,
			@RequestPart("file") MultipartFile uploadedFile, @PathVariable(value = "workflowId") Long workflowId,
			@RequestPart(value = "metaInfo", required = true) FileMetaData metainfo,
			@RequestParam("invitationId") Long invitationId) throws CMApplicationException {

		TGOCPRestResponse<FileMetaData, ErrorResponse> response = null;
		TGOCPRestResponseDetails<FileMetaData, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		FileMetaData returnedData = bulkImportFactory.getService(BulkImportType.INVITE).start(uploadedFile,
				invitationId, metainfo, userInfo, workflowId, BulkImportType.INVITE);
		responseDetails.setResponseEntity(returnedData);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails, returnedData.getRequestId(), returnedData);
		return response;
	}

	@PostMapping(value = "/process/single") // async admin dependent
	public Map<String, String> processAsingleRow(HttpServletRequest httpServletRequest,
			@RequestBody BulkImportData bulkrowData) throws CMApplicationException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		return bulkImportFactory.getService(BulkImportType.INVITE).processArow(bulkrowData, userInfo);

	}

	// any change in this url will impact async admin, bulk invitation flow
	@GetMapping(value = "/details/{invitationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public InvitationNeoRest getInvitation(@PathVariable("invitationId") Long invitationId)
			throws CMApplicationException, TGOCPRestException, IOException {
		return onboardingService.getInvitationData(invitationId);
	}

}
