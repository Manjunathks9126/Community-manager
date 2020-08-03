package com.ot.cm.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TGOCP_BULK_IMPORT")
@DynamicUpdate(value=true)
public class BulkImport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="requestId_seq")
	@SequenceGenerator(name = "requestId_seq", sequenceName = "TGOCP_BULK_INVITE_SEQ", allocationSize = 1)
	@Column(name = "REQUEST_ID")
	private Long requestId;

	@Column(name = "CALLER_APPLICATION")
	private String callerApplication;

	@Column(name = "INVITATION_ID")
	private Long invitationId;

	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Lob
	@Column(name = "FILE_CONTENT")
    private byte[] fileContent;
   
	@Lob
	@Column(name = "PROCESSED_DATA")
    private String data;
	
	@Column(name = "CONTEXT")
	private String context;
	
	@Column(name = "USER_EMAIL_ID")
	private String userEmailId;

	@Column(name = "COMPANY_ID")
	private String companyId;

	@Column(name = "STATUS")
	private String  status;

	@Lob
	@Column(name = "META_DATA")
	private String metaInfo;
	
	@Column(name = "PROCESS_START_TIME")
	@Type(type="timestamp")
	private Date processStartTime; 
	
	@Column(name = "PROCESS_END_TIME")
	@Type(type="timestamp")
	private Date processEndTime;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIMESTAMP")
	private Date createdTimeStamp;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_TIMESTAMP")
	private Date modifiedTimeStamp;

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

	public Long getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] content) {
		this.fileContent = content;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String exportContext) {
		this.context = exportContext;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(Date modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BulkImport [requestId=").append(requestId).append(", callerApplication=")
				.append(callerApplication).append(", invitationId=").append(invitationId).append(", fileName=")
				.append(fileName).append(", context=").append(context).append(", userEmailId=").append(userEmailId)
				.append(", companyId=").append(companyId).append(", status=").append(status)
				.append(", processStartTime=").append(processStartTime).append(", processEndTime=")
				.append(processEndTime).append(", createdBy=").append(createdBy).append(", createdTimeStamp=")
				.append(createdTimeStamp).append(", modifiedBy=").append(modifiedBy).append(", remarks=")
				.append(remarks).append("]");
		return builder.toString();
	}

	
}