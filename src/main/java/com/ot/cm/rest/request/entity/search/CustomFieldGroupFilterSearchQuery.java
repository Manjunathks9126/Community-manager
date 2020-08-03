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
 Aug 17, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.request.entity.search;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author ssen
 *
 */
public class CustomFieldGroupFilterSearchQuery extends BaseSearchQuery {

	private String[] sortColumns;
	private String uniqueId;
	private String name;
	private String description;
	private String businessUnitId;
	private String creationDate;
	private String lastModifiedDate;
	@JsonFormat(pattern = "dd-MMM-YYYY")
	private String createdBy;
	@JsonFormat(pattern = "dd-MMM-YYYY")
	private String modifiedBy;
	private boolean parentsOnly;
	private String categoryId;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String[] getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String[] sortColumns) {
		this.sortColumns = sortColumns;
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

	public String getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
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

	public boolean isParentsOnly() {
		return parentsOnly;
	}

	public void setParentsOnly(boolean parentsOnly) {
		this.parentsOnly = parentsOnly;
	}

	@Override
	public String toString() {
		return "CustomFieldGroupFilterSearchQuery [sortColumns=" + Arrays.toString(sortColumns) + ", uniqueId="
				+ uniqueId + ", name=" + name + ", description=" + description + ", businessUnitId=" + businessUnitId
				+ ", creationDate=" + creationDate + ", lastModifiedDate=" + lastModifiedDate + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + "]";
	}

}
