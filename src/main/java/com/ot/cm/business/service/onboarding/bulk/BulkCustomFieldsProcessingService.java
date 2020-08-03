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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ot.cm.business.service.CustomDataService;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkImportHelper;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.BusinessPartnerRestClient;
import com.ot.cm.rest.client.CustomFieldGroupsClient;
import com.ot.cm.rest.client.CustomFieldRestClient;
import com.ot.cm.rest.client.TradingPartnerRestClient;
import com.ot.cm.rest.request.entity.BulkImportData;
import com.ot.cm.rest.request.entity.BusinessPartnerFacades;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.CustomFieldChoice;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.response.entity.ImportCustomDataSave;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.Field;
import com.ot.cm.vo.bulk.FileMetaData;

/**
 * @author ssen
 *
 */
@Service
public class BulkCustomFieldsProcessingService extends BulkImportProcessingService {

	@Autowired
	private CustomFieldGroupsClient customFieldGroupsClient;

	@Autowired
	private CustomFieldRestClient customFieldRestClient;

	@Autowired
	private BulkImportHelper bulkImportHelper;
	
	@Autowired
	LocalizationMessages messageSource;
	
	@Autowired
	TradingPartnerRestClient tpRestClient;
	
	@Autowired
	private BusinessPartnerRestClient bpClient;
	
	@Autowired
	private CustomDataService customDataService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(BulkCustomFieldsProcessingService.class);
	private static final String KEY = "maintainOrder";
	// Map of json value
	private volatile Map<String, Field> fieldsAsMap = new HashMap<>();

	private static final Field[] standardExportColumns = { new Field("BUID", true), new Field("Display Name", false),
			new Field("Company ID", false) };

	private static final String[] allowedCustomFieldTypes = { "Text", "Number", "Multiple Choice", "Drop-down",
			"Radio Button", "Text Area", "Date" };

	@Override
	List<Field> generateColumns(String templateName, Long workflowId, UserInfo user) throws TGOCPRestException {
		return getColumns(user);
	}

	/**
	 * This method retrieves all the Export columns
	 * 
	 * @param user
	 * @return
	 * @throws TGOCPRestException
	 */
	private List<Field> getColumns(UserInfo user) throws TGOCPRestException {

		List<Field> columns = new ArrayList<>();
		columns.addAll(Arrays.asList(standardExportColumns));
		columns.addAll(getCustomFields(user).stream().map(Field::new).collect(Collectors.toList()));

		return columns;
	}

	/**
	 * This method retrieves all the custom fields under given Company
	 * configured custom field groups
	 * 
	 * @param user
	 * @return
	 * @throws TGOCPRestException
	 */
	private List<String> getCustomFields(UserInfo user) throws TGOCPRestException {

		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("businessUnitId", user.getCompanyId());

		Map<String, String> allCustomFieldGroups = Arrays.asList(customFieldGroupsClient.list(queryParams, user))
				.stream().collect(Collectors.toMap(CustomFieldGroupType::getUniqueId, CustomFieldGroupType::getName));

		List<String> customFields = Arrays
				.asList(customFieldRestClient.getCustomFieldList(
						allCustomFieldGroups.keySet().toArray(new String[allCustomFieldGroups.keySet().size()]), user,
						allowedCustomFieldTypes, null))
				// Mapping group name - custom Field Id instead of group name - Field name
				.stream().map(field -> allCustomFieldGroups.get(field.getGroupId()) + "--" + field.getCustomFieldId())
				.collect(Collectors.toList());

		return customFields;
	}

	@Override
	public FileMetaData validateFile(MultipartFile uploadedFile, FileMetaData fileMetaData, Long workflowId,
			UserInfo userInfo) throws CMApplicationException, TGOCPRestException {

		fileMetaData = bulkImportHelper.validateFile(uploadedFile, Arrays.asList(standardExportColumns),
				userInfo.getLocale());

		return fileMetaData;
	}

	@Override
	List<Map<String, String>> processData(InputStream fileStream, FileMetaData metadata, UserInfo user, Long workflowId)
			throws CMApplicationException, TGOCPRestException, IllegalAccessException, IOException, ParseException {
		return validCustomFieldsExcelFile(fileStream, metadata, user);
	}

