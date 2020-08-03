package com.ot.cm.rest.controller;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.helper.GlobalSearchHelper;
import com.ot.cm.helper.TileContentHelper;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.vo.TileContentDetail;
import com.ot.cm.vo.TileLink;
import com.ot.cm.vo.UserInfo;

@RestController
public class AccessControlController {

	@Autowired
	NeoRestClient neoRestClient;

	@Autowired
	GlobalSearchHelper globalSearchHelper;

	@Autowired
	TileContentHelper tileContentHelper;

	@GetMapping(value = "tile/content", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<TileContentDetail, ErrorResponse> getTileContent(
			HttpServletRequest httpServletRequest) throws TGOCPBaseException, ExecutionException, InterruptedException {

		TGOCPRestResponseDetails<TileContentDetail, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		UserInfo tgocpSMUserVO = (UserInfo) httpServletRequest.getSession()
				.getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		TileLink[] content = tileContentHelper.getTileContent(tgocpSMUserVO);

		responseDetails.setResponseEntity(new TileContentDetail(content));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return responseDetails;
	}

	@GetMapping(value = "permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<String[], ErrorResponse> getPermissions(HttpServletRequest httpServletRequest)
			throws TGOCPBaseException {

		TGOCPRestResponseDetails<String[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String[], ErrorResponse>();
		UserInfo tgocpSMUserVO = (UserInfo) httpServletRequest.getSession()
				.getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		String[] permissions = neoRestClient.getPermissions(tgocpSMUserVO.getCompanyId(), tgocpSMUserVO.getUserId(),
				tgocpSMUserVO.getServiceInstanceID());
		responseDetails.setResponseEntity(permissions);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return responseDetails;
	}

}
