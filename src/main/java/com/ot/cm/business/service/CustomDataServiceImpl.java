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
package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.CustomDataClient;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.rest.request.entity.ImCustomFieldAnswerMap;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.entity.BpCustomFieldAndAnswersData;
import com.ot.cm.rest.response.entity.BpCustomFieldAndAnswersDataList;
import com.ot.cm.rest.response.entity.CMDCustomFieldGroupRole;
import com.ot.cm.rest.response.entity.CMDNeoRole;
import com.ot.cm.rest.response.entity.CustomDataFieldGroupType;
import com.ot.cm.rest.response.entity.CustomDataSave;
import com.ot.cm.rest.response.entity.CustomFieldAnswersResponse;
import com.ot.cm.rest.response.entity.FileObject;
import com.ot.cm.rest.response.entity.ImportCustomDataSave;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
@Service
public class CustomDataServiceImpl implements CustomDataService {

	@Autowired
	private CustomDataClient customDataClient;

	@Autowired
	private CustomRolesService customRolesService;

	@Autowired
	NeoRestClient neoRestClient;

	private static final String customFieldEditQualifier = "NEO_EDIT_CFG";
	private static final String ADMIN_PERMISSION = "VIEW_ONBOARDING";

	@Override
	public BpCustomFieldAndAnswersData[] getPartnerFieldAnswerData(String groupId, String partnerBuId,
			UserInfo userInfo) throws TGOCPRestException {
		BpCustomFieldAndAnswersDataList list = customDataClient.getFieldAnswerDataOfPartner(groupId, partnerBuId,
				userInfo);
		if (!ObjectUtils.isEmpty(list)) {
			return list.getBpCustomFieldAndAnswersList();
		}

		return null;
	}

	@Override
	public List<CustomDataFieldGroupType> getCustomFieldGroups(CustomFieldGroupFilterSearchQuery filterObject,
			String partnerBuId, UserInfo userInfo) throws TGOCPRestException {

		// get all groups
		CustomDataFieldGroupType[] fieldGroups = customDataClient.listForCustomData(filterObject.getBusinessUnitId(),
				partnerBuId);

		return getAccessControlledFieldGroups(fieldGroups, userInfo);
	}

