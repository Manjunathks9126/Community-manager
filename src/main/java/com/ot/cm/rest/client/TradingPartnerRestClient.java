package com.ot.cm.rest.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.BusinessPartnerRelationshipType;
import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.gxs.services.bsapi.rs.v2.entity.UserSubcriptionDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitType;
import com.gxs.services.bsapi.rs.v3.entity.RegistrationRequestType;
import com.gxs.services.bsapi.rs.v3.entity.TradingPartnerRelationshipType;
import com.gxs.services.imapi.client.ImBusinessPartnerMgr;
import com.gxs.services.imapi.client.ImBusinessPartnerMgrInterface;
import com.gxs.services.imapi.client.ImRestException;
import com.gxs.services.imapi.common.domain.ImBusinessPartnerRelData;
import com.gxs.services.imapi.common.entities.ImEmbeddedBunit;
import com.gxs.services.imapi.common.entities.ImEmbeddedCommunity;
import com.gxs.services.imapi.common.entities.ImRestBusinessPartnerRel;
import com.gxs.services.imapi.common.entities.ImRestSuccessResponse;
import com.ot.cm.business.service.GlobalDirectoryService;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;
import com.ot.session.entity.TGOCPSMUserVO;

@Component
public class TradingPartnerRestClient extends BaseRestClient {

	@Autowired
	GlobalDirectoryService searchService;

