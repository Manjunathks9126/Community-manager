package com.ot.cm.business.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.ot.audit.vo.AuditInfo;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.TradingPartnerRestClient;
import com.ot.cm.rest.response.onboarding.entity.RegistartionProcessingContext;
import com.ot.cm.rest.response.onboarding.entity.RegistrationProvisioningData;
import com.ot.cm.rest.response.onboarding.entity.RegistrationTaskOutputWrapper;
import com.ot.cm.rest.response.onboarding.entity.RegistrationWorkflow;
import com.ot.cm.tgms.entity.InitiatorIdentifier;
import com.ot.cm.tgms.entity.TpIdentifier;
import com.ot.cm.util.Pair;
import com.ot.session.entity.TGOCPSMUserVO;

/**
 * 
 * @author ddharmav
 *
 */
@Component
public class TaskBasedRegistrationServiceImpl implements TaskBasedRegistrationService {

	private static final Logger logger = LoggerFactory.getLogger(TaskBasedRegistrationServiceImpl.class);

	@Autowired
	TradingPartnerRestClient tradingPartnerRestClient;

	@Autowired
	OnboardingService onboardingService;

	@Autowired
	LocalizationMessages localizationMessages;

	@Autowired
	private AuditInfo auditInfo;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Pair<SuccessResponse, RegistrationWorkflow> submitWorkflow(RegistrationTaskOutputWrapper taskWrapper,
			TGOCPSMUserVO tgocpSMUserVO) throws JsonParseException, JsonMappingException, IOException, SAXException,
			ParserConfigurationException, TGOCPRestException, JAXBException {

		RegistrationWorkflow workflow = new RegistrationWorkflow();
		RegistrationProvisioningData provisioningData = new RegistrationProvisioningData();

		Map<String, Object> mergedOutput = new HashMap<>();
		taskWrapper.getTasks().stream().forEach(task -> {
			if (StringUtils.isEmpty(task.getCategoryName())) {
				mergedOutput.putAll(task.getOutput());
			}
		});

		RegistartionProcessingContext context = setProcessingContext(mergedOutput, tgocpSMUserVO);
		Object requesterData = taskWrapper.getContextData().get("requesterData");

		context.setInvitationCode(getInvitationCode(taskWrapper, requesterData, tgocpSMUserVO));
		context.setInvitationSource(ApplicationConstants.CPS_INVITATION_SOURCE);
		context.setAction(ApplicationConstants.CPS_ACTION);
		provisioningData.setRequesterData(requesterData);
		provisioningData.setRegistrationData(mergedOutput);
		workflow.setProvisioningRequestData(provisioningData);
		workflow.setProcessingContext(context);
		String tpBUID = context.getTpIdentifier() != null ? context.getTpIdentifier().getBuId() : "unknown";
		auditInfo.setCustBuId(tpBUID);
		return Pair.of(initiateCPSregistrations(workflow, tgocpSMUserVO), workflow);
	}

	private String getInvitationCode(RegistrationTaskOutputWrapper taskWrapper, Object requesterData,
			TGOCPSMUserVO tgocpSMUserVO) throws TGOCPRestException {
		String invitationCode;
		if (ObjectUtils.isEmpty(requesterData)) {
			invitationCode = onboardingService.getInvitationCode(
					taskWrapper.getContextData().get("invitationId").toString(), tgocpSMUserVO.getLogin());
		} else {
			Map<String, Map<String, String>> requesterDataMap = objectMapper.convertValue(requesterData, Map.class);
			invitationCode = onboardingService.getInvitationCode(
					taskWrapper.getContextData().get("invitationId").toString(),
					requesterDataMap.get("companyInfo").get("companyId"),
					requesterDataMap.get("companyInfo").get("companyName"), tgocpSMUserVO.getLogin());
		}
		return invitationCode;
	}

	private RegistartionProcessingContext setProcessingContext(Map<String, Object> outputMap,
			TGOCPSMUserVO tgocpsmUserVO) {
		RegistartionProcessingContext context = new RegistartionProcessingContext();
		Map<String, String> map = objectMapper.convertValue(outputMap.get("companyInfo"), Map.class);
		TpIdentifier tpIdentifier = new TpIdentifier();
		if (null != map) {
			tpIdentifier.setCompanyName(map.get("companyName"));
			tpIdentifier.setCountryCode(map.get("countryCode"));
			tpIdentifier.setCity(map.get("city"));
			tpIdentifier.setBuId(map.get("tpCompanyId"));
		}

		context.setTpIdentifier(tpIdentifier);

		InitiatorIdentifier initiatorIdentifier = new InitiatorIdentifier();
		initiatorIdentifier.setBuId(tgocpsmUserVO.getParentID());
		initiatorIdentifier.setUserId(tgocpsmUserVO.getPrincipalID());

		context.setInitiatorIdentifier(initiatorIdentifier);

		return context;
	}

	private SuccessResponse initiateCPSregistrations(RegistrationWorkflow workflow, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException, JAXBException, JsonProcessingException {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload = xmlMapper.writeValueAsString(workflow);
		if (logger.isDebugEnabled()) {
			logger.info(payload);
		}
		SuccessResponse resp = new SuccessResponse();
		String workflowId = tradingPartnerRestClient.initiateCPSworkFlow(payload, tgocpsmUserVO);
		resp.setTargetResourceRefId(workflowId);
		resp.setStatusMessage(
				localizationMessages.getMessage("CPS_WORKFLOW", HttpStatus.OK.value(), tgocpsmUserVO.getLocale()));

		return resp;
	}

}
