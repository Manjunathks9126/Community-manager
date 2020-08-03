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
 Aug 23, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.CustomFieldsService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
@RestController
@RequestMapping("/customfields")
@CheckPermissions(permissions = {PermissionConstants.VIEW_ONBOARDING, PermissionConstants.VIEW_CUST_FIELD})
public class CustomFieldsController extends BaseRestController {

	@Autowired
	private CustomFieldsService customFieldService;

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> getCustomFields(HttpServletRequest httpServletRequest,
			@RequestParam(value = "forGroupId", required = true) String groupId,
			@RequestParam(value = "type", required = false) String[] type,
			@RequestParam(required = false) String[] sortBy) throws TGOCPBaseException {
		TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> response = null;
		TGOCPRestResponseDetails<CustomFieldEntity[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldService.list(groupId, userInfo, type, sortBy));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELD-C", actionSummary = "Create custom field", actionDetail = "Custom field {0} :id-{1} created")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> addCustomField(HttpServletRequest httpServletRequest,
			@RequestBody CustomFieldEntity fieldData)
			throws TGOCPBaseException {
		
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldService.create(fieldData, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_CREATE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, fieldData.getQuestion(),
				responseDetails.getResponseEntity().getUniqueId());

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELD-D", actionSummary = "Delete custom field(s)", actionDetail = "Deleted custom field(s): {0}")
	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> deleteCustomField(HttpServletRequest httpServletRequest,
			@RequestBody String[] ids) throws TGOCPBaseException {
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldService.delete(ids, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_DELETE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, String.join(",", ids));
		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELD-U", actionSummary = "Edit custom field", actionDetail = "Custom field {0} :id-{1} updated")
	@PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> editCustomField(HttpServletRequest httpServletRequest,
			@RequestBody CustomFieldEntity fieldData)
			throws TGOCPBaseException, IOException {
	
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldService.edit(fieldData, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_UPDATE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, fieldData.getQuestion(), fieldData.getUniqueId());

		return response;
	}

	@GetMapping(value = "/listForGroupDependency", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> getCustomFieldsForBU(
			HttpServletRequest httpServletRequest, @RequestParam(value = "forGroupId", required = false) String groupId,
			@RequestParam(value = "type", required = false) String[] type,
			@RequestParam(required = false) String[] sortBy) throws TGOCPBaseException {
		TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> response = null;
		TGOCPRestResponseDetails<CustomFieldEntity[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails
				.setResponseEntity(customFieldService.fieldlistForGroupDependency(groupId, userInfo, type, sortBy));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/listForFieldDependency", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> getCustomFieldsForGroup(
			HttpServletRequest httpServletRequest, @RequestParam(value = "fieldId", required = false) String fieldId,
			@RequestParam(value = "forGroupId", required = true) String groupId,
			@RequestParam(value = "type", required = false) String[] type,
			@RequestParam(required = false) String[] sortBy) throws TGOCPBaseException {
		TGOCPRestResponse<CustomFieldEntity[], ErrorResponse> response = null;
		TGOCPRestResponseDetails<CustomFieldEntity[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails
				.setResponseEntity(customFieldService.listForFieldDependency(fieldId, groupId, userInfo, type, sortBy));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity download(HttpServletRequest httpServletRequest, @PathVariable("id") String id,
			@RequestParam(value = "fileName", required = false) String fileName)
			throws TGOCPBaseException, IOException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		byte[] fileContent = customFieldService.download(id, fileName, userInfo);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(fileContent);

	}

}
