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
 11/16/2017    Dwaraka                              Modified - 16.7
 ******************************************************************************/
package com.ot.cm.rest.controller;

import com.gxs.services.bsapi.rs.v2.entity.PointOfContactDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.business.service.PointOfContactServiceImpl;
import com.ot.cm.business.service.UserService;
import com.ot.cm.cms.response.entity.User;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.request.entity.UserFilter;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("companies")
public class CompanyProfileController extends BaseRestController {

	@Autowired
	private BusinessUntilRestClient businessUntilRestClient;

	@Autowired
	private UserService userService;

	@Autowired
	PointOfContactServiceImpl pointOfContactService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<BusinessUnitDetailsType, ErrorResponse> getCompany(HttpServletRequest httpServletRequest,
			@PathVariable("id") String companyId) throws TGOCPBaseException {

		TGOCPRestResponse<BusinessUnitDetailsType, ErrorResponse> response = null;
		TGOCPRestResponseDetails<BusinessUnitDetailsType, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		BusinessUnitDetailsType businessType = businessUntilRestClient.getBusinessUnitDetailsType(companyId);
		
		responseDetails.setResponseEntity(businessType);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@PostMapping(value = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<User>, ErrorResponse> getUsers(HttpServletRequest httpServletRequest,
			@PathVariable("id") String companyId, @RequestBody UserFilter filterObject) throws TGOCPBaseException, ParseException {

		TGOCPRestResponse<ListingQueryResponse<User>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<User>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<ListingQueryResponse<User>, ErrorResponse>();
		filterObject.setId(companyId);
		responseDetails.setResponseEntity(userService.getUsers(filterObject));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}


	@GetMapping(value = "{id}/poc", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<PointOfContactDetailsType, ErrorResponse> getTechnicalPointOfContact(HttpServletRequest httpServletRequest,
																								  @PathVariable("id") String companyId) throws TGOCPBaseException {

		TGOCPRestResponse<PointOfContactDetailsType, ErrorResponse> response = null;
		TGOCPRestResponseDetails<PointOfContactDetailsType, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		PointOfContactDetailsType businessType = pointOfContactService.getTechnicalPointOfContact(companyId);

		responseDetails.setResponseEntity(businessType);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}
