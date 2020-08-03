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
 08/03/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Rest response which is common across all requests which contains
 * error,response details.
 * 
 * @author Dwaraka
 *
 */
public class TGOCPRestResponse<T1, T2> {

	private TGOCPRestResponseDetails<T1, T2> responseDetails;
	@JsonIgnore
	private Object[] auditDetails; // Dynamic values required for Audit log Text

	public TGOCPRestResponse(TGOCPRestResponseDetails<T1, T2> responseDetails) {
		this.responseDetails = responseDetails;
	}

	public TGOCPRestResponse(TGOCPRestResponseDetails<T1, T2> responseDetails, Object... auditDetails) {
		this.responseDetails = responseDetails;
		this.auditDetails = auditDetails;
	}

	public TGOCPRestResponseDetails<T1, T2> getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(TGOCPRestResponseDetails<T1, T2> responseDetails) {
		this.responseDetails = responseDetails;
	}

	public Object[] getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(Object... auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TGOCPRestResponse [responseDetails=");
		builder.append(responseDetails);
		builder.append("]");
		return builder.toString();
	}

}
