package com.ot.cm.rest.controller;

import static com.ot.cm.constants.ApplicationConstants.USER_ID;
import static com.ot.cm.constants.ApplicationConstants.USER_LOCALE;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("session")
public class SessionController extends BaseRestController {

	private final static Logger logger = LoggerFactory.getLogger(SessionController.class);

	@GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> getUserInfo(
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws TGOCPBaseException {

		TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> response = new TGOCPRestResponseDetails<>();
		Map<String, String> responseData = new HashMap<>();

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseData.put(USER_ID, userInfo.getUserId());
		responseData.put(USER_LOCALE, userInfo.getLocale().getLanguage());
		response.setResponseEntity(responseData);
		response.setSuccess(true);
		if (logger.isDebugEnabled()) {
			logger.debug("User from session : {}", userInfo);
		}
		return response;
	}

	@GetMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> companyInfo(
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws TGOCPBaseException {

		TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> response = new TGOCPRestResponseDetails<>();
		Map<String, String> responseData = new HashMap<>();

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseData.put("companyId", userInfo.getCompanyId());
		responseData.put("communityId", userInfo.getCommunityID());
		responseData.put("siId", userInfo.getServiceInstanceID());
		response.setResponseEntity(responseData);
		response.setSuccess(true);

		return response;
	}
}
