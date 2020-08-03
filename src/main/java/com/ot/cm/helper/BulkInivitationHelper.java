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
 Feb 11, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.helper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.rest.request.entity.CompanyDetailsEntity;
import com.ot.cm.rest.request.entity.ContactEntity;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.tasks.WorkFlowJsonCreator;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.BulkImportTemplate;

/**
 * @author ssen
 *
 */
@Service
public class BulkInivitationHelper {

	@Autowired
	private OnboardingService onboardingService;

	@Autowired
	private WorkFlowJsonCreator jsonCreator;
	
	private static final Logger logger = LoggerFactory.getLogger(BulkInivitationHelper.class);
	
	/**
	 * 
	 * @param fileName
	 * @return Map<String,FieldType>
	 */
	public BulkImportTemplate readJson(String fileName) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.debug("Reading json for object mapping " + fileName);
			return mapper.readValue(ResourceUtils.getFile("classpath:json/" + fileName), BulkImportTemplate.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new BulkImportTemplate();
	}

	public BulkImportTemplate readJsonSchema(String taskFieldsJson) {

		BulkImportTemplate template = new BulkImportTemplate();
		template.setFields(jsonCreator.readTaskJson(taskFieldsJson, true));

		return template;
	}

	// Consider only valid status one
	public Map<String, String> checkExistanceAndSendInvitationForAO(@NonNull Map<String, String> rowData,
			InvitationNeoRest invData, UserInfo userInfo, Long workflowId, Long taskId) {

		if (!rowData.get(BulkUploadConstants.ROW_STATUS_HEADER).equals(BulkUploadConstants.STATUS_VALID))
			return rowData;

		CompanyDetailsEntity companyDetailsEntity = new CompanyDetailsEntity();
		companyDetailsEntity.setCompanyName(rowData.get("companyInfo.companyName"));
		companyDetailsEntity.setCity(rowData.get("companyInfo.city"));
		String countryCode = rowData.get("companyInfo.country");

		companyDetailsEntity.setCountry(new Country(countryCode, null));
		companyDetailsEntity.setAddressLine1(rowData.get("companyInfo.addrLine1"));
		companyDetailsEntity.setAddressLine2(rowData.get("companyInfo.addrLine2"));
		companyDetailsEntity.setState(rowData.get("companyInfo.state"));
		companyDetailsEntity.setPostalcode(rowData.get("companyInfo.postalCode"));
		companyDetailsEntity.setPhone(rowData.get("companyInfo.companyPhone"));
		companyDetailsEntity.setEmail(rowData.get("companyInfo.companyEmail"));
		companyDetailsEntity.setCompanyId(rowData.get("companyInfo.companyId"));
		companyDetailsEntity.setCompanyWebsiteURL(rowData.get("companyInfo.website"));
		companyDetailsEntity
				.setContact(new ContactEntity(rowData.get("contactInfo.firstName"), rowData.get("contactInfo.lastName"),
						rowData.get("contactInfo.email"), rowData.get("contactInfo.phone")));
		try {
			OnboardingInvitationWrapper onboardingWrapper = new OnboardingInvitationWrapper();
			onboardingWrapper.setCompanyDetailsEntity(companyDetailsEntity);
			onboardingWrapper.setInvitationEntity(invData);
			onboardingWrapper.setTaskId(taskId);
			onboardingWrapper.setWorkflowId(workflowId);
			onboardingWrapper.setRequestorTask(jsonCreator.getJsonSchema(rowData));

			onboardingService.invite(onboardingWrapper, userInfo);
			rowData.put(BulkUploadConstants.ROW_STATUS_HEADER, BulkUploadConstants.STATUS_SUCCESS);

		} catch (CMApplicationException b) {
			rowData.put(BulkUploadConstants.ROW_STATUS_HEADER, BulkUploadConstants.STATUS_FAILED);
			rowData.put(BulkUploadConstants.ROW_REMARKS_HEADER, b.getErrorLog());
		} catch (Exception e) {
			rowData.put(BulkUploadConstants.ROW_STATUS_HEADER, BulkUploadConstants.STATUS_FAILED);
			rowData.put(BulkUploadConstants.ROW_REMARKS_HEADER, e.getLocalizedMessage());
		}

		return rowData;
	}

}