	@Override
	public Map<String, String> processArow(BulkImportData bulkrowData, UserInfo userInfo) {
		
		return checkExistanceAndUpdateFields(bulkrowData.getRowData(), userInfo);
	}
	
	
	public List<Map<String, String>> validCustomFieldsExcelFile(InputStream is, FileMetaData fileMetaInfo, UserInfo user) throws IOException, IllegalAccessException, TGOCPRestException {

		JSONParser jsonParser = new JSONParser();
		List<Field> templateFields = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file = new File(getClass().getClassLoader().getResource("json\\CustomImport-fields.json").getFile());
			String reader = FileUtils.readFileToString(file);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonObject.get("fields");
            String json =  jsonArray.toString();
            templateFields = mapper.readValue(json, new TypeReference<List<Field>>(){});
		}catch (FileNotFoundException e) {
			logger.error("FileNotFoundException :" + e);
        }catch(Exception e) {
        	logger.error("Exception :" + e);
		}
		String hashKey = DigestUtils
				.md5Hex(String.join("-", fileMetaInfo.getHeaderColumnDisplayNames()) + KEY + fileMetaInfo.getRows());
		if (!hashKey.equals(fileMetaInfo.getValidationCode())) {
			throw new IllegalAccessException(
					messageSource.getMessage("BULK_ONBOARD_EXCEL_VAlIDATION_FAILURE", user.getLocale()));
		}
		
