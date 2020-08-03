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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.CustomFieldGroupsService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.CustomFieldCategoryType;
import com.ot.cm.rest.response.entity.CustomFieldGroupAndRoles;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * Rest Controller to handle custom field groups for CRUD operations
 *
 */
@RestController
@RequestMapping("/customfieldgroups")
@CheckPermissions(permissions = { PermissionConstants.VIEW_ONBOARDING, PermissionConstants.VIEW_CUST_FIELD })
public class CustomFieldGroupsController extends BaseRestController {

	@Autowired
	private CustomFieldGroupsService customFieldGroupService;

	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<CustomFieldGroupType>, ErrorResponse> listCustomFieldGroups(
			HttpServletRequest httpServletRequest, @RequestParam(required = false) String groupId,
			@RequestParam(required = false) String[] sortBy, @RequestParam(required = false) boolean parentsOnly)
			throws TGOCPBaseException {

		CustomFieldGroupFilterSearchQuery filterObject = new CustomFieldGroupFilterSearchQuery();
		TGOCPRestResponse<ListingQueryResponse<CustomFieldGroupType>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<CustomFieldGroupType>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		filterObject.setBusinessUnitId(userInfo.getCompanyId());
		filterObject.setUniqueId(groupId);
		filterObject.setSortColumns(sortBy);
		filterObject.setParentsOnly(parentsOnly);
		responseDetails.setResponseEntity(customFieldGroupService.list(filterObject, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);
		return response;

	}

	@GetMapping(value = "/roles/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CustomFieldGroupAndRoles, ErrorResponse> listCustomFieldGroupsAndRoles(
			HttpServletRequest httpServletRequest, @PathVariable("groupId") String groupId) throws TGOCPBaseException {
		CustomFieldGroupFilterSearchQuery filterObject = new CustomFieldGroupFilterSearchQuery();// TODO
		TGOCPRestResponse<CustomFieldGroupAndRoles, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CustomFieldGroupAndRoles, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		filterObject.setBusinessUnitId(userInfo.getCompanyId());
		filterObject.setUniqueId(groupId);
		responseDetails.setResponseEntity(customFieldGroupService.get(filterObject, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELDGRP-C", actionSummary = "New custom field group", actionDetail = "Custom Field group {0} :id-{1} created")
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> addCustomFieldGroup(
			HttpServletRequest httpServletRequest, @RequestBody CustomFieldGroupAndRoles groupData)
			throws TGOCPBaseException {
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		CMDSuccessResponse addedFieldGroup = customFieldGroupService.add(groupData.getFgData(), userInfo);
		responseDetails.setResponseEntity(addedFieldGroup);
		groupData.getFgData().setUniqueId(addedFieldGroup.getUniqueId());
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_GROUP_CREATE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, groupData.getFgData().getName(),
				responseDetails.getResponseEntity().getUniqueId());

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELDGRP-D", actionSummary = "Delete custom field group", actionDetail = "Custom Field Group {0} deleted")
	@GetMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> deleteCustomFieldGroup(
			HttpServletRequest httpServletRequest, @PathVariable("id") String id) throws TGOCPBaseException {
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldGroupService.delete(id, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_GROUP_DELETE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, id);

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTFIELDGRP-U", actionSummary = "Update custom field group", actionDetail = "Custom Field group {0} :id-{1} updated")
	@PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> eitCustomFieldGroup(
			HttpServletRequest httpServletRequest, @RequestBody CustomFieldGroupAndRoles groupData)
			throws TGOCPBaseException {
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CMDSuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		if (CommonUtil.isEmpty(groupData.getFgData().getUniqueId())) {
			throw new TGOCPRestException(HttpStatus.BAD_REQUEST, "TGOCP-400", null, "Required Id is missing.", null);
		}
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldGroupService.edit(groupData.getFgData(), userInfo));
		customFieldGroupService.grantAndRevokeRoles(groupData);
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_FIELD_GROUP_UPDATE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, groupData.getFgData().getName(),
				responseDetails.getResponseEntity().getUniqueId());

		return response;
	}

	@GetMapping(value = "/defaultCategory", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<CustomFieldCategoryType, ErrorResponse> getCategory(HttpServletRequest httpServletRequest)
			throws TGOCPBaseException {
		TGOCPRestResponse<CustomFieldCategoryType, ErrorResponse> response = null;
		TGOCPRestResponseDetails<CustomFieldCategoryType, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customFieldGroupService.getDefaultCategory(userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

}
