package com.ot.cm.rest.client;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ot.audit.vo.AuditInfo;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.NeoRestErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.NavigationFeatures;
import com.ot.cm.rest.response.entity.NeoRestResponseWrapper;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.vo.TileLink;
import com.ot.cm.vo.UserInfo;

/**
 * This class contains all the NEO API Rest calls.
 *
 * @author ddharmav
 */
@Component
public class NeoRestClient extends BaseRestClient {

	@Autowired
	protected RestTemplate basicRestTemplate;

	@Async
	public CompletableFuture<TileLink[]> getTileContent(UserInfo tgocpSMUserVO) throws TGOCPRestException {
		String userId = tgocpSMUserVO.getUserId();
		String serviceId = tgocpSMUserVO.getServiceInstanceID();
		Locale locale = tgocpSMUserVO.getLocale();
		String buId = tgocpSMUserVO.getCompanyId();
		String endPoint = globalProperties.getRest().getRaURL()
				+ "/v1/navigations/tiles/{buId}/user/{userId}/service/{serviceId}?locale={locale}";
		TGOCPRestResponse<TileLink[], ErrorResponse> tileContent = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession()), TileLink[].class, buId, userId, serviceId, locale);
		return CompletableFuture.completedFuture(tileContent.getResponseDetails().getResponseEntity());
	}

	public String[] getPermissions(String buId, String userId, String serviceId) throws TGOCPRestException {

		String endPoint = globalProperties.getRest().getRaURL()
				+ "/v1/permissions/businessunit/{buId}/user/{userId}/service/{serviceId}";
		TGOCPRestResponse<String[], ErrorResponse> tileContent = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession()), String[].class, buId, userId, serviceId);
		return tileContent.getResponseDetails().getResponseEntity();
	}

	public ListingQueryResponse<AuditInfo> getAuditLogs(String initiatorBUId, String custBUId, Boolean countOnly,
			Integer limit, Integer startsFrom) throws TGOCPRestException {

		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL() + "/v1/auditLog?userBuId="
				+ initiatorBUId + "&custBuId=" + custBUId);

		if (countOnly) {
			endPoint.append("&countOnly=true");
		}
		if (limit != null) {
			endPoint.append("&limit=").append(limit);
		}
		if (startsFrom != null) {
			endPoint.append("&startsFrom=").append(startsFrom);
		}

		RequestPayload<NeoRestResponseWrapper<ListingQueryResponse<AuditInfo>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<ListingQueryResponse<AuditInfo>>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public NavigationFeatures getTiles(UserInfo userInfo) throws TGOCPRestException {
		String endPoint = serviceURLResolver.RAREST("NAVIGATIONS.TILES", "V1");
		HttpEntity<UserInfo> requestEntity = new HttpEntity<>(userInfo, basicHeaders());
		ResponseEntity<NeoRestResponseWrapper<NavigationFeatures>> payload = basicRestTemplate.exchange(endPoint,
				HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<NavigationFeatures>>() {
				}, userInfo.getCompanyId(), userInfo.getUserId(), userInfo.getLocale());
		return payload.getBody().getResponseDetails().getResponseEntity();
	}

	public NavigationFeatures getHeaderFeatures(UserInfo userInfo) throws TGOCPRestException {
		// isHome hardcoded to false since portal is the home page for neo
		String endPoint = serviceURLResolver.RAREST("NAVIGATIONS.HAMBURGER", "V1");
		HttpEntity<UserInfo> requestEntity = new HttpEntity<>(userInfo, basicHeaders());
		ResponseEntity<NeoRestResponseWrapper<NavigationFeatures>> payload = basicRestTemplate.exchange(endPoint,
				HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<NavigationFeatures>>() {
				}, userInfo.getCompanyId(), userInfo.getUserId(), userInfo.getLocale(), false);
		return payload.getBody().getResponseDetails().getResponseEntity();
	}

}
