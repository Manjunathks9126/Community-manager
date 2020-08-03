package com.ot.cm.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ge.gxs.icpx.utils.EncryptionException;
import com.ot.cm.business.service.TradingPartnerGraphService;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.vo.UserInfo;
import com.ot.session.constants.TGOCPSessionHelperConstants;
import com.ot.session.entity.TGOCPSMUserVO;
import com.ot.session.entity.TGOCPSessionVO;

@RestController
public class TradingPartnerGraphController extends BaseRestController {
	private static final Logger logger = LoggerFactory.getLogger(TradingPartnerGraphController.class);

	@Autowired
	private TradingPartnerGraphService tradingPartnerGraphService;

	@GetMapping(value = "/graphApplnData", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> getEncryptedUserDetailsForGraph(
			HttpServletRequest request) throws EncryptionException {

		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) request.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();

		String userDetails = tgocpSMUserVO.getParentID() + "," + tgocpSMUserVO.getLogin() + ","
				+ tgocpSMUserVO.getTimeZone();
		logger.debug(userDetails);
		TGOCPRestResponseDetails<Map<String, String>, ErrorResponse> response = new TGOCPRestResponseDetails<>();
		Map<String, String> responseEntity = new HashMap<>();
		response.setResponseEntity(responseEntity);
		response.setSuccess(true);
		responseEntity.put("param", tradingPartnerGraphService.getEncryptedUserDetails(userDetails));
		responseEntity.put("url", appProperties.getTpGraphAppURL());

		return response;
	}

	@GetMapping(value = "/tpGraphSubscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<Map<String, Boolean>, ErrorResponse> getTPgraphSubscription(
			HttpServletRequest request) throws TGOCPRestException {

		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);

		TGOCPRestResponseDetails<Map<String, Boolean>, ErrorResponse> response = new TGOCPRestResponseDetails<>();
		Map<String, Boolean> responseEntity = new HashMap<>();
		response.setResponseEntity(responseEntity);
		response.setSuccess(true);
		responseEntity.put("graphsubscription", tradingPartnerGraphService.getTPGraphSubsription(userInfo));

		return response;
	}

}
