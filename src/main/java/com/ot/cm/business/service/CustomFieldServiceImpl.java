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
package com.ot.cm.business.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.CustomFieldRestClient;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
@Service
public class CustomFieldServiceImpl implements CustomFieldsService {

	@Autowired
	private CustomFieldRestClient cfrc;

	@Autowired
	private CustomFieldGroupsService customFieldGroupService;

	@Override
	public CustomFieldEntity[] list(String groupId, UserInfo userInfo, String[] type, String[] sortColumns)
			throws TGOCPRestException {

		return cfrc.getCustomFieldList(new String[] { groupId }, userInfo, type, sortColumns);
	}

	@Override
	public CMDSuccessResponse create(CustomFieldEntity fieldData, UserInfo userInfo) throws TGOCPRestException {
		try {
			return cfrc.addCustomField(fieldData, userInfo);
		} catch (TGOCPRestException e) {
			if (HttpStatus.CONFLICT.equals(e.getHttpStatus())) {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_FIELD_DUPLICATE_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else if (HttpStatus.BAD_REQUEST.equals(e.getHttpStatus()) && e.getRestErrorCode().equals("GM0001")) {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_FIELD_MAX_LIMIT_400", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_FIELD_CREATE_FAIL_400",
						e.getMessage(), CommonUtil.getStackTrace(e));
			}
		}
	}

