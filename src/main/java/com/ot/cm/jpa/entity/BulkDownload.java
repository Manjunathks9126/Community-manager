package com.ot.cm.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TGOCP_BULK_EXPORT")
public class BulkDownload implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="requestId_seq")
	@SequenceGenerator(name = "requestId_seq", sequenceName = "TGOCP_BULK_EXPORT_SEQ", allocationSize = 1)
	@Column(name = "REQUEST_ID")
	private Long requestId;

	@Column(name = "CALLER_APPLICATION")
	private String callerApplication;

	@Column(name = "DATA_EXTRACTION_URL")
	private String dataExtractionUrl;

	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "EXPORT_CONTEXT")
	private String exportContext;
	
	@Column(name = "USER_EMAIL_ID")
	private String userEmailId;

	@Column(name = "COMPANY_ID")
	private String companyId;

	@Column(name = "TOTAL_RECORDS")
	private Integer totalRecords;

	@Column(name = "STATUS")
	private String  status;

	@Column(name = "FILTER_STRING")
	private String filterString;
	
	@Column(name = "DSM_KEY")
	private String dsmKey;
	
	@Column(name = "PROCESS_START_TIME")
	@Type(type="timestamp")
	private Date processStartTime; 
	
	@Column(name = "PROCESS_END_TIME")
	@Type(type="timestamp")
	private Date processEndTime;
	
	@Type(type="timestamp")
	@Column(name = "LAST_DOWNLOAD_TIMESTAMP")
	private Date lastDownloadTimeStamp;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Type(type="timestamp")
	@Column(name="CREATE_TIMESTAMP")
	private Date createdTimeStamp;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getCallerApplication() {
		return callerApplication;
	}

	public void setCallerApplication(String callerApplication) {
		this.callerApplication = callerApplication;
	}

	public String getDataExtractionUrl() {
		return dataExtractionUrl;
	}

	public void setDataExtractionUrl(String dataExtractionUrl) {
		this.dataExtractionUrl = dataExtractionUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getExportContext() {
		return exportContext;
	}

	public void setExportContext(String exportContext) {
		this.exportContext = exportContext;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public String getDsmKey() {
		return dsmKey;
	}

	public void setDsmKey(String dsmKey) {
		this.dsmKey = dsmKey;
	}

	public Date getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Date getLastDownloadTimeStamp() {
		return lastDownloadTimeStamp;
	}

	public void setLastDownloadTimeStamp(Date lastDownloadTimeStamp) {
		this.lastDownloadTimeStamp = lastDownloadTimeStamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	
	
}