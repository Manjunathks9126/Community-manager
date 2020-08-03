package com.ot.cm.rest.controller.onboarding.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.OnboardingDataService;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.response.entity.VanProvidersInfo;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("v1/onboardingservices")
public class OnboardingLookupController extends BaseRestController {

	@Autowired
	private OnboardingDataService  onboardingDataService;
	
	@Autowired
	LocalizationMessages localizationMessages;

	@GetMapping(value = "/tradingaddresses/{ediAddress}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCompaniesByEdiInfo(HttpServletRequest httpServletRequest,
			@PathVariable("ediAddress") String ediAddress) throws TGOCPOnboardingRestClientException, TGOCPRestException {

		UserInfo userInfo = (UserInfo)httpServletRequest.getSession().getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		return onboardingDataService.getCompaniesDetails(userInfo,ediAddress);
	}


	@GetMapping(value = "/tradingaddresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getEDIaddressList(HttpServletRequest request)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {

		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		return onboardingDataService.getEdiAddresses(userInfo.getCompanyId());
	}
	
	@GetMapping(value = "/vanProviders", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VanProvidersInfo> getVanProvidersList(
			HttpServletRequest httpServletRequest)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		

		return onboardingDataService.getVanProvidersList();
	}
	
}