	@Override
	public CMDSuccessResponse delete(String[] ids, UserInfo userInfo) throws TGOCPRestException {
		CMDSuccessResponse resp = new CMDSuccessResponse();
		for (String id : ids) {
			try {
				cfrc.deleteCustomField(id, userInfo);
			} catch (TGOCPRestException e) {
				if (e.getErrorCode().equals("TGOCP-CMS-REST-4040") && e.getRestErrorCode().equals("GM0001")) {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_PARENT_FIELD_DELETE_400",
							e.getMessage(), CommonUtil.getStackTrace(e));
				} else {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_DELETE_400", e.getMessage(),
							CommonUtil.getStackTrace(e));
				}
			}
		}
		return resp;
	}

	@Override
	public CMDSuccessResponse edit(CustomFieldEntity fieldData, UserInfo userInfo) throws TGOCPRestException {
		fieldData.setModifiedBy(userInfo.getUserId());
		try {
			return cfrc.editCustomField(fieldData, userInfo);
		} catch (TGOCPRestException e) {

			if (HttpStatus.CONFLICT.equals(e.getHttpStatus())) {
				throw new TGOCPRestException(HttpStatus.CONFLICT, null, "CUSTOM_FIELD_DUPLICATE_500", e.getMessage(),
						CommonUtil.getStackTrace(e));
			} else {
				throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_FIELD_UPDATE_FAIL_400",
						e.getMessage(), CommonUtil.getStackTrace(e));
			}
		}
	}

	@Override
	public CustomFieldEntity[] fieldlistForGroupDependency(String currentGroupId, UserInfo userInfo, String[] type,
			String[] sortColumns) throws TGOCPRestException {

		// get all groupList
		CustomFieldGroupFilterSearchQuery filterObject = new CustomFieldGroupFilterSearchQuery();
		filterObject.setBusinessUnitId(userInfo.getCompanyId());

		List<CustomFieldGroupType> allFieldGroups = customFieldGroupService.list(filterObject, userInfo).getItemList();
		if (CollectionUtils.isEmpty(allFieldGroups)) {
			return null;
		}

		Set<String> allowedGroupIds = new HashSet<>();
		allowedGroupIds = allFieldGroups.stream().map(group -> group.getUniqueId()).collect(Collectors.toSet());
		String allGroupIdsAsString = String.join(",", allowedGroupIds);

		// get fields of all group
		CustomFieldEntity[] fieldListForAllGroup = list(allGroupIdsAsString, userInfo, new String[0], new String[0]);

		// store Fields corresponding to groupId in map
		Map<String, List<CustomFieldEntity>> fieldListForGroupsAsMap = new HashMap<>();

		Arrays.asList(fieldListForAllGroup).stream().forEach(field -> {
			if (null == fieldListForGroupsAsMap.get(field.getGroupId())) {
				List<CustomFieldEntity> listField = new ArrayList<CustomFieldEntity>();
				listField.add(field);
				fieldListForGroupsAsMap.put(field.getGroupId(), listField);
			} else {
				List<CustomFieldEntity> listField = fieldListForGroupsAsMap.get(field.getGroupId());
				listField.add(field);
				fieldListForGroupsAsMap.put(field.getGroupId(), listField);
			}

		});

		/*
		 * get the groupId's whose fields are not allowed to be parent of
		 * current group
		 */

		Set<String> exemptedGroupIds = new HashSet<>();
		Set<String> childGroupIdOfCurrentGroup = new HashSet<>();

		/* NOT Allowed: child of current group */
		if (null != currentGroupId) {// for "EDIT GROUP"
			childGroupIdOfCurrentGroup = getChildOfGroup(fieldListForGroupsAsMap.get(currentGroupId), allFieldGroups);// all
																														// child
																														// of
																														// current
																														// group
			exemptedGroupIds.addAll(childGroupIdOfCurrentGroup);

			/* NOT Allowed: children of current group's children */
			Set<String> grandChildrenGroupIdOfCurrentGroup = new HashSet<>();
			for (String childGroupId : childGroupIdOfCurrentGroup) {
				grandChildrenGroupIdOfCurrentGroup = getChildOfGroup(fieldListForGroupsAsMap.get(childGroupId),
						allFieldGroups);// child of child group
				if (grandChildrenGroupIdOfCurrentGroup.size() > 0) {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "TWO_LEVEL_DEPENDENCY_400", "", "");
				}
				exemptedGroupIds.addAll(grandChildrenGroupIdOfCurrentGroup);
			}

			/*
			 * NOT Allowed: X----parent of--------> Y and A-------parent of
			 * ------> B than, Y can't be parent of A (2-level dependency)
			 */
			if (childGroupIdOfCurrentGroup.size() > 0) { // if current field has
															// child than,

				// groups which are children to other group
				Set<String> ids = allFieldGroups.stream()
						.filter(group -> null != group.getDependentCustomfieldId()
								&& null != group.getDependentChoiceIds() && group.getDependentChoiceIds().length > 0)
						.map(group -> group.getUniqueId()).collect(Collectors.toSet());

				exemptedGroupIds.addAll(ids);
			}
		}

		// NOT ALLOWED: Groups which already reached 2-level dependency i.e
		// (X--->Y----->Z than Z can not be parent of A(new group))
		for (String groupId : allowedGroupIds) {
			Set<String> oneLevelGroupIds = getChildOfGroup(fieldListForGroupsAsMap.get(groupId), allFieldGroups);
			for (String childGroupId : oneLevelGroupIds) {
				exemptedGroupIds.addAll(getChildOfGroup(fieldListForGroupsAsMap.get(childGroupId), allFieldGroups));
			}
		}

		allowedGroupIds.removeAll(exemptedGroupIds);
		allowedGroupIds.remove(currentGroupId);
		String allowedIdsAsString = String.join(",", allowedGroupIds);
		return cfrc.getCustomFieldList(new String[] { allowedIdsAsString }, userInfo, type, sortColumns);
	}

	public Set<String> getChildOfGroup(List<CustomFieldEntity> fieldListOfGroup, List<CustomFieldGroupType> groupList) {
		Set<String> childGroupIds = new HashSet<>();
		if (null != fieldListOfGroup && fieldListOfGroup.size() > 0) {
			Set<String> parentFieldIds = fieldListOfGroup.stream().map(group -> group.getUniqueId())
					.collect(Collectors.toSet());
			childGroupIds = groupList.stream()
					.filter(group -> null != group.getDependentCustomfieldId()
							&& parentFieldIds.contains(group.getDependentCustomfieldId())
							&& null != group.getDependentChoiceIds() && group.getDependentChoiceIds().length > 0)
					.map(group -> group.getUniqueId()).collect(Collectors.toSet());

		}

		return childGroupIds;
	}

	@Override
	public CustomFieldEntity[] listForFieldDependency(String fieldId, String groupId, UserInfo userInfo, String[] type,
			String[] sortColumns) throws TGOCPRestException, CMApplicationException {
		// TODO Auto-generated method stub
		CustomFieldEntity[] fieldList = cfrc.getCustomFieldList(new String[] { groupId }, userInfo, new String[0],
				sortColumns);
		Set<String> allFieldIds = Arrays.stream(fieldList).map(field -> field.getUniqueId())
				.collect(Collectors.toSet());
		Set<String> exemptedFieldIds = new HashSet<>();

		if (null != fieldId) {
			// child field
			Set<String> childOfCurrentField = Arrays.stream(fieldList)
					.filter(field -> null != field.getDependentCustomfieldId() && null != field.getDependentChoiceIds()
							&& field.getDependentChoiceIds().length > 0
							&& field.getDependentCustomfieldId().equalsIgnoreCase(fieldId))
					.map(field -> field.getUniqueId()).collect(Collectors.toSet());
			exemptedFieldIds.addAll(childOfCurrentField);
			// child of child field
			for (String childField : exemptedFieldIds) {
				Set<String> grandChildrenOfCurrentFiled = Arrays.stream(fieldList)
						.filter(field -> null != field.getDependentCustomfieldId()
								&& null != field.getDependentChoiceIds() && field.getDependentChoiceIds().length > 0
								&& field.getDependentCustomfieldId().equalsIgnoreCase(childField))
						.map(field -> field.getUniqueId()).collect(Collectors.toSet());
				if (grandChildrenOfCurrentFiled.size() > 0) {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "TWO_LEVEL_DEPENDENCY_400", "", "");
				}
			}

			// X---->Y and A--->B than Y can't be parent of A(current field)
			if (childOfCurrentField.size() > 0) {
				Set<String> ids = Arrays.stream(fieldList)
						.filter(field -> null != field.getDependentCustomfieldId()
								&& null != field.getDependentChoiceIds() && field.getDependentChoiceIds().length > 0)
						.map(field -> field.getUniqueId()).collect(Collectors.toSet());
				exemptedFieldIds.addAll(ids);
			}
		}
		// check for two level dependency
		for (CustomFieldEntity parentField : fieldList) {
			Set<String> childIds = Arrays.stream(fieldList)
					.filter(field -> null != field.getDependentCustomfieldId() && null != field.getDependentChoiceIds()
							&& field.getDependentChoiceIds().length > 0
							&& field.getDependentCustomfieldId().equalsIgnoreCase(parentField.getUniqueId()))
					.map(field -> field.getUniqueId()).collect(Collectors.toSet());

			for (String childId : childIds) {
				Set<String> grandChildIds = Arrays.stream(fieldList)
						.filter(field -> null != field.getDependentCustomfieldId()
								&& null != field.getDependentChoiceIds() && field.getDependentChoiceIds().length > 0
								&& field.getDependentCustomfieldId().equalsIgnoreCase(childId))
						.map(field -> field.getUniqueId()).collect(Collectors.toSet());
				exemptedFieldIds.addAll(grandChildIds);
			}
		}

		allFieldIds.removeAll(exemptedFieldIds);
		List<CustomFieldEntity> allowedFields = Arrays.stream(fieldList)
				.filter(field -> Arrays.asList(type).contains(field.getCustomFieldType())
						&& allFieldIds.contains(field.getUniqueId()))
				.collect(Collectors.toList());

		CustomFieldEntity[] arr = new CustomFieldEntity[allowedFields.size()];
		return allowedFields.toArray(arr);
	}

	@Override
	public byte[] download(String fileId, String fileName, UserInfo userInfo) throws TGOCPRestException, IOException {
		byte[] fileContent = cfrc.getFile(fileId, userInfo);
		return fileContent;
	}
}
