package com.ot.cm.rest.response.entity;


import java.io.Serializable;

public class OnboardingWorkflowExtended implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long workflowId;
	private String attrName;
	private String attrValue;

	public OnboardingWorkflowExtended() {

	}

	public OnboardingWorkflowExtended(String attrName, String attrValue) {
		super();
		this.attrName = attrName;
		this.attrValue = attrValue;
	}
	public OnboardingWorkflowExtended(long workflowId, String attrName, String attrValue) {
		super();
		this.workflowId=workflowId;
		this.attrName = attrName;
		this.attrValue = attrValue;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

}
