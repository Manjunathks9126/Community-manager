package com.ot.cm.rest.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.ot.cm.exception.CMApplicationException;


@Component
public class LogOutRestClient extends BaseRestClient {

	public static final String SM_SERVER_ERROR = "TGOCPSM-5000";
	public LogOutRestClient() {

	}

	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws Exception
	 */
	public void invokeSMFCC(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			String actionType) throws CMApplicationException {
		try {
			httpServletRequest.getSession(true).invalidate();

			switch (actionType) {
			case "logout": {
				httpServletResponse.sendRedirect(globalProperties.getLogoutURL());
				break;
			}
			case "timeout": {
				httpServletResponse.sendRedirect(globalProperties.getTimeoutURL());
				break;
			}
			case "relogin": {
				httpServletResponse.sendRedirect(globalProperties.getReloginURL());
				break;
			}
			}

		} catch (Exception e) {
			throw new CMApplicationException(SM_SERVER_ERROR, "LogoutClient.invokeSMFCC()", e);
		}
	}
}
