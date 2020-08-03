package com.ot.cm.rest.controller.onboarding.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.OnboardingDataService;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("v1/onboardingservices")
public class OnboardingValidationsController extends BaseRestController {

	@Autowired
	private OnboardingDataService  onboardingDataService;

	
	
	@GetMapping(value = "/validatetp", produces = MediaType.APPLICATION_JSON_VALUE)
	public String validateTPExist(
			HttpServletRequest request, @RequestParam("companyName") String companyName,
			@RequestParam("city") String city, @RequestParam("countryCode") String countryCode,@RequestParam(value="vanProviderBuId",required=false) String vanProviderBuId
			,@RequestParam(value="companyParticipationType",required=false) String companyParticipationType)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {

		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		String response = onboardingDataService.validateTP(companyName, city, countryCode, vanProviderBuId,userInfo,companyParticipationType);
		return response.toString();
	}

}
