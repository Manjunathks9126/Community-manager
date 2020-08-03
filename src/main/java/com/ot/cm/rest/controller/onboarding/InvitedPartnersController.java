package com.ot.cm.rest.controller.onboarding;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.InvitedCompanyRequest;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("onboarding/invitedpartners")
public class InvitedPartnersController {

	@Autowired
	OnboardingService onboardingService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<InvitedCompanyRequest>, ErrorResponse> listInvitedPartners(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "countOnly", required = false) boolean countOnly,
			@RequestParam(value = "limit", required = false) Long limit,
			@RequestParam(value = "after", required = false) Long after,
			@RequestParam(value = "sortField", required = false) String sortField,
			@RequestParam(value = "sortOrder", required = false) Integer sortOrder)
			throws CMApplicationException, TGOCPRestException {

		TGOCPRestResponse<ListingQueryResponse<InvitedCompanyRequest>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<ListingQueryResponse<InvitedCompanyRequest>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<ListingQueryResponse<InvitedCompanyRequest>, ErrorResponse>();
		ListingQueryResponse<InvitedCompanyRequest> invitedList = null;
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		invitedList = onboardingService.listInvitedPartners(userInfo.getCompanyId(), limit, after, countOnly, sortField,
				sortOrder);

		responseDetails.setResponseEntity(invitedList);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}
}
