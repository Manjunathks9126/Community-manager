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
 05/09/2108    Madan                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.TileServiceImpl;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.NavigationFeatures;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.Tile;

@RestController
@RequestMapping("tiles")
public class NavigationFeaturesController extends BaseRestController {
	@Autowired
	TileServiceImpl tileService;

	@GetMapping(value = "/communitypage")
	public TGOCPRestResponseDetails<List<Tile>, ErrorResponse> listCommunityPageTiles() {
		TGOCPRestResponseDetails<List<Tile>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<Tile>, ErrorResponse>();
		responseDetails.setResponseEntity(tileService.listCommunityPageTiles());
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return responseDetails;
	}

	/**
	 * GET hamburger info based on user subscription
	 * 
	 * @param httpServletRequest
	 * @return TGOCPRestResponse
	 * @throws TGOCPBaseException
	 */
	@GetMapping(value = "")
	public TGOCPRestResponse<NavigationFeatures, ErrorResponse> getHamburgerInfo(HttpServletRequest httpServletRequest)
			throws TGOCPBaseException {
		TGOCPRestResponse<NavigationFeatures, ErrorResponse> response = null;
		NavigationFeatures navigations = tileService
				.getHeaderFeatures(TGOCPSessionUtil.getUserInfoInSession(httpServletRequest));
		TGOCPRestResponseDetails<NavigationFeatures, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<NavigationFeatures, ErrorResponse>();
		responseDetails.setSuccess(true);
		responseDetails.setResponseEntity(navigations);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}