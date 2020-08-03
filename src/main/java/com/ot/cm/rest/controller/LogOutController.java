package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.rest.client.LogOutRestClient;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;


@RestController
public class LogOutController extends BaseRestController {
	
	@Autowired
	private LogOutRestClient logoutRestClient;

	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
	public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws CMApplicationException {
		String actionType = "logout";
		logoutRestClient.invokeSMFCC(httpServletRequest, httpServletResponse,actionType);
	}
	
	@RequestMapping(value = "/timeout", method = RequestMethod.POST, produces = "application/json")
	public void timeout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws CMApplicationException {
		String actionType = "timeout";
		logoutRestClient.invokeSMFCC(httpServletRequest, httpServletResponse,actionType);
	}

	@RequestMapping(value = "/relogin", method = RequestMethod.POST, produces = "application/json")
	public void reLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws CMApplicationException {
		String actionType = "relogin";
		logoutRestClient.invokeSMFCC(httpServletRequest, httpServletResponse,actionType);
	}
	
	@RequestMapping(value = "/keepAlive", method = RequestMethod.GET, produces = "application/json")
	public TGOCPRestResponseDetails<String, ErrorResponse> keepAlive(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws CMApplicationException {
		TGOCPRestResponseDetails<String, ErrorResponse> response = new TGOCPRestResponseDetails<String, ErrorResponse>();
		response.setResponseEntity("session reset successfull");
		response.setSuccess(true);
		response.setStatusMessage("session reset successfull");
		return response;
	}
	
}
