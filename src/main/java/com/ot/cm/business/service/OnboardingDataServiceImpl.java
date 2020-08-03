package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gxs.services.bsapi.rs.v1.entity.RetrieveVANProviderResponse;
import com.gxs.services.bsapi.rs.v1.entity.VanProviderDetailsType;
import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.OnboardingDataRestClient;
import com.ot.cm.rest.response.entity.VanProvidersInfo;
import com.ot.cm.vo.UserInfo;


@Service
public class OnboardingDataServiceImpl implements OnboardingDataService{
	
	private static final Logger logger = LoggerFactory.getLogger(OnboardingDataServiceImpl.class);
	
	@Autowired
	private OnboardingDataRestClient onboardingDataRestClient;
	
	@Autowired
	LocalizationMessages localizationMessages;
	
	@Override
	public String getCompaniesDetails(UserInfo userInfo, String ediAddress) throws TGOCPOnboardingRestClientException, TGOCPRestException {
			JSONObject response = new JSONObject();
			TradingAddressDetailsType tradingAddressDetailsType = onboardingDataRestClient.getEdiInfo(ediAddress);

			if (tradingAddressDetailsType != null && tradingAddressDetailsType.getCompanyId() != null) {
				String companyId = tradingAddressDetailsType.getCompanyId();
				boolean isFromSameHub = validateSameHubCondition(companyId, userInfo.getCompanyId());
				if (isFromSameHub) {
					response.put("responseStatus", ApplicationConstants.RESPONSE_STATUS_INFO);
					response.put("responseMessage", localizationMessages.getMessage("EDI_SEARCH_SAME_COMPANY", HttpStatus.OK.value(), userInfo.getLocale()));
				}else {
					VanProvidersInfo vanProvidersInfo = getVanProvidersInfo(companyId);
					BusinessUnitDetailsType businessType = getBusinessUnitDetails(companyId);
					if (businessType != null) {
						response = formCompanyResponse(businessType, vanProvidersInfo, userInfo.getLocale(),tradingAddressDetailsType);
					} 
				}
			}
		return response.toString();
	}
	
	
	private BusinessUnitDetailsType getBusinessUnitDetails(String companyId) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		return onboardingDataRestClient.getBusinessUnitDetailsType(companyId);
	}


	private VanProvidersInfo getVanProvidersInfo(String companyId) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		TradingAddressDetailsType[] ediList = onboardingDataRestClient.getEDIAddresses(companyId,
				"?after=0&limit=1");
		VanProvidersInfo vanProvidersInfo = new VanProvidersInfo();
		if (ediList.length > 0) {
			vanProvidersInfo.setVanMailBoxId(ediList[0].getMailboxId());
			vanProvidersInfo.setVanProviderCompany(ediList[0].getVanProvider().getCompanyName());
		}
		return vanProvidersInfo;
	}


	@Override
	public List<String> getEdiAddresses(String companyId) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		TradingAddressDetailsType[] ediList = onboardingDataRestClient.getEDIAddresses(companyId);
		List<String> response = new ArrayList<>();
		if (ediList.length > 0) {
			response = formEdiAddressDetails(ediList);
		} else {
			logger.info("ediList is empty for companyId: " + companyId);
		}
		return response;
		
	}
	
	@Override
	public List<VanProvidersInfo> getVanProvidersList() throws TGOCPOnboardingRestClientException, TGOCPRestException {
		List<VanProvidersInfo> response = null;
		RetrieveVANProviderResponse vanProvidersList = onboardingDataRestClient.getVANProvidersList();
		
		if(vanProvidersList != null && vanProvidersList.getVanProviderDetail().size() > 0) {
			response = getVANproviderDetails(vanProvidersList.getVanProviderDetail());
		}
		return response;
	}
	
	@Override
	public String validateTP(String companyName, String city, String countryCode, String vanProviderBuId,UserInfo userInfo,String companyParticipationType) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		JSONObject response = new JSONObject();
		BusinessUnitDetailsType[] businessUnitDetails=onboardingDataRestClient.validateTPExist(companyName, city, countryCode);
		if((null == companyParticipationType || ApplicationConstants.VAN_PROVIDER.equalsIgnoreCase(companyParticipationType)) && (businessUnitDetails != null && businessUnitDetails.length >0)) {
				response = formValidateTPObj(businessUnitDetails,vanProviderBuId,userInfo);
		}else {
			response.put("responseStatus", ApplicationConstants.RESPONSE_STATUS_SUCCESS);
		}
		return response.toString();
	}
	
	private JSONObject formCompanyResponse(BusinessUnitDetailsType businessType, VanProvidersInfo vanProvidersInfo, Locale locale, TradingAddressDetailsType tradingAddressDetailsType) throws TGOCPOnboardingRestClientException {
		JSONObject responseWrapper = new JSONObject();
		responseWrapper.put("responseStatus", ApplicationConstants.RESPONSE_STATUS_SUCCESS);
		JSONObject response = new JSONObject();
		
		response.put("vanMailBoxId",tradingAddressDetailsType.getMailboxId() !=null ? tradingAddressDetailsType.getMailboxId():"");
		response.put("mailboxOwnerBuId", tradingAddressDetailsType.getMailboxOwnerCompanyId() !=null ? tradingAddressDetailsType.getMailboxOwnerCompanyId():"");
		response.put("ediOwnerBuId", tradingAddressDetailsType.getCompanyId() !=null ? tradingAddressDetailsType.getCompanyId():"");
		response.put("companyParticipationType", businessType.getCompanyParticipationType() !=null ? businessType.getCompanyParticipationType():"");
		response.put("vanProviderBuId", tradingAddressDetailsType.getVanProvider().getCompanyId() !=null ? tradingAddressDetailsType.getVanProvider().getCompanyId():"");
		
		switch(businessType.getCompanyParticipationType()) {
			case ApplicationConstants.VAN_PROVIDER:
				String[] vanProidersInfo = { vanProvidersInfo.getVanProviderCompany() };
				responseWrapper.put("responseMessage", localizationMessages.getMessage("EDI_VAN_PROVIDER", vanProidersInfo, locale));
				break;
			case ApplicationConstants.SERVICE_PROVIDER:
				response.put("serProviderBuId", businessType.getCompanyId() !=null ? businessType.getCompanyId():"");
				String[] serviceProvidersInfo = { businessType.getCompanyName() };
				responseWrapper.put("responseMessage", localizationMessages.getMessage("EDI_SERVICE_PROVIDER", serviceProvidersInfo, locale));
				break;
			default:
				response.put("companyName", businessType.getCompanyName() !=null ? businessType.getCompanyName():"");
				response.put("addressLine1", businessType.getCompanyAddress().getAddressLine1() !=null ? businessType.getCompanyAddress().getAddressLine1():"");
				response.put("addressLine2", businessType.getCompanyAddress().getAddressLine2() !=null ? businessType.getCompanyAddress().getAddressLine2():"");
				response.put("city", businessType.getCompanyAddress().getCity() !=null ? businessType.getCompanyAddress().getCity():"");
				response.put("state", businessType.getCompanyAddress().getState() !=null ? businessType.getCompanyAddress().getState():"");
				response.put("postalCode", businessType.getCompanyAddress().getPostalCode() !=null ? businessType.getCompanyAddress().getPostalCode():"");
				response.put("countryCode", businessType.getCompanyAddress().getCountryCode() !=null ? businessType.getCompanyAddress().getCountryCode():"");
				response.put("website", businessType.getCompanyWebsiteURL() !=null ? businessType.getCompanyWebsiteURL():"");
				response.put("tpCompanyId", tradingAddressDetailsType.getCompanyId() !=null ? tradingAddressDetailsType.getCompanyId():"");
		}
		JSONObject vanProviderObj = new JSONObject();
		if(vanProvidersInfo != null) {
			vanProviderObj.put("vanMailBoxId", vanProvidersInfo.getVanMailBoxId());
			vanProviderObj.put("vanProviderCompany", vanProvidersInfo.getVanProviderCompany());
			vanProviderObj.put("vanProviderBuId", tradingAddressDetailsType.getVanProvider().getCompanyId());
			response.put("vanProviders", vanProviderObj);
		}
		
		responseWrapper.put("responseEntity",response);
		
		return responseWrapper;
	}

	private List<String> formEdiAddressDetails(TradingAddressDetailsType[] ediList) throws TGOCPOnboardingRestClientException{
		List<String> ediAddressList = new ArrayList<>();
		for(int i=0 ; i<ediList.length ; i++) {
			String ediAddress = StringUtils.isEmpty(ediList[i].getTradingAddress().getQualifier()) ? ":"+ediList[i].getTradingAddress().getAddress() : ediList[i].getTradingAddress().getQualifier()+":"+ediList[i].getTradingAddress().getAddress();
			ediAddressList.add(ediAddress);
		}
		
		return ediAddressList;
		
	}

	public List<VanProvidersInfo> getVANproviderDetails(List<VanProviderDetailsType> list) {
		ArrayList<VanProvidersInfo> vanProviderList = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			VanProvidersInfo vanProviderObj = new VanProvidersInfo();
			vanProviderObj.setVanMailBoxId(list.get(i).getMailboxId());
			vanProviderObj.setVanProviderCompany(list.get(i).getVanProvider().getCompanyName());
			vanProviderObj.setVanProviderBuId(list.get(i).getVanProvider().getCompanyId());
			vanProviderList.add(vanProviderObj);
		}
		return vanProviderList;
	}

	private JSONObject formValidateTPObj(
			BusinessUnitDetailsType[] businessUnitDetailsType,String vanProviderBuId, UserInfo userInfo) throws TGOCPOnboardingRestClientException, TGOCPRestException {
		JSONObject validateTPresponse = new JSONObject();
		
		JSONObject responseEntity = new JSONObject();
		
		if(businessUnitDetailsType.length == 1 && (ApplicationConstants.STATUS_ACTIVE).equals(businessUnitDetailsType[0].getStatus().toUpperCase())) {
			String companyId = businessUnitDetailsType[0].getCompanyId();
			
			validateTPresponse.put("responseStatus", ApplicationConstants.RESPONSE_STATUS_SUCCESS);
			TradingAddressDetailsType[] ediList = onboardingDataRestClient.getEDIAddresses(companyId,
					"?after=0&limit=1");
			if(ediList.length > 0 && vanProviderBuId.equals(ediList[0].getVanProvider().getCompanyId())) {
				responseEntity.put("tpCompanyId", businessUnitDetailsType[0].getCompanyId());
				validateTPresponse.put("responseEntity", responseEntity);
			}
		}else if(businessUnitDetailsType.length > 1) {
			validateTPresponse.put("responseStatus", ApplicationConstants.RESPONSE_STATUS_WARNING);
			validateTPresponse.put("responseMessage", localizationMessages.getMessage("TP_VALIDATION", userInfo.getLocale()));
		}
		
		return validateTPresponse;
	}

	private boolean validateSameHubCondition(String companyId, String loggedInCompanyId) {
		return companyId.equalsIgnoreCase(loggedInCompanyId);
	}

	
}
