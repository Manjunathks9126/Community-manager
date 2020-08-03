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
package com.ot.cm.rest.response.entity;

public class NeoRestResponseWrapper<T> {

	private NeoRestResponseDetails<T> responseDetails;

	public NeoRestResponseDetails<T> getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(NeoRestResponseDetails<T> responseDetails) {
		this.responseDetails = responseDetails;
	}

	public NeoRestResponseWrapper() {
		
	}	
	public NeoRestResponseWrapper(NeoRestResponseDetails<T> responseDetails) {
		this.responseDetails = responseDetails;
	}
	
	public NeoRestResponseWrapper(T responseEntity, Integer statusCode, String statusMessage, String targetResourceRefId,
			String targetResourceName, String targetResourceLink) {
		this.responseDetails = new NeoRestResponseDetails<T>(responseEntity, statusCode, statusMessage, targetResourceRefId,
				targetResourceName, targetResourceLink);
	}
}
