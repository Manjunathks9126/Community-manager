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
 Feb 1, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.CustomFieldRestClient;
import com.ot.cm.rest.client.TradingPartnerRestClient;
import com.ot.cm.rest.response.entity.CustomFieldChoice;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.response.entity.ImportCustomDataSave;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.cm.vo.bulk.BulkImportTemplate;
import com.ot.cm.vo.bulk.Field;
import com.ot.cm.vo.bulk.FileMetaData;
import com.ot.cm.vo.bulk.ValidationType;

/**
 * @author ssen
 *
 */
@Component
public class BulkImportHelper {

	@Autowired
	LocalizationMessages messageSource;

	@Autowired
	private BulkInivitationHelper bulkInivitationHelper;
	
	@Autowired
	private CustomFieldRestClient customFieldRestClient;
	
	
	@Autowired
	TradingPartnerRestClient tpRestClient;

	private static final Logger logger = LoggerFactory.getLogger(BulkImportHelper.class);
	private static final String KEY = "maintainOrder";
	// Map of json value
	private volatile Map<String, Field> fieldsAsMap = new HashMap<>();

	public void createExcelTemplate(List<Field> columnData, String templateName) {
		createExcelTemplate(columnData, templateName, "Sheet1");
	}

	public void createExcelTemplate(List<Field> columnData, String templateName, @NonNull String sheetName) {
		if (!templateName.endsWith(BulkUploadConstants.EXCEL_EXT)) {
			templateName = templateName + BulkUploadConstants.EXCEL_EXT;
		}
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(sheetName);
			CellStyle style = workbook.createCellStyle();
		    Font font = workbook.createFont();
		    
			boolean isExportCustomFields = false;
			if(null != templateName && templateName.equals("Import_Template.xlsx")) {
				String[] instructions = {"Please delete these instructions before importing the file!",
						"Instructions: This template provides all the possible data elements that you can update in your community (Display Names and custom fields). Please populate each row with the BUID (required) and the applicable custom fields for all partners that you wish to update, then save as a spreadsheet to your computer.  Note, you can obtain a list of all your partners (their BUIDs and Company Names) via the Export process.",
						"IMPORTANT! This functionality uses the following logic:  Value(s) in the cell -> Updates the field | The word 'BLANK_VALUE' in the cell -> Removes existing field value(s) | Empty cell or missing custom field column -> No changes to field"};
				Row row = null;
				for(int i=0;i<3;i++) {
					row = sheet.createRow(i);
					Cell cell = row.createCell(0);
					cell.setCellValue(instructions[i]);
				}
				font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
				style.setFont(font);
				sheet.getRow(0).getCell(0).setCellStyle(style);
				
				
				
				isExportCustomFields = true;
			}
			
			
			Row row = isExportCustomFields == true ? sheet.createRow(3): sheet.createRow(0);
			
			AtomicInteger atomicInt = new AtomicInteger(0);
			
			columnData.stream().forEach(e -> {
				Cell cell = row.createCell(atomicInt.getAndIncrement());
				if (e.isRequired()) {
					cell.setCellValue(e.getLabel() + BulkUploadConstants.MANDATORY_MARK);
				} else {
					cell.setCellValue(e.getLabel());
				}
				
			});
			
			if(isExportCustomFields) {
			    font = workbook.createFont();
			    font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
			    font.setBold(true);
			    CellStyle styleIns = workbook.createCellStyle();
			    styleIns.setFont(font);
			    for(int i = 0; i < row.getLastCellNum(); i++){
			        row.getCell(i).setCellStyle(styleIns);
			    }
			    
			    XSSFRichTextString instruction = new XSSFRichTextString(sheet.getRow(1).getCell(0).toString());
			    instruction.applyFont(0,12,font);
				sheet.getRow(1).getCell(0).setCellValue(instruction);
				
				XSSFRichTextString impString = new XSSFRichTextString(sheet.getRow(2).getCell(0).toString());
				impString.applyFont(0,9,font);
				sheet.getRow(2).getCell(0).setCellValue(impString);
			    
			}
		    
			FileOutputStream out = new FileOutputStream(
					new File(getFolderPath(BulkUploadConstants.TEMP_LOCATION) + templateName));
			workbook.write(out);
			out.close();
			logger.info("Excel written successfully.." + templateName);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException :" + e);
		} catch (IOException e) {
			logger.error("IOException :" + e);
		}
	}

	/**
	 * 
	 * @param directoryName
	 * @return String
	 */
	private String getFolderPath(final String directoryName) {

		final File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory.getAbsolutePath() + BulkUploadConstants.SEPARATOR;
	}

	/**
	 * 
	 * @param file
	 * @param templateFields
	 * @param locale
	 * @return
	 * @throws CMApplicationException
	 */
	public FileMetaData validateFile(MultipartFile file, List<Field> templateFields, Locale locale)
			throws CMApplicationException {
		FileMetaData excelMetaData = new FileMetaData();
		Workbook workbook = null;
		try {
			workbook = createWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);
			int firstRowNo = sheet.getFirstRowNum();
			int rowCount = (sheet.getLastRowNum() - firstRowNo);

			if (rowCount < 1) {
				throw new IllegalArgumentException("File contains no data");
			}

			if (rowCount > BulkUploadConstants.BULK_IMPORT_LIMIT) {
				throw new IllegalArgumentException(
						messageSource.getMessage("BULK_ONBOARD_EXCEL_FILE_VALIDATION_FAILURE", locale)
								+ BulkUploadConstants.BULK_IMPORT_LIMIT);
			}

			excelMetaData.setRows(rowCount);
			// Header Row
			Row headerRow = sheet.getRow(firstRowNo);
			// get header column display names list
			List<String> excelHeaderColNames = getHeaderCellNames(headerRow);
			excelMetaData.setColumns(excelHeaderColNames.size());

			// Mandatory Header validation
			checkForMandatoryHeader(templateFields, excelHeaderColNames);

			// add status and remarks field
			if (!excelHeaderColNames.contains(BulkUploadConstants.ROW_STATUS_HEADER))
				excelHeaderColNames.add(BulkUploadConstants.ROW_STATUS_HEADER);
			if (!excelHeaderColNames.contains(BulkUploadConstants.ROW_REMARKS_HEADER))
				excelHeaderColNames.add(BulkUploadConstants.ROW_REMARKS_HEADER);

			excelMetaData.setHeaderColumnDisplayNames(excelHeaderColNames);
			excelMetaData.setValid(true);

			String hashKey = DigestUtils.md5Hex(String.join("-", excelHeaderColNames) + KEY + rowCount);
			excelMetaData.setValidationCode(hashKey);

		} catch (EncryptedDocumentException | IOException e) {
			throw new CMApplicationException("TGOCP-5000", "BULK_ONBOARD_EXCEL_DOC_VALIDATION_FAILURE", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
				} catch (IOException e) {
					logger.error("Error while closing workbook");
				}
		}
		return excelMetaData;

	}

	/**
	 * @param templateFields
	 * @param excelHeaderColNames
	 *            void
	 */
	private void checkForMandatoryHeader(List<Field> templateFields, List<String> excelHeaderColNames) {
		for (Field field : templateFields) {
			if (field.isRequired() && !(excelHeaderColNames.contains(field.getLabel())
					|| excelHeaderColNames.contains(field.getLabel() + BulkUploadConstants.MANDATORY_MARK))) {
				throw new IllegalArgumentException("Mandatory header column '" + field.getLabel() + "' is missing.");
			}
		}

	}

	/**
	 * 
	 * @param file
	 * @return Workbook
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 * 
	 */
	private Workbook createWorkbook(MultipartFile file) throws EncryptedDocumentException, IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Uploaded empty file " + fileName);
		}
		if (fileName.endsWith(".xls") || fileName.endsWith(BulkUploadConstants.EXCEL_EXT)) {

			return WorkbookFactory.create(file.getInputStream());

		} else {
			throw new IllegalArgumentException("Uploaded file does not have a valid extension");
		}
	}

	/**
	 * 
	 * @param headerRow
	 * @return List<String>
	 */
	private List<String> getHeaderCellNames(Row headerRow) {
		List<String> excelHeaderColNames = new ArrayList<>();
		for (Cell headerCell : headerRow) {
			if (isCellEmpty(headerCell)) {
				throw new IllegalArgumentException("Null column name not accepted in header");
			}
			if (excelHeaderColNames.contains(headerCell.toString())
					|| excelHeaderColNames.contains(headerCell.toString() + BulkUploadConstants.MANDATORY_MARK)) {
				throw new IllegalArgumentException("Duplicate columns found");
			}
			excelHeaderColNames.add(headerCell.toString());
		}
		return excelHeaderColNames;
	}

	/**
	 * 
	 * @param cell
	 * @return boolean
	 */
	public static boolean isCellEmpty(final Cell cell) {
		if (cell == null) {
			return true;
		}

		if (cell.getCellType() == CellType.BLANK) {
			return true;
		}

		return (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
	}

	/**
	 * 
	 * @param cell
	 * @return String
	 */
	public static String getCellValue(final Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		}
		if (cell.getCellType() == CellType.STRING) {
			String cellValue = cell.getStringCellValue().trim();
			return cellValue.isEmpty() ? null : cellValue;
		}

		return null;
	}
	
	/**
	 * 
	 * @param cell
	 * @return String
	 */
	public static String getCustomCellValue(final Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		}
		if (cell.getCellType() == CellType.STRING) {
			String cellValue = cell.getStringCellValue().trim();
			return cellValue.isEmpty() ? null : cellValue;
		}

		return null;
	}

	/**
	 * 
	 * @param field
	 * @param value
	 * @return String
	 */
	public String doValidateData(Field field, String value) {
		if (field == null || field.getLabel() == null)
			return null;
		boolean isEmpty = CommonUtil.isEmpty(value);
		StringBuilder remarksMessage = new StringBuilder("Invalid ").append(field.getLabel()).append(": ");
		if (field.isRequired() && isEmpty)
			return "Missing mandatory field - " + field.getLabel();
		
		if ((field.isRequired()) && value.equals(BulkUploadConstants.BLANK_VALUE))	
			return ""+field.getKey()+" cannot be updated to Blank value" + field.getLabel();

		if (!isEmpty) {
			Map<ValidationType, Object> validations = field.getValidationRules();

			for (Entry<ValidationType, Object> entrySet : validations.entrySet()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Checking validation of '" + field.getLabel() + "' for '" + value + "' against "
							+ entrySet);
				}
				boolean valid = entrySet.getKey().validate(value, entrySet.getValue());
				if (!valid)
					return remarksMessage.append(value).append(" - Validation:")
							.append(entrySet.getKey() + " " + entrySet.getValue()).toString();
			}

		}

		return null;
	}

	/**
	 * Get FieldType from parsed object
	 * 
	 * @param fieldObject
	 * @param columnName
	 * @return FieldType
	 */
	public Field getCellFieldRule(List<Field> fieldObject, String columnName) {
		if (!fieldsAsMap.containsKey(columnName)) {
			Field foundField = fieldObject.stream()
					.filter(field -> field.getLabel()
							.equals(columnName.endsWith(BulkUploadConstants.MANDATORY_MARK)
									? columnName.substring(0, columnName.length() - 1) : columnName))
					.findFirst().orElse(new Field());
			fieldsAsMap.put(columnName, foundField);
		}
		return fieldsAsMap.get(columnName);

	}

	/**
	 * This method for the type of data in the cell, extracts the data and
	 * returns it as a string.
	 */
	public static String getCellValueAsString(Cell cell) {
		String strCellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case STRING:
				strCellValue = cell.toString();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					strCellValue = dateFormat.format(cell.getDateCellValue());
				} else {
					Double value = cell.getNumericCellValue();
					Long longValue = value.longValue();
					strCellValue = longValue.toString();
				}
				break;
			case BOOLEAN:
				strCellValue = Boolean.toString(cell.getBooleanCellValue());
				break;
			case BLANK:
				strCellValue = "";
				break;
			case ERROR:
				break;
			case FORMULA:
				break;
			case _NONE:
				break;
			default:
				break;
			}
		}
		return strCellValue;
	}

	/**
	 * 
	 * @param templateName
	 * @param columnData
	 * @return
	 */
	public Path generateTemplateWithColumns(String templateName, List<Field> columnData) {

		createExcelTemplate(columnData, templateName + ".xlsx");
		return Paths.get(getFolderPath(BulkUploadConstants.TEMP_LOCATION) + BulkUploadConstants.SEPARATOR + templateName
				+ ".xlsx");

	}

	public List<Map<String, String>> readValidExcelFile(InputStream is, FileMetaData fileMetaInfo,
			String fieldJsonSchema, UserInfo user) throws IOException, IllegalAccessException {
		if (CommonUtil.isEmpty(fieldJsonSchema)) {
			fieldJsonSchema = "AO-fields.json";
		}
		String hashKey = DigestUtils
				.md5Hex(String.join("-", fileMetaInfo.getHeaderColumnDisplayNames()) + KEY + fileMetaInfo.getRows());
		if (!hashKey.equals(fileMetaInfo.getValidationCode())) {
			throw new IllegalAccessException(
					messageSource.getMessage("BULK_ONBOARD_EXCEL_VAlIDATION_FAILURE", user.getLocale()));
		}

		try (Workbook workbook = WorkbookFactory.create(is)) {

			Sheet sheet = workbook.getSheetAt(0);

			// get template rule from json
			BulkImportTemplate template = bulkInivitationHelper.readJsonSchema(fieldJsonSchema);
			List<Field> templateFields = template.getFields();

			// list of column keys
			List<String> keyList = new ArrayList<>();

			List<Map<String, String>> processedData = new ArrayList<>();
			Set<String> uniqueIdentifierSet = new HashSet<>();
			// Header Row
			int firstRowNo = sheet.getFirstRowNum();
			Row headerRow = sheet.getRow(firstRowNo);
			int columnsPerRow = headerRow.getLastCellNum();
			// Read non-header columns
			for (Row row : sheet) {
				if (row.getRowNum() > firstRowNo) {
					Map<String, String> thisRowDataMap = new HashMap<>();
					String remarks = null;
					String status = BulkUploadConstants.STATUS_VALID;
					StringBuilder duplicateIdentifier = new StringBuilder();
					boolean inValidated = false;
					for (int i = 0; i < columnsPerRow; i++) {
						String headerColumnName = fileMetaInfo.getHeaderColumnDisplayNames().get(i); // key
						// contains
						// *
						// mark for
						// mandatory field
						if (headerColumnName.endsWith(BulkUploadConstants.MANDATORY_MARK)) {
							headerColumnName = headerColumnName.substring(0, headerColumnName.length() - 1);
						}

						Cell cell = row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL);
						String value = getCellValue(cell);
						if (null != value) {
							cell.setCellType(CellType.STRING);
						}
						/*
						 * String value = null; if (!isCellEmpty(cell)) {
						 * cell.setCellType(CellType.STRING); value =
						 * cell.getStringCellValue().trim(); }
						 */

						Field cellValidationRule = getCellFieldRule(templateFields, headerColumnName);
						String key = cellValidationRule.getKey() == null ? headerColumnName
								: cellValidationRule.getKey();
						if (processedData.isEmpty()) {
							keyList.add(key);
						}
						thisRowDataMap.put(key, value);
						if (!StringUtils.isEmpty(template.getIdentifiers())
								&& template.getIdentifiers().contains(key)) {
							duplicateIdentifier.append(value);
						}

						// any column invalid, put remarks,status and don't
						// check for other cell
						if (!inValidated) {
							remarks = doValidateData(cellValidationRule, value);
							if (remarks != null) {
								inValidated = true;
								status = BulkUploadConstants.STATUS_REJECT;
							}
						}

					}
					if (!StringUtils.isEmpty(template.getIdentifiers())) {
						if (!inValidated && uniqueIdentifierSet.contains(duplicateIdentifier.toString())) {
							status = BulkUploadConstants.STATUS_REJECT;
							remarks = "Duplicate row of: " + String.join(",", template.getIdentifiers());
						} else {
							uniqueIdentifierSet.add(duplicateIdentifier.toString());
						}
					}
					thisRowDataMap.put(BulkUploadConstants.ROW_STATUS_HEADER, status);
					thisRowDataMap.put(BulkUploadConstants.ROW_REMARKS_HEADER, remarks);
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

}
