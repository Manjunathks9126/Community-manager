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
package com.ot.cm.rest.controller.export;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.BusinessPartnerService;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.business.service.TradingPartnerDirectoryService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.util.JacksonWrapper;

/**
 * Trading Partner Listing controller to handle load of data,search,export.
 * 
 * @author Dwaraka
 *
 */
@RestController
@RequestMapping("export")
public class ExportController extends BaseRestController {

	@Autowired
	private TradingPartnerDirectoryService tpDirectoryService;
	@Autowired
	private BusinessPartnerService bprService;
	@Autowired
	OnboardingService onboardingService;

	@Autowired
	private JacksonWrapper mapper;

	@PostMapping(value = "/bulkBpList", produces = MediaType.APPLICATION_JSON_VALUE)
	public String bpListJMS(@RequestBody BPRFilterSearchQuery filterObject)
			throws IOException, TGOCPBaseException, ParseException {
		return mapper.writeValueAsString(bprService.getBpDetails(filterObject).getItemList());
	}

	@PostMapping(value = "/bulkWorkflowReport", produces = MediaType.APPLICATION_JSON_VALUE)
	public String workflowReport(@RequestBody OnboardingWorkflowListingFilterQuery filterObject)
			throws IOException, TGOCPBaseException {
		String response = "";
		response = mapper
				.writeValueAsString(onboardingService.getBulkOnboardingWorkflowReport(filterObject).getItemList());
		return response;
	}

}
