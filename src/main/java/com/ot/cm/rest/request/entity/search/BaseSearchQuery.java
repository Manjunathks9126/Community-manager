package com.ot.cm.rest.request.entity.search;

/******************************************************************************
 * All rights reserved. All information contained in this software is
 * confidential and proprietary to opentext. No part of this software may be
 * reproduced or transmitted in any form or any means, electronic, mechanical,
 * photocopying, recording or otherwise stored in any retrieval system of any
 * nature without the prior written permission of opentext. This material is a
 * trade secret and its confidentiality is strictly maintained. Use of any
 * copyright notice does not imply unrestricted public access to this material.
 * 
 * (c) opentext
 *******************************************************************************
 * 
 * Change Log: Date Name Defect# Description
 * -------------------------------------------------------------------------------
 * 02/17/2017 Madan Base SearchQeury
 ******************************************************************************/
public class BaseSearchQuery {
	/**
	 * Class {@code BaseSearchQuery} contains basics search query parameters for
	 * all searches in applications for data to list,export
	 */
	private boolean countOnly;
	private String after;
	private String limit;
	private String sortField;
	private String sortOrder;
	private boolean exportAll;

	

	public boolean isExportAll() {
		return exportAll;
	}

	public void setExportAll(boolean exportAll) {
		this.exportAll = exportAll;
	}

	public boolean isCountOnly() {
		return countOnly;
	}

	public void setCountOnly(boolean countOnly) {
		this.countOnly = countOnly;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
