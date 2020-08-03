package com.ot.cm.rest.controller.onboarding;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.TaskBasedRegistrationService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.controller.BaseRestController;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.onboarding.entity.RegistrationTaskOutputWrapper;
import com.ot.cm.rest.response.onboarding.entity.RegistrationWorkflow;
import com.ot.cm.util.Pair;
import com.ot.session.constants.TGOCPSessionHelperConstants;
import com.ot.session.entity.TGOCPSMUserVO;
import com.ot.session.entity.TGOCPSessionVO;

@RestController
@RequestMapping(value = "/onboarding/company")
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class TaskBasedRegistrationController extends BaseRestController {

	@Autowired
	TaskBasedRegistrationService taskBasedRegistrationService;

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-COMPANY-ONBOARDING-TB", actionSummary = "Onboarding Trading Partner", actionDetail = "Onboarding workflow {0} initiated for trading partner {1} with workflow ID: {2}", hasContent = true)
	public TGOCPRestResponse<SuccessResponse, ErrorResponse> doRegister(
			@RequestBody RegistrationTaskOutputWrapper registrationTaskWrapper, HttpServletRequest httpServletRequest)
			throws JsonParseException, JsonMappingException, IOException, SAXException, ParserConfigurationException,
			TGOCPRestException, JAXBException {
		TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
		TGOCPRestResponse<SuccessResponse, ErrorResponse> response = null;
		TGOCPSessionVO tgocpSessionVO = (TGOCPSessionVO) httpServletRequest.getSession()
				.getAttribute(TGOCPSessionHelperConstants.TGO_SESSION_KEY);
		TGOCPSMUserVO tgocpSMUserVO = tgocpSessionVO.getIMUserSession();
		Pair<SuccessResponse, RegistrationWorkflow> result = taskBasedRegistrationService
				.submitWorkflow(registrationTaskWrapper, tgocpSMUserVO);
		responseDetails.setResponseEntity(result.getFirst());
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		RegistrationWorkflow worflow = result.getSecond();
		String workflowDisplayName = registrationTaskWrapper.contextData != null
				? (String) registrationTaskWrapper.contextData.get("workflowDisplayName") : "unknown";
		String tpCompanyName = worflow.getProcessingContext().getTpIdentifier() != null
				? worflow.getProcessingContext().getTpIdentifier().getCompanyName() : "unknown";
		response = new TGOCPRestResponse<>(responseDetails, workflowDisplayName, tpCompanyName,
				result.getFirst().getTargetResourceRefId(), worflow.getProvisioningRequestData());

		return response;
	}
}
