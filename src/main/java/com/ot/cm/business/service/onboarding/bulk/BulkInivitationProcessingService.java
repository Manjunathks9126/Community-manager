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
 Mar 13, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service.onboarding.bulk;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkImportHelper;
import com.ot.cm.helper.BulkInivitationHelper;
import com.ot.cm.rest.request.entity.BulkImportData;
import com.ot.cm.tasks.WorkFlowJsonCreator;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.Field;
import com.ot.cm.vo.bulk.FileMetaData;
import com.ot.cm.vo.bulk.BulkImportTemplate;

/**
 * @author ssen
 *
 */
@Service
public class BulkInivitationProcessingService extends BulkImportProcessingService {

	@Autowired
	private BulkImportHelper bulkImportHelper;

	@Autowired
	private BulkInivitationHelper bulkInivitationHelper;

	@Autowired
	private OnboardingService boardingService;

	@Autowired
	private WorkFlowJsonCreator jsonCreator;

	@Override
	List<Field> generateColumns(String templateName, Long workflowId, UserInfo user) throws TGOCPRestException {
		String jsonSchema = boardingService.getOnboardingRequesterTaskSchema(workflowId, user.getLocale());
		return jsonCreator.readTaskJson(jsonSchema);
	}

	@Override
	public FileMetaData validateFile(MultipartFile uploadedFile, FileMetaData fileMetaData, Long workflowId,
			UserInfo userInfo) throws CMApplicationException, TGOCPRestException {

		if (fileMetaData == null || CommonUtil.isEmpty(fileMetaData.getValidationCode())) {
			String jsonSchema = boardingService.getOnboardingRequesterTaskSchema(workflowId, userInfo.getLocale());
			// get template rule from json
			BulkImportTemplate template = bulkInivitationHelper.readJsonSchema(jsonSchema);
			fileMetaData = bulkImportHelper.validateFile(uploadedFile, template.getFields(), userInfo.getLocale());
		}
		return fileMetaData;
	}

	@Override
	List<Map<String, String>> processData(InputStream fileStream, FileMetaData metadata, UserInfo user, Long workflowId)
			throws CMApplicationException, TGOCPRestException, IllegalAccessException, IOException {
		Map<String, Object> taskJsonMap = boardingService.getRequesterTaskSchemaMap(workflowId, user.getLocale());
		String jsonSchema = (String) taskJsonMap.get("jsonSchema");
		Long taskId = (Long) taskJsonMap.get("taskId");
		metadata.setTaskId(taskId);
		metadata.setWorkflowId(workflowId);
		return bulkImportHelper.readValidExcelFile(fileStream, metadata, jsonSchema, user);
	}

	@Override
	public Map<String, String> processArow(BulkImportData bulkrowData, UserInfo userInfo) {
		return bulkInivitationHelper.checkExistanceAndSendInvitationForAO(bulkrowData.getRowData(),
				bulkrowData.getInvitationMetaData(), userInfo, bulkrowData.getWorkflowId(), bulkrowData.getTaskId());
	}

}
