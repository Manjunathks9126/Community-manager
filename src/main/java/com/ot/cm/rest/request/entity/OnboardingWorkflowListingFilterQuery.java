package com.ot.cm.rest.request.entity;

public class OnboardingWorkflowListingFilterQuery {

	private String uniqueId;
	private String companyId;
	private String userId;
	private String after;
	private String limit;
	private String[] exportColumns;

	public OnboardingWorkflowListingFilterQuery() {
	}

	public OnboardingWorkflowListingFilterQuery(String uniqueId, String companyId, String userId, String after,
			String limit, String[] exportColumns) {
		super();
		this.uniqueId = uniqueId;
		this.companyId = companyId;
		this.userId = userId;
		this.after = after;
		this.limit = limit;
		this.exportColumns = exportColumns;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String[] getExportColumns() {
		return exportColumns;
	}

	public void setExportColumns(String[] exportColumns) {
		this.exportColumns = exportColumns;
	}

}
