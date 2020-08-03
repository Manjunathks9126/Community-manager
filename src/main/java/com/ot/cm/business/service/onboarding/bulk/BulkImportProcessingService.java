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
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ot.cm.business.dao.BulkImportDao;
import com.ot.cm.business.service.UserService;
import com.ot.cm.constants.BulkImportType;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkImportHelper;
import com.ot.cm.jpa.entity.BulkImport;
import com.ot.cm.rest.request.entity.BulkImportData;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.JacksonWrapper;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.Field;
import com.ot.cm.vo.bulk.FileMetaData;

/**
 * @author ssen
 *
 */
public abstract class BulkImportProcessingService {

	@Autowired
	BulkImportHelper bulkInivitationHelper;

	@Autowired
	private JacksonWrapper mapper;

	@Autowired
	private BulkImportDao bulkImportDao;

	@Autowired
	private UserService userService;

	@Autowired
	private JmsTemplate jmsTemplate;

	private static final Logger logger = LoggerFactory.getLogger(BulkImportProcessingService.class);

	/**
	 * 
	 * @param templateName
	 * @param workflowId
	 * @param user
	 * @return
	 * @throws TGOCPRestException
	 */
	abstract List<Field> generateColumns(String templateName, Long workflowId, UserInfo user) throws TGOCPRestException;

	public Resource download(String templateName, Long workflowId, UserInfo user)
			throws TGOCPRestException, MalformedURLException {
		Path filePath = bulkInivitationHelper.generateTemplateWithColumns(templateName,
				generateColumns(templateName, workflowId, user));
		Resource resource = new UrlResource(filePath.toUri());
		if (resource.exists() || resource.isReadable()) {
			return resource;
		} else {
			throw new TGOCPRestException(HttpStatus.NO_CONTENT, "TGOCPREST-2040", "BULK_ONBOARD_TEMPLATE_NO_CONTENT",
					"Template Not Available", null);
		}
	}

	/**
	 * 
	 * @param uploadedFile
	 * @param fileMetaData
	 * @param workflowId
	 * @param userInfo
	 * @return
	 * @throws CMApplicationException
	 * @throws TGOCPRestException
	 */
	public abstract FileMetaData validateFile(MultipartFile uploadedFile, FileMetaData fileMetaData, Long workflowId,
			UserInfo userInfo) throws CMApplicationException, TGOCPRestException;

	/**
	 * 
	 * @param file
	 * @param invId
	 * @param metadata
	 * @param user
	 * @param workflowId
	 * @return
	 * @throws CMApplicationException
	 */
	public FileMetaData start(MultipartFile file, Long invId, FileMetaData metadata, UserInfo user, Long workflowId,
			BulkImportType importType) throws CMApplicationException {

		if (metadata.isValid()
				// Invitation code check is only required for Bulk Invite
				&& (!BulkImportType.INVITE.equals(importType) || !CommonUtil.isEmpty(metadata.getValidationCode()))) {
			try {
				BulkImport importData = new BulkImport();
				importData.setCallerApplication("CM");
				importData.setInvitationId(invId);
				importData.setFileName(file.getOriginalFilename());
				importData.setContext(importType.getValue());
				importData.setCompanyId(user.getCompanyId());
				importData.setStatus(BulkUploadConstants.STATUS_STAGED);
				importData.setCreatedBy(user.getUserId());
				importData.setCreatedTimeStamp(new Date());
				importData.setFileContent(file.getBytes());
				importData.setData(mapper.writeValueAsString(new ArrayList<>()));
				importData.setMetaInfo(mapper.writeValueAsString(metadata));

				BulkImport uploadedData = bulkImportDao.uploadBulkImportData(importData);
				metadata.setRequestId(uploadedData.getRequestId());
				startAsyncJob(file.getInputStream(), metadata, user, workflowId);
			} catch (IOException e) {
				throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "BULK_IMPORT_FILE_META_DATA", e);
			} catch (Exception e) {
				throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "BULK_IMPORT_FILE_META_SAVE_FAILURE",
						e);
			}
		}

		return metadata;
	}

	/**
	 * 
	 * @param fileStream
	 * @param metadata
	 * @param user
	 * @param workflowId
	 */
	public void startAsyncJob(InputStream fileStream, FileMetaData metadata, UserInfo user, Long workflowId) {
		CompletableFuture.runAsync(() -> {

			String emailId = null;
			try {
				emailId = userService.getUserInfo(user.getUserId()).getContactInformation().getEmail();
			} catch (TGOCPRestException e) {
				logger.error("Error while getting user details ", e);
			}

			List<Map<String, String>> processedData = null;
			String remarks = null;
			String status = null;
			try {
				processedData = processData(fileStream, metadata, user, workflowId);
				status = BulkUploadConstants.STATUS_VALID;

			} catch (Exception e) {
				status = BulkUploadConstants.STATUS_REJECT;
				remarks = e.getMessage().length() > 200 ? e.getMessage().substring(0, 200) : e.getMessage();
			}
			BulkImport bulkImportData = new BulkImport();
			bulkImportData.setRequestId(metadata.getRequestId());
			bulkImportData.setProcessEndTime(new Date());
			bulkImportData.setStatus(status);
			bulkImportData.setRemarks(remarks);
			bulkImportData.setUserEmailId(emailId);
			bulkImportData.setModifiedBy("SYSTEM_BULK_VALIDATOR");
			bulkImportData.setProcessStartTime(new Date());
			bulkImportData.setData(mapper.writeValueAsString(processedData));
			bulkImportData.setMetaInfo(mapper.writeValueAsString(metadata));
			bulkImportDao.updateBulkImportRequest(bulkImportData);
			jmsTemplate.convertAndSend(EnvironmentConstants.AMQ_BULK_IMPORT,
					mapper.writeValueAsString(metadata.getRequestId()));
		});
	}

	/**
	 * 
	 * @param uploadedFile
	 * @param fileMetaData
	 * @param workflowId
	 * @param userInfo
	 * @return
	 * @throws CMApplicationException
	 * @throws TGOCPRestException
	 * @throws ParseException 
	 */
	abstract List<Map<String, String>> processData(InputStream fileStream, FileMetaData metadata, UserInfo user,
			Long workflowId) throws CMApplicationException, TGOCPRestException, IllegalAccessException, IOException, ParseException;

	/**
	 * @param bulkrowData
	 * @param userInfo
	 * @return Map<String,String>
	 */
	public abstract Map<String, String> processArow(BulkImportData bulkrowData, UserInfo userInfo);

}
