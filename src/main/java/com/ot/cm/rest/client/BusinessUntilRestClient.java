package com.ot.cm.rest.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.PointOfContactDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.session.annotation.Loggable;

import java.util.concurrent.CompletableFuture;

/**
 * The {@code BusinessUntilRestClient} class interface with CMS/CMD/RARest REST
 * APIs to get Company/BusinessUnit information.
 * 
 */
@Component
public class BusinessUntilRestClient extends BaseRestClient {

	@Loggable
	public BusinessUnitDetailsType getBusinessUnitDetailsType(String companyId) throws TGOCPBaseException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v3/companies/{companyId}";
		TGOCPRestResponse<BusinessUnitDetailsType, ErrorResponse> businessUnitDetails = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), BusinessUnitDetailsType.class,
				RestActionContext.GET_BU_DETAILS, null, companyId);

		return businessUnitDetails.getResponseDetails().getResponseEntity();
	}

	@Loggable
	@Async
	public CompletableFuture<BusinessUnitDetailsType> getBusinessUnitDetails(String companyId) throws TGOCPBaseException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v3/companies/{companyId}";
		TGOCPRestResponse<BusinessUnitDetailsType, ErrorResponse> businessUnitDetails = restTemplate.exchange(endPoint,
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()), BusinessUnitDetailsType.class,
				RestActionContext.GET_BU_DETAILS, null, companyId);
		return CompletableFuture.completedFuture(businessUnitDetails.getResponseDetails().getResponseEntity());
	}

	@Loggable
	public PointOfContactDetailsType[] getPointOfcontacts(String companyId) throws TGOCPBaseException {
		String endPoint = globalProperties.getRest().getCmsURL() + "/v2/companies/{companyId}/pointofcontacts";
		TGOCPRestResponse<PointOfContactDetailsType[], ErrorResponse> businessUnitDetails = restTemplate.exchange(
				endPoint, HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				PointOfContactDetailsType[].class, RestActionContext.GET_BU_DETAILS, null, companyId);

		return businessUnitDetails.getResponseDetails().getResponseEntity();
	}

}