		List<String> groupIds = getCustomFieldGroupIds(user.getCompanyId());
		List<ImportCustomDataSave> customDataSaves = new ArrayList<>();
		List<String> grpIds = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(is)) {

			Sheet sheet = workbook.getSheetAt(0);
			// list of column keys
			List<String> keyList = new ArrayList<>();

			List<Map<String, String>> processedData = new ArrayList<>();
			// Header Row
			int firstRowNo = sheet.getFirstRowNum();
			Row headerRow = sheet.getRow(firstRowNo);
			int columnsPerRow = headerRow.getLastCellNum();
			// Read non-header columns
			for (Row row : sheet) {
				if (row.getRowNum() > firstRowNo) {
					Map<String, String> thisRowDataMap = new HashMap<>();
					String remarks = null;
					String customFieldRemarks = null;
					String status = BulkUploadConstants.STATUS_VALID;
					List<String> remarksList = new ArrayList<>();
					
					for (int i = 0; i < columnsPerRow; i++) {
						boolean inValidated = false;	
						String[] headerColumnData = fileMetaInfo.getHeaderColumnDisplayNames().get(i).split("--");
						String headerColumnName = (headerColumnData.length > 1) ? headerColumnData[1] : fileMetaInfo.getHeaderColumnDisplayNames().get(i); // key
						
						

						Cell cell = row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL);
						String value = BulkImportHelper.getCellValueAsString(cell);
						
						
						if (headerColumnName.endsWith(BulkUploadConstants.MANDATORY_MARK)) {
							headerColumnName = headerColumnName.substring(0, headerColumnName.length() - 1);
						}
						
						if (null != value) {
							cell.setCellType(CellType.STRING);
						}
						
						if(headerColumnData.length > 1 && null != value) {
							
							customFieldRemarks = customFieldValidation(headerColumnName,value.trim(),user,groupIds,customDataSaves,grpIds,templateFields);
						}
						

						Field cellValidationRule = bulkImportHelper.getCellFieldRule(templateFields, headerColumnName);
						String key = cellValidationRule.getKey() == null ? headerColumnName
								: cellValidationRule.getKey();
						if (processedData.isEmpty()) {
							keyList.add(key);
						}
						
						
						if(headerColumnData.length > 1) {
							thisRowDataMap.put("CustomData", mapper.writeValueAsString(customDataSaves));
						}else {
							thisRowDataMap.put(key, value);
						}
						
						
						// check for other cell
						if (!inValidated) {
							remarks = bulkImportHelper.doValidateData(cellValidationRule, value);
							if(remarks == null && headerColumnName.equals("BUID")) {
								remarks = checkBPRExistforBuId(value.trim(),user);
							}
							if (remarks != null || customFieldRemarks != null) {
								String rm = remarks != null ? remarks : customFieldRemarks;
								remarksList.add(rm);
								inValidated = true;
								status = BulkUploadConstants.STATUS_REJECT;
							}
						}

					}
				
					thisRowDataMap.put(BulkUploadConstants.ROW_STATUS_HEADER, status);
					thisRowDataMap.put(BulkUploadConstants.ROW_REMARKS_HEADER, remarks);
					if(remarksList.size() > 0) {
						thisRowDataMap.put(BulkUploadConstants.ROW_REMARKS_HEADER, remarksList.toString().replace("[", "").replace("]", ""));	
					}
					processedData.add(thisRowDataMap);
				}
			}
			keyList.add(BulkUploadConstants.ROW_STATUS_HEADER);
			keyList.add(BulkUploadConstants.ROW_REMARKS_HEADER);
			fileMetaInfo.setHeaderKeys(keyList);
			return processedData;
		} finally {
			fieldsAsMap = new HashMap<>();
		}
	}
	

	private String checkBPRExistforBuId(String value, UserInfo user) throws TGOCPRestException {

		boolean isBPRExist = (tpRestClient.bprCount(value, user.getCompanyId(), user).getCount() > 0
				|| tpRestClient.bprCount(user.getCompanyId(), value, user).getCount() > 0) ? true : false;
		
		if(isBPRExist) {
			return null;
		}
		return "BPR does not exist";
	}

	private List<String> getCustomFieldGroupIds(String companyId) throws TGOCPRestException {
		CustomFieldGroupType[] customFieldGroupTypes = customFieldRestClient.getCustomFieldGroupsBasedonBuid(companyId);
		List<CustomFieldGroupType> custFieldGroupTypes = Arrays.asList(customFieldGroupTypes);
		return custFieldGroupTypes.stream().map(r -> r.getUniqueId()).collect(Collectors.toList());
	}


	private String customFieldValidation(String headerColumnName, String columnValue, UserInfo user, List<String> groupIds, List<ImportCustomDataSave> customDataSaves, List<String> grpIds, List<Field> templateFields) throws JsonParseException, JsonMappingException, IOException, TGOCPRestException {
		CustomFieldEntity[] customFieldEntity = null;
		CustomFieldEntity entity = new CustomFieldEntity();
		String remarks = null;
		String groupIdsList = null;
		CustomFieldChoice customFieldChoice = null;
		if(groupIds.size() > 0) {
			groupIdsList = groupIds.toString().replace("[", "").replace("]", "").replaceAll(", ", ",");
		}
			if(headerColumnName.contains(",_")) {
				int i = headerColumnName.lastIndexOf(",_");
				String searchValue = "";
				searchValue = "*"+headerColumnName.substring(i+1, headerColumnName.length());
				customFieldEntity = customFieldRestClient.getCustomFieldInfoBasedId(searchValue, user,groupIdsList);
				List<CustomFieldEntity> filteredList = new ArrayList<>();
				filteredList = Arrays.asList(customFieldEntity).stream()
													.filter(p -> p.getCustomFieldId().equals(headerColumnName))
													.collect(Collectors.toList());
				if(filteredList.size() > 0) {
					entity = filteredList.get(0);
				}
			}else {
				customFieldEntity = customFieldRestClient.getCustomFieldInfoBasedId(headerColumnName, user,groupIdsList);
				if(customFieldEntity.length > 0) {
					entity = customFieldEntity[0];
				}
			}
			if(entity != null) {
				switch(entity.getCustomFieldType()) {
					case "Multichoice":
						String[] strArray = columnValue.split(",");
						List<CustomFieldChoice> customFieldChoices = new ArrayList<>();
						StringBuilder sb = new StringBuilder("");
						for(int i=0;i< strArray.length;i++) {
							customFieldChoice = fieldValidation(headerColumnName,strArray[i].trim(),entity);
							customFieldChoices.add(customFieldChoice);
							sb.append(customFieldChoice.getId()+",");
						}
						columnValue = sb.toString().substring(0,sb.length()-1);
						remarks = customFieldChoices.size() != strArray.length ? ""+headerColumnName+" value not present in choices" : null;
						break;
					case "Drop-down":
						customFieldChoice = fieldValidation(headerColumnName,columnValue,entity); 
						remarks = customFieldChoice == null ? ""+headerColumnName+" value not present in choices" : null;
						break;
					case "Radio Button":
						customFieldChoice = fieldValidation(headerColumnName,columnValue,entity); 
						remarks = customFieldChoice == null ? ""+headerColumnName+" value not present in choices" : null;
						break;
					default:
						Field cellValidationRule = bulkImportHelper.getCellFieldRule(templateFields, entity.getCustomFieldType());
						remarks = bulkImportHelper.doValidateData(cellValidationRule, columnValue.trim());
						break;
				}
			}else {
				remarks = "Currently "+headerColumnName+" is not configured";
			}
			
		createCustomFieldsData(entity,columnValue,customDataSaves,grpIds,customFieldChoice);
		return remarks;
	}

	private void createCustomFieldsData(CustomFieldEntity customFieldEntity, String columnValue, List<ImportCustomDataSave> customDataSaves, List<String> grpIds, CustomFieldChoice customFieldChoice) {

		columnValue = columnValue.equalsIgnoreCase(BulkUploadConstants.BLANK_VALUE) ? BulkUploadConstants.BLANK_VALUE : columnValue;
		String answer = customFieldChoice != null ? customFieldChoice.getId() : columnValue;
		if(grpIds.contains(customFieldEntity.getGroupId())){
			int i = grpIds.indexOf(customFieldEntity.getGroupId());
			ImportCustomDataSave importCustomDataSave= customDataSaves.get(i);
			importCustomDataSave.getValue().put(customFieldEntity.getUniqueId(), answer);
		}else {
			grpIds.add(customFieldEntity.getGroupId());
			ImportCustomDataSave importCustomDataSave = new ImportCustomDataSave();
			importCustomDataSave.setGroupId(customFieldEntity.getGroupId());
			Map<String,String> map = new HashMap<>();
			map.put(customFieldEntity.getUniqueId(),answer);
			importCustomDataSave.setValue(map);
			customDataSaves.add(importCustomDataSave);
		}
	}

	private CustomFieldChoice fieldValidation(String headerColumnName,String columnValue, CustomFieldEntity entity) throws JsonParseException, JsonMappingException, IOException {
		List<CustomFieldChoice> customFieldChoices = Arrays.asList(entity.getChoices());
		
		return customFieldChoices.stream().filter(p -> p.getChoiceIdentifier().equals(columnValue)).findAny().orElse(null);
		
	}

	public Map<String, String> checkExistanceAndUpdateFields(@NonNull Map<String, String> rowData, UserInfo userInfo) {
		
		if (!rowData.get(BulkUploadConstants.ROW_STATUS_HEADER).equals(BulkUploadConstants.STATUS_VALID))
			return rowData;
		
		try {
			String targetBuid = rowData.get("BUID");
			String displayName = rowData.get("Display Name");
			String companyId = rowData.get("Company ID");
			if(null != displayName || null != companyId) {
				updateFacadeFields(displayName,companyId,targetBuid.trim(),userInfo);	
			}
			
			String customFieldsData = rowData.get("CustomData");
			if(null != customFieldsData) {
				ObjectMapper mapper = new ObjectMapper();
				List<ImportCustomDataSave> answerData = mapper.readValue(customFieldsData, new TypeReference<List<ImportCustomDataSave>>(){});
				customDataService.updateCustomFieldAnswerData(targetBuid.trim(), userInfo, answerData);
			}
			
			rowData.put(BulkUploadConstants.ROW_STATUS_HEADER, BulkUploadConstants.STATUS_SUCCESS);
		}catch (Exception e) {
			rowData.put(BulkUploadConstants.ROW_STATUS_HEADER, BulkUploadConstants.STATUS_FAILED);
			rowData.put(BulkUploadConstants.ROW_REMARKS_HEADER, e.getLocalizedMessage());
		}

		return rowData;
	}

	
	private CMDSuccessResponse updateFacadeFields(String displayName, String companyId, String targetBuid, UserInfo userInfo) throws TGOCPRestException {
		String ownerBuId = userInfo.getCompanyId();
		BusinessPartnerFacades fc = getIfFacadeExists(ownerBuId, targetBuid, userInfo);
		String facadeId = null;
		if (fc != null) {
			facadeId = fc.getUniqueId();
			if(null != displayName) {
				displayName = displayName.trim();
				if (!BulkUploadConstants.BLANK_VALUE.equals(displayName))
					fc.setTargetBuCustomName(displayName);
				else if (BulkUploadConstants.BLANK_VALUE.equals(displayName))
					fc.setTargetBuCustomName(null);
			}
			if(null != companyId) {
				companyId = companyId.trim();
				if (!BulkUploadConstants.BLANK_VALUE.equals(companyId))
					fc.setTargetBuCustomId(companyId);
				else if (BulkUploadConstants.BLANK_VALUE.equals(companyId))
					fc.setTargetBuCustomId(null);
			}
		}
		if (null != facadeId) {
				return bpClient.editFacades(fc, facadeId, userInfo);
		}
		return bpClient.createFacades(fc, userInfo);
	}

	
	private BusinessPartnerFacades getIfFacadeExists(String ownerBuId, String targetBuId, UserInfo userInfo) throws TGOCPRestException {
		BusinessPartnerFacades[] facadeList = bpClient.getFacades(ownerBuId, targetBuId, userInfo);
		if (facadeList.length > 0) {
			return facadeList[0];
		}

		return null;
	}

}
