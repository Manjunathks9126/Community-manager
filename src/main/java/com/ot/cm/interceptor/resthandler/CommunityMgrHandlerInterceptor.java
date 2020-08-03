/******************************************************************************
 All rights reserved. All information contained in this software is confidential
 and proprietary to opentext. No part of this software may be
 reproduced or transmitted in any form or any means, electronic, mechanical,
 photocopying, recording or otherwise stored in any retrieval system of any
 nature without the prior written permission of opentext.
 This material is a trade secret and its confidentiality is strictly maintained.
 Use of any copyright notice does not imply unrestricted public access to this
 material.

 (c) opentext

 *******************************************************************************
 Change Log:
 Date          Name                Defect#           Description
 -------------------------------------------------------------------------------
 08/04/2017    Madan                              Initial Creation
 ******************************************************************************/

package com.ot.cm.interceptor.resthandler;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ot.audit.vo.AuditInfo;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.session.constants.TGOCPSessionHelperConstants;
import com.ot.session.entity.TGOCPSMUserVO;
import com.ot.session.entity.TGOCPSessionVO;

/**
 * This class is responsible to intercept all requests before REST controller
 * execution.it is similar to Servlet filters.
 * 
 * @author Madan
 */
@Component
public class CommunityMgrHandlerInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CommunityMgrHandlerInterceptor.class);

	@Autowired
	AuditInfo auditInfo;

	@Autowired
	NeoRestClient neoRestClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		try {
			UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(request);
			TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) request.getSession()
					.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
			TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();

			if (null == userInfo) {

				userInfo = new UserInfo();

				userInfo.setUserId(tgocpSMUserVO.getPrincipalID());
				userInfo.setCompanyId(tgocpSMUserVO.getParentID());
				userInfo.setFirstName(tgocpSMUserVO.getFirstName());
				userInfo.setLastName(tgocpSMUserVO.getLastName());
				userInfo.setLocale(tgocpSMUserVO.getLocale());
				userInfo.setServiceInstanceID(tgocpSMUserVO.getServiceInstanceID());
				userInfo.setCommunityID(tgocpSMUserVO.getCommunityID());

				request.getSession().setAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY, userInfo);
			}

			auditInfo.setUserId(userInfo.getUserId());
			auditInfo.setAobUserId(tgocpSMUserVO.getImpersonatorLogin());
			auditInfo.setUserName(userInfo.getFirstName() + " " + userInfo.getLastName());
			auditInfo.setUserBuId(userInfo.getCompanyId());
			auditInfo.setServiceInstanceId(tgocpSMUserVO.getServiceInstanceID());
			auditInfo.setApplicationName(ApplicationConstants.AUDIT_APP_NAME);
			auditInfo.setSessionId(request.getSession().getId());

			return checkPermissions(userInfo, handler);

		} catch (TGOCPRestException ex) {
			throw new CMApplicationException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
					"CommunityMgrHandlerInterceptor.preHandle() UNAUTHORIZED", ex);
		} catch (Exception ex) {
			throw new CMApplicationException(HttpStatus.BAD_REQUEST, ErrorCodes.SESSION_NOT_FOUND,
					"CommunityMgrHandlerInterceptor.preHandle() Session Not Found", ex);
		}
	}

	/**
	 * This method checks if the REST END Point is Accessible to the Logged in
	 * User or Not based on the Check Permissions Annotation
	 * 
	 * @param userInfo
	 * @param handler
	 * @return
	 * @throws TGOCPRestException
	 */
	private boolean checkPermissions(UserInfo userInfo, Object handler) throws TGOCPRestException {

		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			CheckPermissions anno = null;

			// Check if Method is annotated if not check on Class
			anno = method.getMethodAnnotation(CheckPermissions.class);

			if (null == anno) {
				anno = method.getBeanType().getAnnotation(CheckPermissions.class);
			}

			if (null != anno && null != anno.permissions() && anno.permissions().length > 0) {
				List<String> permissionsToCheck = Arrays.asList(anno.permissions());
				List<String> permissions = Arrays.asList(neoRestClient.getPermissions(userInfo.getCompanyId(),
						userInfo.getUserId(), userInfo.getServiceInstanceID()));

				if (!permissionsToCheck.stream().distinct().filter(permissions::contains).findAny().isPresent()) {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, "TGOCPREST-400", null, null, null);
				}
			}
		}

		return true;
	}
}
