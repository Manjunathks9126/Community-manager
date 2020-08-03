package com.ot.cm.rest.controller.onboarding;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.business.service.TradingPartnerService;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.vo.UserInfo;
import com.ot.session.constants.TGOCPSessionHelperConstants;
import com.ot.session.entity.TGOCPSMUserVO;
import com.ot.session.entity.TGOCPSessionVO;

@RestController
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class TradingPartnerController {

	@Autowired
	TradingPartnerService tradingPartnerService;

	@Autowired
	OnboardingService onboardingService;

	@PostMapping(value = "/tradingpartner/psp", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-CPS-C", actionSummary = "Create Company through CPS", actionDetail = "Created company {0}. Workflow ID: {1}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> createTP(HttpServletRequest httpServletRequest,
			@RequestBody Workflow entity, @RequestParam String invitationId)
			throws CMApplicationException, TGOCPRestException, JAXBException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SuccessResponse, ErrorResponse>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();

		String invitationCode = onboardingService.getInvitationCode(invitationId,
				"gxs-gxsitcscrumteamhorizon@opentext.com");
		entity.getProcessingContext().setInvitationCode(invitationCode);
		entity.getProcessingContext().setInvitationSource(ApplicationConstants.CPS_INVITATION_SOURCE);

		SuccessResponse responseData = tradingPartnerService.createTP(entity, tgocpSMUserVO);

		responseDetails.setResponseEntity(responseData);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProcessingContext().getTpIdentifier().getCompanyName(),
				responseDetails.getResponseEntity().getTargetResourceRefId(), entity);

		return response;
	}

	@PostMapping(value = "/tradingpartner/psp/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-CPS-U", actionSummary = "Update Company through CPS", actionDetail = "Updated company {0}. Workflow ID: {1}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> initiateWorkflow(HttpServletRequest httpServletRequest,
			@RequestBody Workflow entity, @PathVariable("companyId") String companyId)
			throws CMApplicationException, TGOCPRestException, JAXBException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		entity.getProcessingContext().setInvitationSource(ApplicationConstants.CPS_INVITATION_SOURCE);

		SuccessResponse responseData = tradingPartnerService.initiateWorkFlow(entity, tgocpSMUserVO, companyId);

		responseDetails.setResponseEntity(responseData);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProcessingContext().getTpIdentifier().getCompanyName(),
				responseDetails.getResponseEntity().getTargetResourceRefId(), entity);

		return response;
	}

	@PostMapping(value = "/tradingpartner/save", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-C", actionSummary = "Create Company through CMS", actionDetail = "Created Company {0}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> saveTP(HttpServletRequest httpServletRequest,
			@RequestBody Workflow entity, @RequestParam String invitationId)
			throws CMApplicationException, TGOCPRestException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SuccessResponse, ErrorResponse>();
		UserInfo userInfo = (UserInfo) httpServletRequest.getSession()
				.getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);
		String loginCompanyId = userInfo.getCompanyId();
		boolean saveExtrernalReference = false;

		String invitationCode = onboardingService.getInvitationCode(invitationId,
				"gxs-gxsitcscrumteamhorizon@opentext.com");
		entity.getProcessingContext().setInvitationCode(invitationCode);

		responseDetails.setResponseEntity(tradingPartnerService.saveTP(entity, loginCompanyId, saveExtrernalReference));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProcessingContext().getTpIdentifier().getCompanyName(), entity);

		return response;
	}

	@PostMapping(value = "/tradingpartner/save/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-U", actionSummary = "Update Company", actionDetail = "Updated Company {0}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> editTP(HttpServletRequest httpServletRequest,
			@RequestBody Workflow entity, @PathVariable("companyId") String companyId) throws CMApplicationException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SuccessResponse, ErrorResponse>();

		responseDetails.setResponseEntity(tradingPartnerService.editTP(entity, companyId));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails,
				entity.getProcessingContext().getTpIdentifier().getCompanyName(), entity);

		return response;
	}

	@PostMapping(value = "/tradingpartner/verify", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> verifyTP(HttpServletRequest httpServletRequest,
			@RequestBody Workflow entity) throws TGOCPRestException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		responseDetails.setResponseEntity(tradingPartnerService.verifyTP(entity, tgocpSMUserVO));
		if (null != responseDetails.getResponseEntity())
			responseDetails.setSuccess(true);
		else {
			responseDetails.setSuccess(false);
		}
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}

	@GetMapping(value = "/getKeyBankmaps", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map> getKeyBankMaps(HttpServletRequest httpServletRequest) throws CMApplicationException {

		return tradingPartnerService.getKeyBankMaps();
	}

	// Core Service Changes for EW demo

	@PostMapping(value = "/tradingpartner/core", produces = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-CPS-C", actionSummary = "Create Company through CPS", actionDetail = "Created company {0}. Workflow ID: {1}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> createTPForCore(HttpServletRequest httpServletRequest,
			@RequestBody Workflow workflow, @RequestParam String invitationId)
			throws CMApplicationException, TGOCPRestException, JAXBException {
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		String invitationCode = onboardingService.getInvitationCode(invitationId,
				"gxs-gxsitcscrumteamhorizon@opentext.com");
		workflow.getProcessingContext().setInvitationCode(invitationCode);
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<SuccessResponse, ErrorResponse>();
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		SuccessResponse responseData = tradingPartnerService.createTPForCore(workflow, tgocpSMUserVO);

		responseDetails.setResponseEntity(responseData);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails,
				workflow.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyName(),
				responseData, workflow);

		return response;
	}
}
