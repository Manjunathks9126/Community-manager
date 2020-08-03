package com.ot.cm.rest.response.entity;

import java.util.List;

public class TPDirectoryFilterOptionsEntity {
	private String rowsPerPage;
	private List<String> companyStatus;

	public String getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public List<String> getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(List<String> companyStatus) {
		this.companyStatus = companyStatus;
	}

}
