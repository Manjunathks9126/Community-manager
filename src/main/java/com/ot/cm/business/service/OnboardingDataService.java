package com.ot.cm.business.service;

import java.util.List;

import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.entity.VanProvidersInfo;
import com.ot.cm.vo.UserInfo;

public interface OnboardingDataService {

	String getCompaniesDetails(UserInfo userInfo, String ediAddress) throws TGOCPOnboardingRestClientException, TGOCPRestException;

	List<String> getEdiAddresses(String companyId) throws  TGOCPOnboardingRestClientException, TGOCPRestException;

	List<VanProvidersInfo> getVanProvidersList() throws TGOCPOnboardingRestClientException, TGOCPRestException;

	String validateTP(String companyName, String city, String countryCode, String vanProviderBuId, UserInfo userInfo,String companyParticipationType) throws TGOCPOnboardingRestClientException, TGOCPRestException;

}
