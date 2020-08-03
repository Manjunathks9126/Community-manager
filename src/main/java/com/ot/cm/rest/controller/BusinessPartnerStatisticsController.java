package com.ot.cm.rest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.BusinessPartnerService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("/businessPartner/stats")
public class BusinessPartnerStatisticsController extends BaseRestController {

	@Autowired
	private BusinessPartnerService businessPartnerService;

	@GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<Map<String, Integer>, ErrorResponse> statsByStatus(
			HttpServletRequest httpServletRequest) throws TGOCPBaseException {

		TGOCPRestResponse<Map<String, Integer>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<Map<String, Integer>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		responseDetails.setResponseEntity(businessPartnerService.statsByBPRStatus(userInfo.getCompanyId()));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}
