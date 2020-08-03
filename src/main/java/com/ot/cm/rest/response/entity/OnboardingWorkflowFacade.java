/**
 * 
 */
package com.ot.cm.rest.response.entity;

import java.util.Date;
import java.util.Set;

public class OnboardingWorkflowFacade{

	private Long wfFacadeId;
	private String wfFacadeName;
	private String ownerBuId;
	private String ownerBuName;
	private Long workflowId;
	private String description;
	private String createdBy;
	private Date createdTimestamp;
	private String modifiedBy;
	private String status;
	private String serviceName;
	private Set<OnboardingWorkflowExtended> owfExtended;

	private Date modifiedTimestamp;

	public Long getWfFacadeId() {
		return wfFacadeId;
	}

	public void setWfFacadeId(Long wfFacadeId) {
		this.wfFacadeId = wfFacadeId;
	}


	public String getWfFacadeName() {
		return wfFacadeName;
	}

	public void setWfFacadeName(String wfFacadeName) {
		this.wfFacadeName = wfFacadeName;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getOwnerBuId() {
		return ownerBuId;
	}

	public void setOwnerBuId(String ownerBuId) {
		this.ownerBuId = ownerBuId;
	}

	public String getOwnerBuName() {
		return ownerBuName;
	}

	public void setOwnerBuName(String ownerBuName) {
		this.ownerBuName = ownerBuName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Date modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Set<OnboardingWorkflowExtended> getOwfExtended() {
		return owfExtended;
	}

	public void setOwfExtended(Set<OnboardingWorkflowExtended> owfExtended) {
		this.owfExtended = owfExtended;
	}
}
