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
 08/03/2017    Rishabh                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RARestResponseDetails<T> {

	protected Integer statusCode;

	protected String statusMessage;

	protected T responseEntity;

	protected String targetResourceRefId;

	protected String targetResourceName;

	protected String targetResourceLink;

	
	public RARestResponseDetails() {
	}

	@JsonIgnore
	public RARestResponseDetails(T responseEntity, Integer statusCode, String statusMessage, String targetResourceRefId,
			String targetResourceName, String targetResourceLink) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.responseEntity = responseEntity;
		this.targetResourceRefId = targetResourceRefId;
		this.targetResourceName = targetResourceName;
		this.targetResourceLink = targetResourceLink;
	}

	public Integer getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public T getResponseEntity() {
		return this.responseEntity;
	}

	public void setResponseEntity(T responseEntity) {
		this.responseEntity = responseEntity;
	}

	public String getTargetResourceRefId() {
		return this.targetResourceRefId;
	}

	public void setTargetResourceRefId(String targetResourceRefId) {
		this.targetResourceRefId = targetResourceRefId;
	}

	public String getTargetResourceName() {
		return this.targetResourceName;
	}

	public void setTargetResourceName(String targetResourceName) {
		this.targetResourceName = targetResourceName;
	}

	public String getTargetResourceLink() {
		return this.targetResourceLink;
	}

	public void setTargetResourceLink(String targetResourceLink) {
		this.targetResourceLink = targetResourceLink;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.responseEntity == null) ? 0 : this.responseEntity.hashCode());
		result = 31 * result + ((this.statusCode == null) ? 0 : this.statusCode.hashCode());
		result = 31 * result + ((this.statusMessage == null) ? 0 : this.statusMessage.hashCode());
		result = 31 * result + ((this.targetResourceLink == null) ? 0 : this.targetResourceLink.hashCode());
		result = 31 * result + ((this.targetResourceName == null) ? 0 : this.targetResourceName.hashCode());
		result = 31 * result + ((this.targetResourceRefId == null) ? 0 : this.targetResourceRefId.hashCode());
		return result;
	}



	@Override
	public String toString() {
		return "ResponseDetails [statusCode=" + this.statusCode + ", statusMessage=" + this.statusMessage
				+ ", responseEntity=" + this.responseEntity + ", targetResourceRefId=" + this.targetResourceRefId
				+ ", targetResourceName=" + this.targetResourceName + ", targetResourceLink=" + this.targetResourceLink
				+ "]";
	}
}
