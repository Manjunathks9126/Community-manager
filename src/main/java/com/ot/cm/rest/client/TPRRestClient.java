package com.ot.cm.rest.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.TradingPartnerRelationshipDetailsType;
import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.session.annotation.Loggable;

@Component
public class TPRRestClient extends BaseRestClient {

	@Loggable
	public TradingPartnerRelationshipDetailsType[] getTPREDIAddresses(String searchQuery) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/tradingpartnerrelationships?");
		endPoint.append(searchQuery);
		TGOCPRestResponse<TradingPartnerRelationshipDetailsType[], ErrorResponse> response = restTemplate.exchange(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				TradingPartnerRelationshipDetailsType[].class, RestActionContext.TPR_EDI_ADDRESS, null);

		return response.getResponseDetails().getResponseEntity();
	}

	@Loggable
	public String getTPREDICount(String searchQuery) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getCmsURL());
		endPoint.append("/v3/tradingpartnerrelationships?");
		endPoint.append(searchQuery);
		TGOCPRestResponse<String, ErrorResponse> response = restTemplate.exchange(endPoint.toString(), HttpMethod.GET,
				new HttpEntity<String>("headers", basicHeaders()), String.class, RestActionContext.GET_EDI_COUNT, null);

		return response.getResponseDetails().getResponseEntity();
	}

}
