package com.ot.cm.rest.controller.onboarding.services;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.OnboardingGeoGraphicService;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.response.entity.DateFormat;
import com.ot.cm.rest.response.entity.Language;
import com.ot.cm.rest.response.entity.Timezone;

@RestController
@RequestMapping("v1/onboardingservices")
public class OnboardingGeoGraphicController extends BaseRestController {

	@Autowired
	private OnboardingGeoGraphicService onboardingGeographicService;

	@GetMapping(value = "/countries/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Country> getCountries(@PathVariable("locale") Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException  {
		return onboardingGeographicService.getCountriesList(locale);
	}

	
	@GetMapping(value = "/languages/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Language> getLanguages(@PathVariable("locale") Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException  {
		return onboardingGeographicService.getLanguagesList(locale);
	}
	
	
	@GetMapping(value = "/timezones/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Timezone> getTimezones(@PathVariable("locale") Locale locale) throws Exception  {
		return onboardingGeographicService.getTimeZonesList(locale);
	}
	
	
	@GetMapping(value = "/dateformats/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DateFormat> getDateFormat(@PathVariable("locale") String locale) throws TGOCPOnboardingRestClientException {
		return onboardingGeographicService.getDateFormat(locale);
		
	}
	
	
	
}
