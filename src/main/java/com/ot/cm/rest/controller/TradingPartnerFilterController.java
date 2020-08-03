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
package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.TradingPartnerDirectoryService;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.TPDirectoryFilterOptionsEntity;

/**
 * Trading Partner Listing controller to handle load of data,search,export.
 * 
 * @author Dwaraka
 *
 */
@RestController
public class TradingPartnerFilterController extends BaseRestController {

	@Autowired
	private TradingPartnerDirectoryService tpDirectoryService;

	@GetMapping(value = "/tpDirectoryFilterOptions", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<TPDirectoryFilterOptionsEntity, ErrorResponse> tpDirectoryFilterOptions(
			HttpServletRequest httpServletRequest) throws CMApplicationException {
		TGOCPRestResponse<TPDirectoryFilterOptionsEntity, ErrorResponse> response = null;
		TGOCPRestResponseDetails<TPDirectoryFilterOptionsEntity, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<TPDirectoryFilterOptionsEntity, ErrorResponse>();

		responseDetails.setResponseEntity(tpDirectoryService.getTPDirectoryFilterOptions());
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);
		
		return response;
	}
}