	@Loggable
	public SuccessResponse createTP(BusinessUnitType businessUnitType, boolean validate) throws TGOCPRestException {
		// making true, considering always to allow duplicate creation
		businessUnitType.setAllowDuplicates(true);

		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		if (validate) {
			endPoint.append("/v3/companies?validate_only=true");
		} else {
			endPoint.append("/v3/companies");
		}
		HttpEntity<BusinessUnitType> requestEntity = new HttpEntity<>(businessUnitType, basicHeaders());
		TGOCPRestResponse<SuccessResponse, ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.POST, requestEntity, SuccessResponse.class, RestActionContext.CR_TP_DETAILS, null);
		return result.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String createBPR(BusinessPartnerRelationshipType bprtype, String initiator) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/" + initiator + "/businessPartnerReleationship");
		HttpEntity<BusinessPartnerRelationshipType> requestEntity = new HttpEntity<BusinessPartnerRelationshipType>(
				bprtype, basicHeaders());
		TGOCPRestResponse<SuccessResponse, ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.POST, requestEntity, SuccessResponse.class, RestActionContext.CR_BP_DETAILS);
		return result.getResponseDetails().getResponseEntity().getTargetResourceRefId();
	}

	@Loggable
	public TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> validateBPRExist(
			BusinessPartnerRelationshipType bprtype, String initiator) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/" + initiator + "/businessPartnerReleationship?validate_only=true");
		HttpEntity<BusinessPartnerRelationshipType> requestEntity = new HttpEntity<BusinessPartnerRelationshipType>(
				bprtype, basicHeaders());
		TGOCPRestResponse<SuccessResponse, ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.POST, requestEntity, SuccessResponse.class, RestActionContext.CR_BP_DETAILS);
		return result.getResponseDetails();
	}

	// this method is called for validating whether BPR Exist or Not

	@Loggable
	public TGOCPRestResponseDetails<BusinessUnitDetailsType[], ErrorResponse> validateTPExist(String name, String city,
			String country) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/companies?where_company_name=" + name + "&where_company_city=" + city
				+ "&where_company_country=" + country);
		HttpEntity<String> requestEntity = new HttpEntity<>(basicHeaders());
		TGOCPRestResponse<BusinessUnitDetailsType[], ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.GET, requestEntity, BusinessUnitDetailsType[].class, RestActionContext.CR_BP_DETAILS);
		return result.getResponseDetails();
	}

	@Loggable
	public String initiateCPSworkFlow(String companyRegistrationDataType, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCpsURL());
		endPoint.append("/v2/cop/companyRegistrations");
		HttpEntity<String> requestEntity = new HttpEntity<>(companyRegistrationDataType, cpsHeaders(tgocpsmUserVO));
		TGOCPRestResponse<com.opentext.services.cps.rest.entity.SuccessResponse, ErrorResponse> result = restTemplate
				.exchange(endPoint.toString(), HttpMethod.POST, requestEntity,
						com.opentext.services.cps.rest.entity.SuccessResponse.class,
						RestActionContext.CR_BU_REGISTRATION, null);
		return result.getResponseDetails().getResponseEntity().getTargetResourceRefId();
	}

	@Loggable
	public BusinessUnitDetailsType getCommunityDetails(String buId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v3/companies/" + buId;
		HttpEntity<String> requestType = new HttpEntity<>(basicHeaders());
		RequestPayload<BusinessUnitDetailsType> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestType,
				new ParameterizedTypeReference<BusinessUnitDetailsType>() {
				});
		BusinessUnitDetailsType restResponse = restTemplate.exchange(payload, ErrorResponse.class);
		return restResponse;

	}

	@Loggable
	public SuccessResponse editTP(BusinessUnitType businessUnitType, String companyId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/companies/" + companyId);
		HttpEntity<BusinessUnitType> requestEntity = new HttpEntity<>(businessUnitType, basicHeaders());
		TGOCPRestResponse<SuccessResponse, ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.PUT, requestEntity, SuccessResponse.class, RestActionContext.EDIT_TP_DETAILS, null);
		return result.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public UserSubcriptionDetailsType getUserSubscriptions(String companyId, String userId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmsURL()
				+ "/v2/companies/{companyId}/users/{userId}/subscriptions";
		TGOCPRestResponse<UserSubcriptionDetailsType, ErrorResponse> userSubscriptions = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), UserSubcriptionDetailsType.class,
				RestActionContext.GET_USERS_SUBSCRIPTION, null, companyId, userId); // "GC27373270SL",
		return userSubscriptions.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public List<Map> getKeyBankMaps() {
		String maps = appProperties.getKeybank_maps();
		String[] arr = appProperties.getKeybank_maps().split(",");

		List<String> mapslist = Arrays.asList(arr);
		List<Map> result = new ArrayList<Map>();
		for (int i = 0; i < mapslist.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			// map.put("label:"+mapslist.get(i),"value:"+mapslist.get(i));
			map.put("label", mapslist.get(i));
			map.put("value", mapslist.get(i));
			result.add(i, map);
		}
		return result;
	}

	@Loggable
	public RegistrationRequestType getCPSRegistrationData(String companyId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v3/cpsregistrations?where_company_id=" + companyId;
		TGOCPRestResponse<RegistrationRequestType, ErrorResponse> response = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), RegistrationRequestType.class, null,
				null); // "GC27373270SL",
		return response.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public com.ot.cm.rest.request.entity.RegistrationRequestType getCPSRegistrationDataFromRA(String tpId, String hubId)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getRaURL() + "/v1/registrationdata/" + tpId + "/" + hubId;

		TGOCPRestResponse<com.ot.cm.rest.request.entity.RegistrationRequestType, ErrorResponse> response = restTemplate
				.exchange(endPoint, HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
						com.ot.cm.rest.request.entity.RegistrationRequestType.class, null, null); // "GC27373270SL",
		return response.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public SuccessResponse[] createTPR(TradingPartnerRelationshipType tprtype, String initiator)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL());
		endPoint.append("/v2/companies/" + initiator + "/tradingpartnerrelationships");
		HttpEntity<TradingPartnerRelationshipType> requestEntity = new HttpEntity<>(tprtype, basicHeaders());

		TGOCPRestResponse<SuccessResponse[], ErrorResponse> result = restTemplate.exchange(endPoint.toString(),
				HttpMethod.POST, requestEntity, SuccessResponse[].class);
		return result.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String createBPRwitCMD(String tpId, String status, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException, ImRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmdURL());
		endPoint.append("/rest/v1");
		ImBusinessPartnerMgrInterface imBusinessPartnerMgr = new ImBusinessPartnerMgr();
		ImBusinessPartnerRelData imBusinessPartnerRelData = null;
		ImRestSuccessResponse result = null;
		imBusinessPartnerMgr.setImrestServerUrl(endPoint.toString());
		Map<String, String> headerMap = TGOCPSessionUtil.prepareUserSessionHeadersMap(tgocpsmUserVO);
		imBusinessPartnerRelData = new ImBusinessPartnerRelData();
		imBusinessPartnerRelData.setInitiatorEntityId(tgocpsmUserVO.getParentID());
		imBusinessPartnerRelData.setPartnerEntityId(tpId);
		imBusinessPartnerRelData.setApprovalStatus(status);
		imBusinessPartnerRelData.setCommunityId(tgocpsmUserVO.getCommunityID());
		result = imBusinessPartnerMgr.createBusinessPartnerRel(imBusinessPartnerRelData, headerMap);
		// searchService.runDeltaQueryForBprs();// Added for solr data sync when
		// bpr is created
		return result.getUniqueId();
	}

	@Loggable
	public String updateBPRwitCMD(String bprId, String tpId, String status, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException, ImRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmdURL());
		endPoint.append("/rest/v1");
		ImBusinessPartnerMgrInterface imBusinessPartnerMgr = new ImBusinessPartnerMgr();
		ImRestBusinessPartnerRel imBusinessPartnerRelData = null;
		ImRestSuccessResponse result = null;
		imBusinessPartnerMgr.setImrestServerUrl(endPoint.toString());
		Map<String, String> headerMap = TGOCPSessionUtil.prepareUserSessionHeadersMap(tgocpsmUserVO);
		imBusinessPartnerRelData = new ImRestBusinessPartnerRel();
		ImEmbeddedBunit initiatorBU = new ImEmbeddedBunit();
		initiatorBU.setBuId(tgocpsmUserVO.getParentID());
		imBusinessPartnerRelData.setInitiatorEntity(initiatorBU);
		ImEmbeddedBunit partnerBU = new ImEmbeddedBunit();
		partnerBU.setBuId(tpId);
		imBusinessPartnerRelData.setPartnerEntity(partnerBU);
		imBusinessPartnerRelData.setApprovalStatus(status);
		ImEmbeddedCommunity imEmbeddedCommunity = new ImEmbeddedCommunity();
		imEmbeddedCommunity.setCommunityId(tgocpsmUserVO.getCommunityID());
		imBusinessPartnerRelData.setCommunity(imEmbeddedCommunity);
		result = imBusinessPartnerMgr.updateBusinessPartnerRel(bprId, imBusinessPartnerRelData, headerMap);
		return result.getDescription();
	}

	public TradingPartnerRelationshipDetailsEntity[] getHubTradingPartnerList(String ownerBuId, String tradingAddress)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/tradingpartnerrelationships/?ownerCompanyId=" + ownerBuId + "&partnerTradingAddress="
				+ tradingAddress);
		try {
			RequestPayload<TradingPartnerRelationshipDetailsEntity[]> payload = new RequestPayload<>(
					endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
					new ParameterizedTypeReference<TradingPartnerRelationshipDetailsEntity[]>() {
					});
			TradingPartnerRelationshipDetailsEntity[] ts = restTemplate.exchange(payload, ErrorResponse.class);
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TradingPartnerRelationshipDetailsEntity[] getTpTradingPartnerList(String ownerBuId, String tradingAddress)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/tradingpartnerrelationships/?partnerCompanyId=" + ownerBuId + "&ownerTradingAddress="
				+ tradingAddress);
		try {
			RequestPayload<TradingPartnerRelationshipDetailsEntity[]> payload = new RequestPayload<>(
					endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
					new ParameterizedTypeReference<TradingPartnerRelationshipDetailsEntity[]>() {
					});
			TradingPartnerRelationshipDetailsEntity[] ts = restTemplate.exchange(payload, ErrorResponse.class);
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Count of BPR
	public CMDSuccessResponse bprCount(String initiator, String partnerCompanyId, UserInfo userInfo)
			throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL()
				+ "/rest/v1/businessPartnerRelationships?initiatorEntityId={initiator}&partnerEntityId={partnerCompanyId}&countOnly=true";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<CMDSuccessResponse>(cmdUserSession(userInfo)), CMDSuccessResponse.class, initiator,
				partnerCompanyId);
		return details.getResponseDetails().getResponseEntity();
	}

}
