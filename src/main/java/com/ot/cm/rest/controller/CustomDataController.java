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
 Sep 7, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.audit.vo.AuditInfo;
import com.ot.cm.business.service.CustomDataService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.BpCustomFieldAndAnswersData;
import com.ot.cm.rest.response.entity.CustomDataFieldGroupType;
import com.ot.cm.rest.response.entity.CustomDataSave;
import com.ot.cm.rest.response.entity.CustomFieldAnswersResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
@RestController
@RequestMapping("/customdata")
public class CustomDataController extends BaseRestController {

	@Autowired
	private CustomDataService customDataService;

	@Autowired
	private AuditInfo auditInfo;

	@GetMapping(value = "/partner/withAnswer", produces = MediaType.APPLICATION_JSON_VALUE)
	public BpCustomFieldAndAnswersData[] getPartnerFieldGroupData(HttpServletRequest httpServletRequest,
			@RequestParam(value = "groupId", required = true) String groupId,
			@RequestParam(value = "partnerBuId", required = true) String partnerBuId) throws TGOCPBaseException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		return customDataService.getPartnerFieldAnswerData(groupId, partnerBuId, userInfo);

	}

	@GetMapping(value = "/fieldGroup/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomDataFieldGroupType> listCustomFieldGroups(HttpServletRequest httpServletRequest,
			@RequestParam(value = "partnerBuId", required = true) String partnerBuId,
			@RequestParam(required = false) String[] sortBy) throws TGOCPBaseException {

		CustomFieldGroupFilterSearchQuery filterObject = new CustomFieldGroupFilterSearchQuery();// TODO
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		filterObject.setBusinessUnitId(userInfo.getCompanyId());
		filterObject.setSortColumns(sortBy);

		return customDataService.getCustomFieldGroups(filterObject, partnerBuId, userInfo);
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-CUSTDATAANSWER-S", actionSummary = "Set custom data answer", actionDetail = "Custom data of groups:{0} is set for company:{1}")
	@PostMapping(value = "/partner/withAnswer", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<CustomFieldAnswersResponse>, ErrorResponse> updatePartnerFieldGroupAnswer_1(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "partnerBuId", required = true) String partnerBuId,
			@RequestBody List<CustomDataSave> answerData) throws TGOCPBaseException {
		TGOCPRestResponse<List<CustomFieldAnswersResponse>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<List<CustomFieldAnswersResponse>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails
				.setResponseEntity(customDataService.updatePartnerFieldAnswerData(partnerBuId, userInfo, answerData));
		responseDetails.setSuccess(true);
		responseDetails.setStatusMessage(localizationMessages.getMessage("CUSTOM_DATA_SAVE_SUCCESS",
				HttpStatus.OK.value(), userInfo.getLocale()));
		responseDetails.setHttpStatus(HttpStatus.OK);
		String groupIds = getGroupIds(answerData);
		response = new TGOCPRestResponse<>(responseDetails, groupIds, partnerBuId);
		auditInfo.setCustBuId(partnerBuId);
		return response;
	}

	private String getGroupIds(List<CustomDataSave> answerData) {
		if (!CollectionUtils.isEmpty(answerData))
			return String.join(",", answerData.stream().map(data -> data.getGroupId()).collect(Collectors.toSet()));
		return null;
	}

}
