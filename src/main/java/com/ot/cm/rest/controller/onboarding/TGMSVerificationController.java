package com.ot.cm.rest.controller.onboarding;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gxs.services.bsapi.rs.v1.entity.RetrieveVANProviderResponse;
import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.imapi.client.ImRestException;
import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.business.service.TGMSVerifiactionService;
import com.ot.cm.business.service.TradingPartnerDetailsService;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.EDIAddressRestClient;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.session.constants.TGOCPSessionHelperConstants;
import com.ot.session.entity.TGOCPSMUserVO;
import com.ot.session.entity.TGOCPSessionVO;

@RestController
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class TGMSVerificationController extends BaseRestController {

	@Autowired
	private EDIAddressRestClient ediAddressRestClient;

	@Autowired
	LocalizationMessages messageSource;

	@Autowired
	private TGMSVerifiactionService tgmsVerifiactionService;

	@Autowired
	OnboardingService onboardingService;

	@Autowired
	TradingPartnerDetailsService ediService;

	@PostMapping(value = "/ediInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<TradingAddressDetailsType, ErrorResponse> getEdiInfo(HttpServletRequest httpServletRequest,
			@RequestBody String ediAddress) throws TGOCPBaseException {

		TGOCPRestResponse<TradingAddressDetailsType, ErrorResponse> response = null;
		TGOCPRestResponseDetails<TradingAddressDetailsType, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		TradingAddressDetailsType tradingAddressDetailsType = ediAddressRestClient.getEdiInfo(ediAddress);

		responseDetails.setResponseEntity(tradingAddressDetailsType);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/ediList/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<Set<String>, ErrorResponse> getEdiList(HttpServletRequest request,
			@PathVariable("companyId") String companyId) throws TGOCPBaseException {

		TGOCPRestResponse<Set<String>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<Set<String>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		Set<String> ediList = ediService.getAllEdis(companyId);

		responseDetails.setResponseEntity(ediList);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/vanProviders", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<RetrieveVANProviderResponse, ErrorResponse> getVanProvidersList(
			HttpServletRequest httpServletRequest)
			throws TGOCPBaseException, JsonGenerationException, JsonMappingException, IOException {

		TGOCPRestResponse<RetrieveVANProviderResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<RetrieveVANProviderResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		RetrieveVANProviderResponse vanProvidersList = ediAddressRestClient.getVANProvidersList();

		responseDetails.setResponseEntity(vanProvidersList);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/companyediaddresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<TradingAddressDetailsType[], ErrorResponse> getEDIaddressList(@RequestParam String after,
			HttpServletRequest request)
			throws TGOCPBaseException, JsonGenerationException, JsonMappingException, IOException {

		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);

		TGOCPRestResponse<TradingAddressDetailsType[], ErrorResponse> response = null;
		TGOCPRestResponseDetails<TradingAddressDetailsType[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<TradingAddressDetailsType[], ErrorResponse>();

		TradingAddressDetailsType[] ediList = ediAddressRestClient.getEDIAddresses(userInfo.getCompanyId(),
				"?after=" + after + "&limit=20");

		responseDetails.setResponseEntity(ediList);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/msgDetailseparator", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<Map<String, List<String>>, ErrorResponse> getSeparator(HttpServletRequest request)
			throws TGOCPBaseException, JsonGenerationException, JsonMappingException, IOException {

		TGOCPRestResponse<Map<String, List<String>>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<Map<String, List<String>>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		Map<String, List<String>> separatorMap = new HashMap<>();

		separatorMap.put("seg_terminator", Arrays.asList(appProperties.getTpProfileSegmentTerminator().split("\\^")));
		separatorMap.put("elem_separator", Arrays.asList(appProperties.getTpProfileElementSeparator().split("\\^")));
		separatorMap.put("sub_elem_separator",
				Arrays.asList(appProperties.getTpProfileSubelementSeparator().split("\\^")));

		responseDetails.setResponseEntity(separatorMap);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@PostMapping(value = "/initiatevantpflow", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-C/A", actionSummary = "Create Company and/or Add Edi to existing Company through CMS", actionDetail = "{0} Company {1}. Workflow ID: {2}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> initiatevantpflow(@RequestBody Workflow entity,
			@RequestParam String invitationId, HttpServletRequest httpServletRequest)
			throws JAXBException, CMApplicationException, TGOCPRestException, ImRestException {
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		attachInvitationCode(invitationId, entity, tgocpSMUserVO);

		responseDetails.setResponseEntity(tgmsVerifiactionService.createTP(entity, tgocpSMUserVO, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		String actionDesc = messageSource.getMessage("COMPANY_CREATION_SUCCESS", userInfo.getLocale());
		if (tgmsVerifiactionService.existingCompanyRequest(entity.getProvisioningRequestData().getTpRequestType())) {
			actionDesc = messageSource.getMessage("EXISTING_EDIT_SUCESS", userInfo.getLocale());
		}
		response = new TGOCPRestResponse<>(responseDetails, actionDesc,
				entity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyName(),
				responseDetails.getResponseEntity().getTargetResourceRefId(), entity);
		return response;
	}

	@GetMapping(value = "/reviewTpInfo/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Workflow reviewTPInfo(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("companyId") String companyId, HttpServletRequest httpServletRequest)
			throws TGOCPBaseException, JAXBException {

		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		return tgmsVerifiactionService.getWorkflowEntity(companyId, tgocpSMUserVO.getParentID());
	}

	@GetMapping(value = "/validatebpr/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> validateBPRExist(HttpServletRequest request,
			@PathVariable("companyId") String companyId) throws TGOCPBaseException, JAXBException {

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(request);
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> response;
		response = tgmsVerifiactionService.validateBPRExist(companyId, userInfo.getCompanyId());

		return response;
	}

	@GetMapping(value = "/validatetpExist", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponseDetails<BusinessUnitDetailsType[], ErrorResponse> validateTPExist(
			HttpServletRequest request, @RequestParam("companyName") String companyName,
			@RequestParam("city") String city, @RequestParam("country") String country)
			throws TGOCPBaseException, JAXBException {

		TGOCPRestResponseDetails<BusinessUnitDetailsType[], ErrorResponse> response;
		response = tgmsVerifiactionService.validateTPExist(companyName, city, country);

		return response;
	}

	@PostMapping(value = "/approvetp/{bprId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-CPS-C", actionSummary = "Approving TP through CMS", actionDetail = "Updated Company {0}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> approveTp(@RequestBody Workflow entity,
			HttpServletRequest httpServletRequest, @PathVariable("bprId") String bprId)
			throws CMApplicationException, ImRestException {
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SuccessResponse, ErrorResponse>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		boolean validation = true;
		try {
			responseDetails.setResponseEntity(tgmsVerifiactionService.approveTp(entity, tgocpSMUserVO, bprId));
		} catch (CMApplicationException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (TGOCPRestException e) {
			if (e.getRestErrorCode().equals("BSAPI-2100")) {
				validation = false;
			} else {
				throw new CMApplicationException("TGOCPAPP-5000", e.getErrorLog(), e.getCause());
			}
		}
		if (validation)
			responseDetails.setSuccess(true);
		else
			responseDetails.setSuccess(false);
		responseDetails.setHttpStatus(HttpStatus.OK);
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyName(), entity);
		return response;
	}

	@PostMapping(value = "/initiateTGMSflow", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-C", actionSummary = "Create Company", actionDetail = "Created company {0}. Workflow ID: {1}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> initiateTGMSflow(@RequestParam String invitationId,
			@RequestBody Workflow entity, HttpServletRequest httpServletRequest)
			throws TGOCPRestException, ImRestException, JAXBException, CMApplicationException {

		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();

		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

		attachInvitationCode(invitationId, entity, tgocpSMUserVO);

		responseDetails.setResponseEntity(tgmsVerifiactionService.createTP(entity, tgocpSMUserVO, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyName(),
				responseDetails.getResponseEntity().getTargetResourceRefId(), entity);
		return response;

	}

	private void attachInvitationCode(String invitationId, Workflow entity, TGOCPSMUserVO tgocpSMUserVO)
			throws TGOCPRestException {
		String invitationCode = onboardingService.getInvitationCode(invitationId, tgocpSMUserVO.getLogin());
		entity.getProcessingContext().setInvitationCode(invitationCode);
		entity.getProcessingContext().setInvitationSource(ApplicationConstants.CPS_INVITATION_SOURCE);
	}

	@GetMapping(value = "/provisioningMap")
	public Object getProvisioningMap() {
		return appProperties.getOnboardingMaps();
	}

	@GetMapping(value = "/tprList", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<TradingPartnerRelationshipDetailsEntity[], ErrorResponse> viewTradingPartner(
			HttpServletRequest httpServletRequest, @RequestParam("ownerBuId") String ownerBuId,
			@RequestParam String tradingAddress) throws TGOCPRestException {
		TGOCPRestResponseDetails<TradingPartnerRelationshipDetailsEntity[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TradingPartnerRelationshipDetailsEntity[] tprDetails = tgmsVerifiactionService
				.getTradingpartnerDetails(ownerBuId, tradingAddress);
		responseDetails.setResponseEntity(tprDetails);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		return new TGOCPRestResponse<>(responseDetails);
	}
}
