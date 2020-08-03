package com.ot.cm.business.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gxs.services.bsapi.rs.v3.entity.PreferredDateFormatType;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.OnboardingDataRestClient;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.response.entity.DateFormat;
import com.ot.cm.rest.response.entity.Language;
import com.ot.cm.rest.response.entity.Timezone;


@Service
public class OnboardingGeoGraphicServiceImpl implements OnboardingGeoGraphicService{
	
	@Autowired
	private OnboardingDataRestClient onboardingDataRestClient;
	
	@Autowired
	LocalizationMessages localizationMessages;
	
	@Cacheable("countryList")
	public List<Country> getCountriesList(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		
		return onboardingDataRestClient.getCountries(locale);
	}


	@Cacheable("languages")
	public List<Language> getLanguagesList(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		Language[] languages = onboardingDataRestClient.getLanguages(locale);
		List<Language> languagesList = Arrays.asList(languages);
		languagesList.sort(Comparator.comparing(Language::getLanguageDescription));
		List<Language> languageObjs = new ArrayList<>();
		for(int i=0; i<languagesList.size(); i++) {
			Language languageObj = new Language();
			languageObj.setLanguageCode(languagesList.get(i).getLanguageCode());
			languageObj.setLanguageDescription(languagesList.get(i).getLanguageDescription());
			languageObjs.add(languageObj);
		}
		
		return languageObjs;
	}

	@Cacheable("timeZones")
	public List<Timezone> getTimeZonesList(Locale locale) throws TGOCPRestException {
		List<Timezone> timezonesList = new ArrayList<>();
		List<Timezone> timezones = onboardingDataRestClient.getTimezones(locale);
		for(int i=0;i<timezones.size();i++) {
			Timezone timezone = new Timezone();
			timezone.setTimeZoneCode(timezones.get(i).getTimeZoneCode());
			timezone.setTimeZoneCodeDescription(timezones.get(i).getTimeZoneCodeDescription());
			timezonesList.add(timezone);
		}
		
		return timezonesList;
	}


	public List<DateFormat> getDateFormat(String locale) throws TGOCPOnboardingRestClientException{
		List<DateFormat> dateFormatList = new ArrayList<>();
		
		for (String format : ApplicationConstants.PREFERRED_DATE_FORMAT) {
			DateFormat date = new DateFormat();
			date.setFormatCode(PreferredDateFormatType.fromValue(format).name());
			date.setFormatExample(new SimpleDateFormat(format, new Locale((locale!=null?locale:"en"))).format(new Date()));
			dateFormatList.add(date);
		}
		return dateFormatList;
	}

	
}
