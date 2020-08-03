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
package com.ot.cm.rest.response.entity;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ssen
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomFieldEntity {

	private String uniqueId;
	private String customFieldId;
	private String question;
	private String defaultAnswer;
	private CustomFieldChoice defaultChoice;
	private Boolean mandatory;
	private String groupId;
	private String customFieldType;
	private String creationDate;
	private String lastModifiedDate;
	private String createdBy;
	private String modifiedBy;
	private String typeName;
	private Integer typeId;
	private String validationRules;
	private FileObject[] fileObjects;
	private CustomFieldChoice[] choices;

	private String dependentCustomfieldId;
	private String[] dependentChoiceIds;
	private String dependencyConjunction;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(String customFieldId) {
		this.customFieldId = customFieldId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDefaultAnswer() {
		return defaultAnswer;
	}

	public void setDefaultAnswer(String defaultAnswer) {
		this.defaultAnswer = defaultAnswer;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCustomFieldType() {
		return customFieldType;
	}

	public void setCustomFieldType(String customFieldType) {
		this.customFieldType = customFieldType;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public CustomFieldChoice getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(CustomFieldChoice defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public CustomFieldChoice[] getChoices() {
		return choices;
	}

	public void setChoices(CustomFieldChoice[] choices) {
		this.choices = choices;
	}

	public String getDependentCustomfieldId() {
		return dependentCustomfieldId;
	}

	public void setDependentCustomfieldId(String dependentCustomfieldId) {
		this.dependentCustomfieldId = dependentCustomfieldId;
	}

	public String[] getDependentChoiceIds() {
		return dependentChoiceIds;
	}

	public void setDependentChoiceIds(String[] dependentChoiceIds) {
		this.dependentChoiceIds = dependentChoiceIds;
	}

	public String getDependencyConjunction() {
		return dependencyConjunction;
	}

	public void setDependencyConjunction(String dependencyConjunction) {
		this.dependencyConjunction = dependencyConjunction;
	}

	public String getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}

	public FileObject[] getFileObjects() {
		return fileObjects;
	}

	public void setFileObjects(FileObject[] fileObjects) {
		this.fileObjects = fileObjects;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomFieldEntity [uniqueId=").append(uniqueId).append(", question=").append(question)
				.append(", defaultAnswer=").append(defaultAnswer).append(", defaultChoice=").append(defaultChoice)
				.append(", mandatory=").append(mandatory).append(", groupId=").append(groupId)
				.append(", customFieldType=").append(customFieldType).append(", creationDate=").append(creationDate)
				.append(", lastModifiedDate=").append(lastModifiedDate).append(", createdBy=").append(createdBy)
				.append(", modifiedBy=").append(modifiedBy).append(", typeName=").append(typeName).append(", typeId=")
				.append(typeId).append(", choices=").append(Arrays.toString(choices)).append("]")
				.append(", dependentCustomfieldId=").append(dependentCustomfieldId).append(", dependentChoiceIds=")
				.append(Arrays.toString(dependentChoiceIds)).append("]").append(", dependencyConjunction=")
				.append(dependencyConjunction);
		return builder.toString();
	}

}
