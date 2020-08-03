package com.ot.cm.rest.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.CMDNeoRole;
import com.ot.cm.rest.response.entity.CMDUser;
import com.ot.cm.vo.UserInfo;

@Component
public class NeoRolesRestClient extends BaseRestClient {

	public CMDNeoRole[] getBURoles(String buId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/neoRoles?businessUnitId={businessUnitId}";
		TGOCPRestResponse<CMDNeoRole[], ErrorResponse> neoRoles = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession()), CMDNeoRole[].class, buId);
		return neoRoles.getResponseDetails().getResponseEntity();
	}

	public CMDNeoRole[] getUserNeoRoles(String buId, String userId) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL()
				+ "/rest/v1/neoRoles?businessUnitId={businessUnitId}&userId={userId}";
		TGOCPRestResponse<CMDNeoRole[], ErrorResponse> neoRoles = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession()), CMDNeoRole[].class, buId, userId);
		return neoRoles.getResponseDetails().getResponseEntity();
	}

	public CMDUser[] getUsers(String roleId, UserInfo userInfo) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getCmdURL()
				+ "/rest/v1/neoRoles/{neoRoleId}/users/{businessUnitId}";
		TGOCPRestResponse<CMDUser[], ErrorResponse> neoRoles = restTemplate.exchange(endPoint, HttpMethod.GET,
				new HttpEntity<String>("headers", cmdAppSession()), CMDUser[].class, roleId, userInfo.getCompanyId());
		return neoRoles.getResponseDetails().getResponseEntity();
	}

}