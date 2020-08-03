package com.ot.cm.rest.request.entity;

public class ListingFilterQuery {

	private String id;
	private String userId;
	private Boolean countOnly;
	private String after;
	private String limit;
	private String name;
	private String[] status;
	private String sortField;
	private String sortOrder;
	private String ediAddress;
	private String qualifier;
	private String[] exportColumns;
	private String emailId;
	public ListingFilterQuery() {

	}

	public ListingFilterQuery(String buId, Boolean countOnly, String startRowNum, String maxRowCount,
			String companyNameContains, String[] companyStatus, String sortField, String sortOrder, String ediAddress, String qualifier) {
		super();
		this.id = buId;
		this.countOnly = countOnly;
		this.after = startRowNum;
		this.limit = maxRowCount;
		this.name = companyNameContains;
		this.status = companyStatus;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		this.ediAddress = ediAddress;
		this.qualifier = qualifier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCountOnly() {
		return countOnly;
	}

	public void setCountOnly(Boolean countOnly) {
		this.countOnly = countOnly;
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

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public String getEdiAddress() {
		return ediAddress;
	}

	public void setEdiAddress(String ediAddress) {
		this.ediAddress = ediAddress;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String[] getExportColumns() {
		return exportColumns;
	}

	public void setExportColumns(String[] exportColumns) {
		this.exportColumns = exportColumns;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
}