	/**
	 * This method does the following Gets the user assigned custom roles Gets
	 * the custom roles mapped to all the FGs 1. club the FGs w/o roles & FGs
	 * mapped to the user assigned custom roles 2. Remove the FGs which are not
	 * all covered in user assigned custom roles 3. now set the isReadOnly flag
	 * on each FG based on the corresponding Role qualifier 4. Child FGs follow
	 * the Parent FG Configuration
	 * 
	 * @param fieldGroups
	 * @param userInfo
	 * @return
	 * @throws TGOCPRestException
	 */
	private List<CustomDataFieldGroupType> getAccessControlledFieldGroups(CustomDataFieldGroupType[] fieldGroups,
			UserInfo userInfo) throws TGOCPRestException {
		List<CustomDataFieldGroupType> allFieldGroups = Arrays.asList(fieldGroups);
		List<CustomDataFieldGroupType> accessControlledFieldGroups = new ArrayList<>();

		// get the custom roles mapped to all the FGs
		String groupIds = String.join(",",
				allFieldGroups.stream().map(data -> data.getUniqueId()).collect(Collectors.toSet()));
		Set<CMDCustomFieldGroupRole> allCustomRoles = new HashSet<>(Arrays.asList(customDataClient.getRoles(groupIds)));

		// Get the user assigned custom roles
		List<CMDNeoRole> userAssignedCustomRoles = customRolesService.getCustomFieldRoles(userInfo.getCompanyId(),
				userInfo.getUserId());

		if (!userAssignedCustomRoles.isEmpty()) {
			/*
			 * 1. club the FGs w/o roles & FGs mapped to the user assigned
			 * custom roles 2. Remove the FGs which are not all covered in user
			 * assigned custom roles 3. now set the isReadOnly flag on each FG
			 * based on the corresponding Role qualifier 4. Child FGs follow the
			 * Parent FG Configuration
			 */

			for (CustomDataFieldGroupType fieldGroup : allFieldGroups) {

				// Child FGs
				if (null != fieldGroup.getDependentCustomfieldId() && null != fieldGroup.getDependentChoiceIds()
						&& fieldGroup.getDependentChoiceIds().length > 0) {
					// Child FG follows Parent FG's accessibility
					CustomDataFieldGroupType parentFieldGroup = accessControlledFieldGroups.stream()
							.filter(fg -> null != fieldGroup.getDependentGroupId()
									&& fieldGroup.getDependentGroupId().equals(fg.getUniqueId()))
							.findAny().orElse(null);
					if (null != parentFieldGroup) {
						fieldGroup.setReadOnly(parentFieldGroup.isReadOnly());
						accessControlledFieldGroups.add(fieldGroup);
					}
				} else { // Parent FGs
					List<CMDCustomFieldGroupRole> fgAssignedCustomRoles = allCustomRoles.stream()
							.filter(fg -> fieldGroup.getUniqueId().equals(fg.getFieldGroupId()))
							.collect(Collectors.toList());
					// FGs w/o roles
					if (fgAssignedCustomRoles.isEmpty()) {
						accessControlledFieldGroups.add(fieldGroup);
					} else { // FGs with roles
						String roleIds = String.join(",", fgAssignedCustomRoles.stream().map(data -> data.getRoleId())
								.collect(Collectors.toSet()));
						List<CMDNeoRole> userAssignedFGCustomRoles = userAssignedCustomRoles.stream()
								.filter(role -> roleIds.contains(role.getUniqueId())).collect(Collectors.toList());
						if (!userAssignedFGCustomRoles.isEmpty()) {
							fieldGroup.setReadOnly(!userAssignedFGCustomRoles.stream()
									.filter(role -> customFieldEditQualifier.equals(role.getRoleQualifier()))
									.findFirst().isPresent());
							accessControlledFieldGroups.add(fieldGroup);
						}
					}
				}
			}

		} else {
			// For Community/Company Admin - All the field groups are visible
			String[] permissions = neoRestClient.getPermissions(userInfo.getCompanyId(), userInfo.getUserId(),
					userInfo.getServiceInstanceID());
			if (null != permissions && Arrays.stream(permissions).anyMatch(ADMIN_PERMISSION::equals)) {
				accessControlledFieldGroups = allFieldGroups;
			} else { // For Non Admins - Only FGs w/o roles are visible
				for (CustomDataFieldGroupType fieldGroup : allFieldGroups) {
					List<CMDCustomFieldGroupRole> fgAssignedCustomRoles = allCustomRoles.stream()
							.filter(fg -> fieldGroup.getUniqueId().equals(fg.getFieldGroupId()))
							.collect(Collectors.toList());
					// FGs w/o roles
					if (fgAssignedCustomRoles.isEmpty()) {
						accessControlledFieldGroups.add(fieldGroup);
					}
				}
			}
		}

		return accessControlledFieldGroups;

	}

