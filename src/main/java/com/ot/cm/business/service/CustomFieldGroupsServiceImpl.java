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
package com.ot.cm.business.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.CustomFieldCategoriesClient;
import com.ot.cm.rest.client.CustomFieldGroupsClient;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.CMDNeoRoleAssociation;
import com.ot.cm.rest.response.entity.CMDNeoRoleUpdate;
import com.ot.cm.rest.response.entity.CustomFieldCategoryType;
import com.ot.cm.rest.response.entity.CustomFieldGroupAndRoles;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
@Service
public class CustomFieldGroupsServiceImpl implements CustomFieldGroupsService {

	@Autowired
	private CustomFieldGroupsClient customFieldGroupsClient;

	@Autowired
	private OnboardingService onboardingService;

	@Autowired
	private CustomFieldCategoriesClient customFieldCategoriesClient;

	private String addWildCards(String param) {
		StringBuilder str = new StringBuilder();

		return str.append("*").append(param).append("*").toString();
	}

	@Override
	public CustomFieldGroupAndRoles get(CustomFieldGroupFilterSearchQuery filterObject, UserInfo userInfo)
			throws TGOCPRestException {

		CustomFieldGroupAndRoles response = new CustomFieldGroupAndRoles();
		ListingQueryResponse<CustomFieldGroupType> fieldGroup = list(filterObject, userInfo);

		CMDNeoRoleUpdate[] assignedRoles = customFieldGroupsClient.getSelectedRoles(filterObject.getUniqueId());

		response.setFgData(fieldGroup.getItemList().get(0));
		response.setSelectedRoles(assignedRoles);
		return response;

	}

	@Override
	public ListingQueryResponse<CustomFieldGroupType> list(CustomFieldGroupFilterSearchQuery filterObject,
			UserInfo userInfo) throws TGOCPRestException {

		ListingQueryResponse<CustomFieldGroupType> listResponse = new ListingQueryResponse<>();
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

		queryParams.add("categoryId", CommonUtil.isEmpty(filterObject.getCategoryId())
				? getDefaultCategory(userInfo).getCategoryId() : filterObject.getCategoryId());

		if (!CommonUtil.isEmpty(filterObject.getUniqueId())) {
			queryParams.add("uniqueId", filterObject.getUniqueId());
		}
		if (!CommonUtil.isEmpty(filterObject.getName())) {

			queryParams.add("name", addWildCards(filterObject.getName()));
		}
		if (!CommonUtil.isEmpty(filterObject.getDescription())) {

			queryParams.add("description", addWildCards(filterObject.getDescription()));
		}
		if (!CommonUtil.isEmpty(filterObject.getBusinessUnitId())) {

			queryParams.add("businessUnitId", filterObject.getBusinessUnitId());
		}
		if (!CommonUtil.isEmpty(filterObject.getCreationDate())) {
			queryParams.add("creationDate", filterObject.getCreationDate());
		}
		if (!CommonUtil.isEmpty(filterObject.getLastModifiedDate())) {
			queryParams.add("lastModifiedDate", filterObject.getLastModifiedDate());
		}
		if (!ObjectUtils.isEmpty(filterObject.getCreatedBy())) {
			queryParams.add("createdBy", String.join(",", filterObject.getCreatedBy()));
		}
		if (!ObjectUtils.isEmpty(filterObject.getModifiedBy())) {
			queryParams.add("modifiedBy", String.join(",", filterObject.getModifiedBy()));
		}
		if (!ObjectUtils.isEmpty(filterObject.getSortColumns())) {
			queryParams.add("sort", String.join(",", filterObject.getSortColumns()));
		}
		if (filterObject.isCountOnly()) {
			queryParams.add("countOnly", filterObject.isCountOnly() + "");
			Integer count = customFieldGroupsClient.count(queryParams, userInfo);
			listResponse.setItemCount(count);

		} else {
			CustomFieldGroupType[] list = customFieldGroupsClient.list(queryParams, userInfo);
			if (!ObjectUtils.isEmpty(list)) {
				if (filterObject.isParentsOnly()) {
					listResponse.setItemList(Arrays.asList(list).stream()
							.filter(fg -> StringUtils.isEmpty(fg.getDependentCustomfieldId())
									|| null == fg.getDependentChoiceIds() || 0 == fg.getDependentChoiceIds().length)
							.collect(Collectors.toList()));
				} else {
					listResponse.setItemList(Arrays.asList(list));
				}
			}
		}

		return listResponse;
	}

