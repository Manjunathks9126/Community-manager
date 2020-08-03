package com.ot.cm.rest.controller.onboarding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.request.entity.ReinviteEntity;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequestData;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("workflow/invitations")
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class WorkflowInvitationsController {

	@Autowired
	OnboardingService onboardingService;

	@Autowired
	ApplicationProperties appProperties;

	@Autowired
	private BusinessUntilRestClient businessUntilRestClient;

	@GetMapping(value = "/{workflowId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<InvitationNeoRest>, ErrorResponse> listInvitations(
			@PathVariable("workflowId") String workflowId,
			@RequestParam(value = "invitationStatus", required = false) String[] invitationStatus)
			throws CMApplicationException, TGOCPRestException {

		TGOCPRestResponse<List<InvitationNeoRest>, ErrorResponse> response = null;
		TGOCPRestResponseDetails<List<InvitationNeoRest>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<InvitationNeoRest>, ErrorResponse>();
		List<InvitationNeoRest> invitationsList = null;
		invitationsList = onboardingService.listInvitations(workflowId, invitationStatus);

		responseDetails.setResponseEntity(invitationsList);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-ONBOARD-WRKFLW-INVTN-C", actionSummary = "Workflow invitation created", actionDetail = "Created workflow invitation {0} for Company {1}", hasContent = true)
	@PostMapping(value = "/{workflowId}", consumes = { "multipart/form-data" })
	public TGOCPRestResponse<String, ErrorResponse> createOrUpdateInvitation(HttpServletRequest httpServletRequest,
			@RequestPart("invitation") InvitationNeoRest invitationEntity,
			@RequestPart(required = false, value = "file") MultipartFile file,
			@PathVariable("workflowId") String workflowId) throws TGOCPBaseException {

		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		TGOCPRestResponse<String, ErrorResponse> response = null;
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String, ErrorResponse>();
		String invitationId = onboardingService.createOrUpdateInvitation(invitationEntity, userInfo, workflowId);
		if (null != file && invitationEntity.getInvitationId() == null) {
			onboardingService.saveInvitationImage(invitationId, file);
		}
		Long invitatioId = Long.parseLong(invitationId);
		if (null != file && invitationEntity.getInvitationId() != null) {

			onboardingService.updateInvitationImage(invitatioId, file);
		}
		if (null == file && invitationEntity.getImageContent() != null
				&& invitationEntity.getImageContent().length == 0) {
			onboardingService.deleteInvitationImage(invitatioId);
		}

		responseDetails.setResponseEntity(invitationId);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, invitationEntity.getInvitationName(),
				invitationEntity.getCreatorCompanyName(), invitationEntity);

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMAPNY-INVITE-C", actionSummary = "Invite Trading Partner", actionDetail = "New Trading Partner Invited with Invitation Code - {0}", hasContent = true)
	@PostMapping(value = "/invite", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<String, ErrorResponse> invite(@RequestBody OnboardingInvitationWrapper invitationWrapper,
			HttpServletRequest httpServletRequest) throws TGOCPBaseException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		TGOCPRestResponse<String, ErrorResponse> response = null;
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String, ErrorResponse>();
		String inviatationCode = onboardingService.invite(invitationWrapper, userInfo);

		responseDetails.setResponseEntity(inviatationCode);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, inviatationCode, invitationWrapper);

		return response;
	}

	@GetMapping(value = "/previewUtil")
	public Map<String, String> getRegistrationUrl(HttpServletRequest httpServletRequest) throws TGOCPBaseException {
		Map<String, String> result = new HashMap<>();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		BusinessUnitDetailsType businessType = businessUntilRestClient
				.getBusinessUnitDetailsType(userInfo.getCompanyId());
		result.put("companyName", businessType.getCompanyName());
		result.put("registrationUrl", appProperties.getRegistrationAppURL());
		return result;
	}

	@GetMapping(value = "/details/{invitationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<InvitationNeoRest, ErrorResponse> getInvitation(
			@PathVariable("invitationId") Long invitationId)
			throws CMApplicationException, TGOCPRestException, IOException {
		TGOCPRestResponse<InvitationNeoRest, ErrorResponse> response = null;
		TGOCPRestResponseDetails<InvitationNeoRest, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<InvitationNeoRest, ErrorResponse>();
		InvitationNeoRest invitations = null;
		invitations = onboardingService.getInvitationData(invitationId);
		responseDetails.setResponseEntity(invitations);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@GetMapping(value = "/{requestId}/data")
	public TGOCPRestResponse<OnboardingWorkflowRequestData, ErrorResponse> getRequesterTaskdata(
			@PathVariable("requestId") Long requestId) throws TGOCPBaseException {

		TGOCPRestResponse<OnboardingWorkflowRequestData, ErrorResponse> response = null;
		TGOCPRestResponseDetails<OnboardingWorkflowRequestData, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<OnboardingWorkflowRequestData, ErrorResponse>();
		OnboardingWorkflowRequestData requesterData = null;
		requesterData = onboardingService.getRequesterTaskdata(requestId);
		responseDetails.setResponseEntity(requesterData);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails);

		return response;
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMAPNY-INVITATIONS-S", actionSummary = "delete invitation", actionDetail = "Deleted invitaion having invitationcode= {0}")
	@PostMapping(value = "/delete")
	public TGOCPRestResponse<String, ErrorResponse> deleteInvitation(@RequestBody String[] invCodes)
			throws TGOCPRestException {

		TGOCPRestResponse<String, ErrorResponse> response = null;
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String, ErrorResponse>();

		onboardingService.deleteSentInvitation(invCodes[0]);

		responseDetails.setResponseEntity("deleted");
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, invCodes[0]);

		return response;
	}

	@PostMapping(value = "/reInvite")
	public void reInviteTP(@RequestBody List<ReinviteEntity> reInvitationEntities)
			throws TGOCPRestException, IOException {
		onboardingService.resendInvitation(reInvitationEntities);
	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMAPNY-INVITATIONS-S", actionSummary = "reInvite invitation", actionDetail = "Deleted invitaion having invitationcode - {0} and ReInvited Trading Partner with Invitation Code - {1}")
	@PostMapping(value = "/reInviteWithNewInvCode")
	public TGOCPRestResponse<String, ErrorResponse> reInviteTPWithNewinvCode(
			@RequestBody ReinviteEntity reInvitationEntities, HttpServletRequest httpServletRequest)
			throws TGOCPBaseException {
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		TGOCPRestResponse<String, ErrorResponse> response = null;
		TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<String, ErrorResponse>();
		String newInvitationCode = onboardingService.resendInvitationWithNewinvCode(reInvitationEntities, userInfo);

		responseDetails.setResponseEntity(newInvitationCode);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);

		response = new TGOCPRestResponse<>(responseDetails, reInvitationEntities.getInvitationCode(),
				newInvitationCode);

		return response;
	}

}
