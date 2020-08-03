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
 Mar 20, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.request.entity;

import java.util.Map;

import com.ot.cm.rest.response.entity.InvitationNeoRest;

/**
 * @author ssen
 *
 */
public class BulkImportData {
	private Map<String, String> rowData;
	private InvitationNeoRest invitationMetaData;
	private String context;
	private Long workflowId;
	private Long taskId;
	
	public Map<String, String> getRowData() {
		return rowData;
	}
	public void setRowData(Map<String, String> rowData) {
		this.rowData = rowData;
	}
	public InvitationNeoRest getInvitationMetaData() {
		return invitationMetaData;
	}
	public void setInvitationMetaData(InvitationNeoRest invitationMetaData) {
		this.invitationMetaData = invitationMetaData;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Long getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
}