	@Override
	public CMDSuccessResponse add(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException {
		groupData.setBusinessUnitId(userInfo.getCompanyId());
		try {

			return customFieldGroupsClient.create(groupData, userInfo);
			// TODO - call grant API
		} catch (TGOCPRestException e) {
			if (e.getErrorCode().equals("TGOCPAPP-5000") && e.getRestErrorCode().equals("GM0001")) {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_GROUP_DUPLICATE_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else if (e.getErrorCode().equals("TGOCPREST-4000") && e.getRestErrorCode().equals("GM0001")) {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_GROUP_MAX_LIMIT_400", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_GROUP_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			}
		}
	}

	@Override
	public CMDSuccessResponse delete(String id, UserInfo userInfo) throws TGOCPRestException, CMApplicationException {
		List<Long> taskIds = onboardingService.getCustomTaskForFGId(id);

		if (taskIds.size() > 0) {
			throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_TASK_GROUP_ASSOSIATION_400", "", "");
		}

		try {
			return customFieldGroupsClient.delete(id, userInfo);
		} catch (TGOCPRestException e) {
			if (e.getErrorCode().equals("TGOCPREST-4000") && e.getRestErrorCode().equals("GM0001")) {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_PARENT_GROUP_DELETE_400",
						e.getMessage(), CommonUtil.getStackTrace(e));
			} else {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_DELETE_400", e.getMessage(),
						CommonUtil.getStackTrace(e));
			}
		}

	}

	@Override
	public CMDSuccessResponse edit(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException {

		if (!StringUtils.isEmpty(groupData.getDependentCustomfieldId())) {
			List<Long> taskIds = onboardingService.getCustomTaskForFGId(groupData.getUniqueId());

			if (taskIds.size() > 0) {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_TASK_GROUP_ASSOSIATION_UPDATE_400",
						"", "");
			}
		}

		groupData.setModifiedBy(userInfo.getUserId());
		groupData.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		try {
			return customFieldGroupsClient.edit(groupData, userInfo);
		} catch (TGOCPRestException e) {
			if (e.getErrorCode().equals("TGOCPAPP-5000") && e.getRestErrorCode().equals("GM0001")) {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_GROUP_DUPLICATE_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_GROUP_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			}
		}
	}

	public CMDSuccessResponse grantNeoRoles(String fieldGroupId, CMDNeoRoleAssociation neoRoleAssociationList)
			throws TGOCPRestException {
		CMDSuccessResponse response = null;
		try {
			response = customFieldGroupsClient.grantNeoRoles(fieldGroupId, neoRoleAssociationList);
		} catch (TGOCPRestException e) {

		}
		return response;

	}

	public CMDSuccessResponse revokeNeoRoles(String fieldGroupId, CMDNeoRoleAssociation neoRoleAssociationList)
			throws TGOCPRestException {
		CMDSuccessResponse response = null;
		try {
			return customFieldGroupsClient.revokeNeoRoles(fieldGroupId, neoRoleAssociationList);
		} catch (TGOCPRestException e) {

		}
		return response;
	}

	public CMDSuccessResponse grantAndRevokeRoles(CustomFieldGroupAndRoles groupData) throws TGOCPRestException {
		CMDNeoRoleAssociation neoRoleAssoc = new CMDNeoRoleAssociation();
		CMDSuccessResponse neoRolesUpdated = new CMDSuccessResponse();
		List<CMDNeoRoleUpdate> grantRoles = Arrays.stream(groupData.getSelectedRoles())
				.filter(rol -> !rol.isOldValue() && rol.isNewValue()).collect(Collectors.toList());
		List<String> grantBusinessUnitRoles = grantRoles.stream().map(rol -> rol.getRoleId())
				.collect(Collectors.toList());
		List<CMDNeoRoleUpdate> revokeRoles = Arrays.stream(groupData.getSelectedRoles())
				.filter(rol -> rol.isOldValue() && !rol.isNewValue()).collect(Collectors.toList());
		List<String> revokeBusinessUnitRoles = revokeRoles.stream().map(rol -> rol.getRoleId())
				.collect(Collectors.toList());

		if (!grantBusinessUnitRoles.isEmpty()) {
			neoRoleAssoc.setBusinessUnitRoles(grantBusinessUnitRoles);
			try {
				neoRolesUpdated = customFieldGroupsClient.grantNeoRoles(groupData.getFgData().getUniqueId(),
						neoRoleAssoc);
			} catch (TGOCPRestException e) {

			}

		}
		if (!revokeBusinessUnitRoles.isEmpty()) {
			neoRoleAssoc.setBusinessUnitRoles(revokeBusinessUnitRoles);
			try {
				neoRolesUpdated = customFieldGroupsClient.revokeNeoRoles(groupData.getFgData().getUniqueId(),
						neoRoleAssoc);
			} catch (TGOCPRestException e) {

			}
		}
		return neoRolesUpdated;
	}

	@Override
	public CustomFieldCategoryType getDefaultCategory(UserInfo userInfo) throws TGOCPRestException {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("categoryType", ApplicationConstants.DEFAULT_CUSTOM_FIELD_CATEGORY_CM);
		return customFieldCategoriesClient.list(queryParams, userInfo)[0];
	}
}
