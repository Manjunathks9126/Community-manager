package com.ot.cm.rest.controller;

import javax.servlet.http.HttpServletRequest;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.UserService;
import com.ot.cm.cms.response.entity.User;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;

@RestController
@RequestMapping("users")
public class UserController extends BaseRestController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<User, ErrorResponse> getUserDetails(HttpServletRequest httpServletRequest,
			@PathVariable("userId") String userId) throws TGOCPBaseException {
		TGOCPRestResponse<User, ErrorResponse> response = null;
		TGOCPRestResponseDetails<User, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<User, ErrorResponse>();
		responseDetails.setResponseEntity(userService.getUserInfo(userId));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<User, ErrorResponse> getLoggedInUserDetails(HttpServletRequest httpServletRequest) throws TGOCPRestException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		TGOCPRestResponseDetails<User, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		responseDetails.setResponseEntity(userService.getUserInfo(userInfo.getUserId()));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		TGOCPRestResponse<User, ErrorResponse> response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}

}
