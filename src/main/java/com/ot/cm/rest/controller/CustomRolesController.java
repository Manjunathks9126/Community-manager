package com.ot.cm.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.CustomRolesService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.CMDNeoRole;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("customRoles")
@CheckPermissions(permissions = {PermissionConstants.VIEW_ONBOARDING, PermissionConstants.VIEW_CUST_FIELD})
public class CustomRolesController extends BaseRestController {
	@Autowired
	private CustomRolesService customRolesService;

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<CMDNeoRole>, ErrorResponse> getCustomRoles(HttpServletRequest httpServletRequest)
			throws TGOCPBaseException {
		TGOCPRestResponse<List<CMDNeoRole>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<List<CMDNeoRole>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<CMDNeoRole>, ErrorResponse>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(customRolesService.getCustomFieldRoles(userInfo.getCompanyId(), null));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(path = "/{roleId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<String>, ErrorResponse> getUsers(HttpServletRequest httpServletRequest,
			@PathVariable("roleId") String roleId) throws TGOCPBaseException {
		TGOCPRestResponse<List<String>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<List<String>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<String>, ErrorResponse>();
		responseDetails.setResponseEntity(
				customRolesService.getUsers(roleId, TGOCPSessionUtil.getUserInfoInSession(httpServletRequest)));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

}
