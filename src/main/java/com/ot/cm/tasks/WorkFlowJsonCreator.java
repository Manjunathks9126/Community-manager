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
 Mar 25, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.JacksonWrapper;
import com.ot.cm.vo.bulk.Field;
import com.ot.cm.vo.bulk.ValidationType;

/**
 * @author ssen
 *
 */
@Component
public class WorkFlowJsonCreator {

	@Autowired
	private JacksonWrapper mapper;

	private Map<String, EnumMap<ValidationType, Object>> fieldValidationMap = new HashMap<>();

	/**
	 * @return
	 * Map<String,EnumMap<ValidationType,Object>>
	 */
	protected Map<String, EnumMap<ValidationType, Object>> getFieldValidationMap() {
		if (fieldValidationMap.isEmpty()) {

			try {
				fieldValidationMap = mapper.readValue(ResourceUtils.getFile("classpath:json/field-validations.json"),
						new TypeReference<Map<String, EnumMap<ValidationType, Object>>>() {
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fieldValidationMap;
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 * List<Field>
	 */
	public List<Field> readTaskJson(String json){
		List<Field> flds =  readTaskJson(json, false);
		System.out.println(flds);
		return flds;
	}

	/**
	 * 
	 * @param json
	 * @param getValidations
	 * @return
	 * List<Field>
	 */
	public List<Field> readTaskJson(String json, boolean getValidations) {
		List<Field> fields = new ArrayList<>();
		try {
			SchemaAttributes schema = mapper.readValue(json,SchemaAttributes.class);
			fields = iterateIn(schema, fields, null, null, getValidations);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fields;
	}

	/**
	 * 
	 * @param schema
	 * @param fields
	 * @param parentKey
	 * @param getValidationChecks
	 * @return
	 * List<Field>
	 */
	private List<Field> iterateIn(SchemaAttributes schema, List<Field> fields, String parentKey, List<String> requiredFields, 
			boolean getValidationChecks) {
		if (schema.getProperties() != null && schema.getType().equals("object")) {
			for (Map.Entry<String, SchemaAttributes> child : schema.getProperties().entrySet()) {
				iterateIn(child.getValue(), fields,parentKey == null ? child.getKey() : parentKey + "." + child.getKey(),
						schema.getRequired(), getValidationChecks);
			}
		} else {
			if (!CommonUtil.isEmpty(schema.getTitle())) {

				fields.add(schemaToFieldType(schema, parentKey, requiredFields, getValidationChecks));
			}
		}
		return fields;
	}

	/**
	 * 
	 * @param schema
	 * @param key
	 * @param requiredFields 
	 * @param getValidationChecks
	 * @return
	 * Field
	 */
	private Field schemaToFieldType(SchemaAttributes schema, String key, List<String> requiredFields, boolean getValidationChecks) {
		Field field = new Field();
		field.setLabel(schema.getTitle());
		field.setKey(key);
		if(null != requiredFields && requiredFields.contains(key.substring(key.lastIndexOf('.')+1))){
		field.setRequired(true);
		}
		if (getValidationChecks) {
			// get validation rule depending on key
			Map<ValidationType, Object> validation = getFieldValidationMap().get(key);
			if (validation == null) {
				validation = new EnumMap<>(ValidationType.class);
			}
			// override the rules as required
			if (schema.getMinimum() != 0) {
				validation.put(ValidationType.MINLENGTH, schema.getMinimum());
			}
			if (schema.getMaximum() != 0) {
				validation.put(ValidationType.MAXLENGTH, schema.getMaximum());
			}
			if (schema.getPattern() != null) {
				try {
					Pattern.compile(schema.getPattern());
					validation.put(ValidationType.PATTERN, schema.getPattern());
				} catch (PatternSyntaxException exception) {
					System.out.println(exception.getDescription());
				}
			}

			field.setValidationRules(validation);
		}
		return field;
	}
	
	
	public String getJsonSchema(@NonNull Map<String, String> rowData){
		final char splitChar = '.';
		MultiValueMap<String,KeyValue> mv = new LinkedMultiValueMap<>();
		for(Map.Entry<String, String> entry : rowData.entrySet()){
			String key = entry.getKey();
			int indexOfKeySplitter = key.indexOf(splitChar);
			if(indexOfKeySplitter<1) continue;
			if(key.lastIndexOf(splitChar) == indexOfKeySplitter){
				String myKey = key.substring(0,indexOfKeySplitter);
				mv.add(myKey, new KeyValue(key.substring(indexOfKeySplitter+1), entry.getValue()));
			}
		}
		Map<String,Object> map = new HashMap<>(mv.size());
		mv.forEach((key,list) -> {
			Map<String, String> interKeyValue = new HashMap<>(list.size());
			for(KeyValue child : list){
				interKeyValue.put(child.getKey(), child.getValue());
			}
			map.put(key, interKeyValue);
			
		});		
		return mapper.writeValueAsString(map);
	}
}
