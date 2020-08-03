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
 Jan 02, 2020      vberanjeg                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author vberanjeg
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnboardingCustomFieldGroupType {

	private String uniqueId;
	private String name;
	private String description;
	private BusinessUnit ownerBusinessUnit;
	private String businessUnitId;
	private String creationDate;
	private String lastModifiedDate;
	private String createdBy;
	private String modifiedBy;
	private String typeName;
	private Integer typeId;
	private String dependentCustomfieldId;
	private String[] dependentChoiceIds;
	private String dependencyConjunction;
	private CustomFieldEntity dependentCustomField;
	private Boolean hidden = false;
	private String url;
	private String dependentGroupId;

	public String getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BusinessUnit getOwnerBusinessUnit() {
		return ownerBusinessUnit;
	}

	public void setOwnerBusinessUnit(BusinessUnit ownerBusinessUnit) {
		this.ownerBusinessUnit = ownerBusinessUnit;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
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

	public CustomFieldEntity getDependentCustomField() {
		return dependentCustomField;
	}

	public void setDependentCustomField(CustomFieldEntity dependentCustomField) {
		this.dependentCustomField = dependentCustomField;
	}

	public Boolean isHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDependentGroupId() {
		return dependentGroupId;
	}

	public void setDependentGroupId(String dependentGroupId) {
		this.dependentGroupId = dependentGroupId;
	}

}
