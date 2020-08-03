package com.ot.cm.rest.controller.onboarding.services;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.rest.controller.BaseRestController;

@RestController
@RequestMapping("v1/onboardingservices/messages")
public class OnboardingMessagesController extends BaseRestController {

	@GetMapping(value = "/segmentTerminator", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSegmentTerminator(HttpServletRequest request) throws TGOCPOnboardingRestClientException {

		List<String> response = Arrays
				.asList(appProperties.getOnboarding().getTpProfileSegmentTerminator().split("\\^"));
		return response;
	}

	@GetMapping(value = "/elementSeparator", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getElementSeparator(HttpServletRequest request) throws TGOCPOnboardingRestClientException {

		List<String> response = Arrays
				.asList(appProperties.getOnboarding().getTpProfileElementSeparator().split("\\^"));

		return response;
	}

	@GetMapping(value = "/subElementSeparator", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSubElementSeparator(HttpServletRequest request) throws TGOCPOnboardingRestClientException {

		List<String> response = Arrays
				.asList(appProperties.getOnboarding().getTpProfileSubelementSeparator().split("\\^"));
		return response;
	}

}
