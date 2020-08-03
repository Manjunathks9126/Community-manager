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
 Feb 21, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.vo.bulk;

import java.util.List;

/**
 * @author ssen
 *
 */
public class FileMetaData {
	private Integer rows;
	private Integer columns;
	private boolean valid;
	private String validationCode;
	private List<String> headerColumnDisplayNames;
	private List<String> headerKeys;
	private Long requestId;
	private Long workflowId;
	private Long taskId;

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer noOfRows) {
		this.rows = noOfRows;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public List<String> getHeaderColumnDisplayNames() {
		return headerColumnDisplayNames;
	}

	public void setHeaderColumnDisplayNames(List<String> headerColumns) {
		this.headerColumnDisplayNames = headerColumns;
	}

	public List<String> getHeaderKeys() {
		return headerKeys;
	}

	public void setHeaderKeys(List<String> headerKeys) {
		this.headerKeys = headerKeys;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
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
