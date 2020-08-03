package com.ot.cm.rest.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v3.entity.PartnerTradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.TPDirectoryDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.BusinessPartnerFacades;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.BusinessPartnerRelationship;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Component
public class BusinessPartnerRestClient extends BaseRestClient {

	@Loggable
	/**
	 * 
	 * @param facades
	 * @return CMDSuccessResponse
	 */
	public CMDSuccessResponse createFacades(BusinessPartnerFacades facades, UserInfo userInfo)
			throws TGOCPRestException {

		String endpoint = globalProperties.getRest().getCmdURL() + "/rest/v1/businessPartnerFacades";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endpoint, HttpMethod.POST,
				new HttpEntity<BusinessPartnerFacades>(facades, cmdUserSession(userInfo)), CMDSuccessResponse.class);
		return details.getResponseDetails().getResponseEntity();
	}

	@Loggable
	/**
	 * 
	 * @param facades
	 * @return CMDSuccessResponse
	 */
	public CMDSuccessResponse editFacades(BusinessPartnerFacades facades, String businessPartnerFacadesId,
			UserInfo userInfo) throws TGOCPRestException {

		String endpoint = globalProperties.getRest().getCmdURL() + "/rest/v1/businessPartnerFacades/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endpoint, HttpMethod.PUT,
				new HttpEntity<BusinessPartnerFacades>(facades, cmdUserSession(userInfo)), CMDSuccessResponse.class,
				businessPartnerFacadesId);
		return details.getResponseDetails().getResponseEntity();
	}

	@Loggable
	/**
	 * 
	 * @param facades
	 * @return CMDSuccessResponse
	 */
	public BusinessPartnerFacades[] getFacades(String ownerBuId, String targetBuId, UserInfo userInfo)
			throws TGOCPRestException {

		String endpoint = globalProperties.getRest().getCmdURL()
				+ "/rest/v1/businessPartnerFacades?ownerBuId={ownerBuId}&targetBuId={targetBuId}";
		TGOCPRestResponse<BusinessPartnerFacades[], ErrorResponse> details = restTemplate.exchange(endpoint,
				HttpMethod.GET, new HttpEntity<String>("headers", cmdUserSession(userInfo)),
				BusinessPartnerFacades[].class, ownerBuId, targetBuId);
		return details.getResponseDetails().getResponseEntity();
	}

	@Loggable
	/**
	 * 
	 * @param filterQuery
	 * @param queryParams
	 * @param companyId
	 * @return Integer
	 * @throws TGOCPRestException
	 */
	public Integer getBPRsCount(String filterQuery, Map<String, String> queryParams, String companyId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();

		if (null != queryParams && queryParams.size() > 0) {
			endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/companies/");
			endPoint.append(companyId);
			endPoint.append("/partnerdirectory");
			RequestPayload<Integer> payload = new RequestPayload<>(endPoint.toString(), queryParams, HttpMethod.GET,
					new HttpEntity<String>("headers", basicHeaders()), new ParameterizedTypeReference<Integer>() {
					});

			return restTemplate.exchange(payload, ErrorResponse.class);

		} else {
			endPoint.append(globalProperties.getRest().getCmsURL())
					.append("/v3/companies/{companyId}/partnerdirectory");
			endPoint.append(filterQuery);
			TGOCPRestResponse<Integer, ErrorResponse> resp = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
					new HttpEntity<String>("headers", basicHeaders()), Integer.class, companyId);
			return resp.getResponseDetails().getResponseEntity();
		}

	}


	@Async
	public CompletableFuture<Integer> getBPRsCountCompletable(String filterQuery, String companyId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
			endPoint.append("/v3/companies/{companyId}/partnerdirectory");
			endPoint.append(filterQuery);
			TGOCPRestResponse<Integer, ErrorResponse> resp = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
					new HttpEntity<String>("headers", basicHeaders()), Integer.class, companyId);
			return CompletableFuture.completedFuture(resp.getResponseDetails().getResponseEntity());
	}

	public Integer getBPRsCount(String filterQuery, String companyId) throws TGOCPRestException {
		return getBPRsCount(filterQuery, null, companyId);
	}

	/**
	 * 
	 * @param queryParams
	 * @param companyId
	 * @return TPDirectoryDetailsType[]
	 * @throws TGOCPRestException
	 * 
	 */
	@Loggable
	public List<TPDirectoryDetailsType> getBPRList(Map<String, String> queryParams, String companyId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		// GET /v3/companies/{companyId}/partnerdirectory
		endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/companies/");
		endPoint.append(companyId);
		endPoint.append("/partnerdirectory/");
		RequestPayload<List<TPDirectoryDetailsType>> payload = new RequestPayload<>(endPoint.toString(), queryParams,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<List<TPDirectoryDetailsType>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class);
	}

	/**
	 * @param queryParam
	 * @param companyId
	 * @return Integer
	 * @throws TGOCPRestException
	 */
	@Loggable
	public Integer getEdiCount(String queryParam, String companyId, String bprId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/companies/");
		endPoint.append(companyId);
		endPoint.append("/partnerdirectory/");
		endPoint.append(bprId);
		endPoint.append("/tradingaddresses");
		TGOCPRestResponse<Integer, ErrorResponse> resp = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
				new HttpEntity<String>("headers", basicHeaders()), Integer.class, companyId);
		return resp.getResponseDetails().getResponseEntity();
	}

	/**
	 * @param queryParam
	 * @param companyId
	 * @return BaseTradingAddressType
	 * @throws TGOCPRestException
	 */
	@Loggable
	public PartnerTradingAddressType getEdiList(String queryParam, String companyId, String bprId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmsURL()).append("/v3/companies/");
		endPoint.append(companyId);
		endPoint.append("/partnerdirectory/");
		endPoint.append(bprId);
		endPoint.append("/tradingaddresses");
		TGOCPRestResponse<PartnerTradingAddressType, ErrorResponse> resp = restTemplate.exchange(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), PartnerTradingAddressType.class,
				companyId);
		return resp.getResponseDetails().getResponseEntity();
	}

	@Loggable
	/**
	 * 
	 * @param facades
	 * @return CMDSuccessResponse
	 */
	public CMDSuccessResponse deleteFacade(String businessPartnerFacadesId, UserInfo userInfo)
			throws TGOCPRestException {

		String endpoint = globalProperties.getRest().getCmdURL() + "/rest/v1/businessPartnerFacades/{id}";
		TGOCPRestResponse<CMDSuccessResponse, ErrorResponse> details = restTemplate.exchange(endpoint,
				HttpMethod.DELETE, new HttpEntity<>("", cmdUserSession(userInfo)), CMDSuccessResponse.class,
				businessPartnerFacadesId);
		return details.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public List<BusinessPartnerRelationship> getBPRListbybuids(String bprId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder();
		endPoint.append(globalProperties.getRest().getCmdURL())
				.append("/rest/v1/businessPartnerRelationships?uniqueId=");
		endPoint.append(bprId);
		RequestPayload<List<BusinessPartnerRelationship>> payload = new RequestPayload<>(endPoint.toString(), null,
				HttpMethod.GET, new HttpEntity<>("", cmdAppSession()),
				new ParameterizedTypeReference<List<BusinessPartnerRelationship>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class);
	}

}