	@Override
	public List<CustomFieldAnswersResponse> updatePartnerFieldAnswerData(String partnerBuId, UserInfo userInfo,
			List<CustomDataSave> answerDataList) throws TGOCPRestException {
		ObjectMapper mapper = new ObjectMapper();
		List<CustomFieldAnswersResponse> response = new ArrayList<>();
		try {
			for (CustomDataSave answerData : answerDataList) {
				List<ImCustomFieldAnswerMap> qstnAswrs = new ArrayList<>();
				for (Entry<String, ?> entrySet : answerData.getValue().entrySet()) {
					ImCustomFieldAnswerMap data = new ImCustomFieldAnswerMap();
					data.setQuestionId(entrySet.getKey());
					if (null == entrySet.getValue()) {
						data.setFileObjects(new FileObject[] {});
						data.setAnswers(new String[0]);
					} else if (entrySet.getValue() instanceof String) {
						data.setFileObjects(new FileObject[] {});
						String value = (String) entrySet.getValue();
						data.setAnswers(StringUtils.isEmpty(value) ? new String[0] : new String[] { value });// value.split(",")
					} else if (entrySet.getValue() instanceof ArrayList<?>) {
						List<?> list = (ArrayList<?>) entrySet.getValue();
						if (!list.isEmpty()) {
							if (list.get(0) instanceof String) {
								data.setFileObjects(new FileObject[] {});
								data.setAnswers(((ArrayList<String>) list).stream().toArray(String[]::new));
							} else {
								data.setAnswers(new String[0]);
								FileObject[] files = new FileObject[list.size()];
								for (int index = 0; index < files.length; index++) {
									FileObject fileObject = mapper.convertValue(list.get(index), FileObject.class);
									files[index] = fileObject;
								}

								data.setFileObjects(files);
							}
						} else {
							data.setFileObjects(new FileObject[] {});
							data.setAnswers(new String[0]);
						}
					}
					qstnAswrs.add(data);
				}
				Map<String, List<ImCustomFieldAnswerMap>> answerMap = new HashMap<>();
				answerMap.put("answerMap", qstnAswrs);

				response.add(customDataClient.updateFieldAnswerMap(answerData.getGroupId(), partnerBuId, userInfo,
						answerMap));
			}
		} catch (Exception e) {
			throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_DATA_SAVE_FAIL_400", e.getMessage(),
					CommonUtil.getStackTrace(e));

		}
		return response;
	}
	
	@Override
	public List<CustomFieldAnswersResponse> updateCustomFieldAnswerData(String partnerBuId, UserInfo userInfo,
			List<ImportCustomDataSave> answerDataList) throws TGOCPRestException {
		ObjectMapper mapper = new ObjectMapper();
		List<CustomFieldAnswersResponse> response = new ArrayList<>();
		try {
			for (ImportCustomDataSave answerData : answerDataList) {
				List<ImCustomFieldAnswerMap> qstnAswrs = new ArrayList<>();
				for (Entry<String, ?> entrySet : answerData.getValue().entrySet()) {
					ImCustomFieldAnswerMap data = new ImCustomFieldAnswerMap();
					data.setQuestionId(entrySet.getKey());
					if (null == entrySet.getValue()) {
						data.setFileObjects(new FileObject[] {});
						data.setAnswers(new String[0]);
					} else if(BulkUploadConstants.BLANK_VALUE.equals(entrySet.getValue())){
						//data.setFileObjects(new FileObject[] {});
						data.setAnswers(null);
					} else if (entrySet.getValue() instanceof String) {
						data.setFileObjects(new FileObject[] {});
						String value = (String) entrySet.getValue();
						data.setAnswers(StringUtils.isEmpty(value) ? new String[0] : new String[] { value });// value.split(",")
					} else if (entrySet.getValue() instanceof ArrayList<?>) {
						List<?> list = (ArrayList<?>) entrySet.getValue();
						if (!list.isEmpty()) {
							if (list.get(0) instanceof String) {
								data.setFileObjects(new FileObject[] {});
								data.setAnswers(((ArrayList<String>) list).stream().toArray(String[]::new));
							} else {
								data.setAnswers(new String[0]);
								FileObject[] files = new FileObject[list.size()];
								for (int index = 0; index < files.length; index++) {
									FileObject fileObject = mapper.convertValue(list.get(index), FileObject.class);
									files[index] = fileObject;
								}

								data.setFileObjects(files);
							}
						} else {
							data.setFileObjects(new FileObject[] {});
							data.setAnswers(new String[0]);
						}
					}
					qstnAswrs.add(data);
				}
				Map<String, List<ImCustomFieldAnswerMap>> answerMap = new HashMap<>();
				answerMap.put("answerMap", qstnAswrs);

				response.add(customDataClient.updateFieldAnswerMap(answerData.getGroupId(), partnerBuId, userInfo,
						answerMap));
			}
		} catch (Exception e) {
			throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "CUSTOM_DATA_SAVE_FAIL_400", e.getMessage(),
					CommonUtil.getStackTrace(e));

		}
		return response;
	}

}
