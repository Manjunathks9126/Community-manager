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
 May 18, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.request.entity.search;

import java.util.Arrays;

/**
 * @author ssen
 *
 */
public class BPRFilterSearchQuery extends BaseSearchQuery{
	private String companyId;
	private String userId;
	private String partnerCompanyId;
	private String companyName;
	private String displayName;
	private String ediAddress;
	private String dateFrom;
	private String dateTo;
	private String[] status;
	private String[] exportColumns;
	private String sortBy;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPartnerCompanyId() {
		return partnerCompanyId;
	}
	public void setPartnerCompanyId(String partnerCompanyId) {
		this.partnerCompanyId = partnerCompanyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEdiAddress() {
		return ediAddress;
	}
	public void setEdiAddress(String ediAddress) {
		this.ediAddress = ediAddress;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}	
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	
	public String[] getExportColumns() {
		return exportColumns;
	}
	public void setExportColumns(String[] exportColumns) {
		this.exportColumns = exportColumns;
	}
	@Override
	public String toString() {
		return "BPRFilterSearchQuery [companyId=" + companyId + ", userId=" + userId + ", partnerCompanyId="
				+ partnerCompanyId + ", companyName=" + companyName + ", displayName=" + displayName + ", ediAddress="
				+ ediAddress + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", status=" + Arrays.toString(status)
				+ ", exportColumns=" + Arrays.toString(exportColumns) + ",sortBy="+sortBy+"]";
	}
	
	
	

}
