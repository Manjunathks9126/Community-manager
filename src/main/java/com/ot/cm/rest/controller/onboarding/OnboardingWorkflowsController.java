package com.ot.cm.rest.controller.onboarding;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.OnboardingWorkflow;
import com.ot.cm.rest.response.entity.OnboardingWorkflowReport;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping("onboarding/workflows")
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class OnboardingWorkflowsController {

	@Autowired
	OnboardingService onboardingService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<OnboardingWorkflow>, ErrorResponse> listWorkflows(
			@RequestParam(value = "bulkOnly", required = false) boolean bulkOnly, HttpServletRequest httpServletRequest)
			throws TGOCPRestException {
		TGOCPRestResponseDetails<List<OnboardingWorkflow>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<OnboardingWorkflow>, ErrorResponse>();
		TGOCPRestResponse<List<OnboardingWorkflow>, ErrorResponse> response = null;
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(onboardingService.listWorkflows(userInfo.getCompanyId(), bulkOnly));

		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;

	}

	@GetMapping(value = "/{wfId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<OnboardingWorkflow, ErrorResponse> getWorkflow(HttpServletRequest httpServletRequest,
			@PathVariable String wfId) throws TGOCPRestException {
		TGOCPRestResponseDetails<OnboardingWorkflow, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<OnboardingWorkflow, ErrorResponse>();
		TGOCPRestResponse<OnboardingWorkflow, ErrorResponse> response = null;
		responseDetails.setResponseEntity(onboardingService.getWorkflow(wfId));

		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;

	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-ONBOARD-WRKFLW-U", actionSummary = "Onboarding workflow updated", actionDetail = "Updated worfklow {0} for Company {1}", hasContent = true)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<OnboardingWorkflow, ErrorResponse> updateOnbaordingWorkflow(
			HttpServletRequest httpServletRequest, @RequestBody OnboardingWorkflow workflow) throws TGOCPRestException {
		TGOCPRestResponseDetails<OnboardingWorkflow, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<OnboardingWorkflow, ErrorResponse>();
		TGOCPRestResponse<OnboardingWorkflow, ErrorResponse> response = null;
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		responseDetails.setResponseEntity(onboardingService.updateWorkflow(workflow, userInfo));
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails, workflow.getDisplayName(), workflow.getOwnerBuName(),
				workflow);

		return response;
	}

	@GetMapping(value = "/report/{workflowId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<ListingQueryResponse<OnboardingWorkflowReport>, ErrorResponse> getOnboardingWorkflowReport(
			HttpServletRequest httpServletRequest, @PathVariable("workflowId") String workflowId)
			throws TGOCPRestException, CMApplicationException {
		TGOCPRestResponseDetails<ListingQueryResponse<OnboardingWorkflowReport>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<ListingQueryResponse<OnboardingWorkflowReport>, ErrorResponse>();
		TGOCPRestResponse<ListingQueryResponse<OnboardingWorkflowReport>, ErrorResponse> response = null;

		OnboardingWorkflowListingFilterQuery request = new OnboardingWorkflowListingFilterQuery();
		UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
		request.setCompanyId(userInfo.getCompanyId());
		request.setUserId(userInfo.getUserId());
		request.setUniqueId(workflowId);
		responseDetails.setResponseEntity(onboardingService.getOnboardingWorkflowReport(request));

		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		response = new TGOCPRestResponse<>(responseDetails);

		return response;

	}

}
