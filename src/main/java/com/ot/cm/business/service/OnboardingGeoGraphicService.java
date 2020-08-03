package com.ot.cm.business.service;

import java.util.List;
import java.util.Locale;

import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.response.entity.DateFormat;
import com.ot.cm.rest.response.entity.Language;
import com.ot.cm.rest.response.entity.Timezone;

public interface OnboardingGeoGraphicService {

	List<Country> getCountriesList(Locale language) throws TGOCPOnboardingRestClientException, TGOCPRestException;

	List<Language> getLanguagesList(Locale locale) throws TGOCPOnboardingRestClientException, TGOCPRestException;

	List<Timezone> getTimeZonesList(Locale locale) throws TGOCPRestException, TGOCPOnboardingRestClientException;

	List<DateFormat> getDateFormat(String locale) throws TGOCPOnboardingRestClientException;

	
}
