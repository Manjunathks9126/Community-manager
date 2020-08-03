package com.ot.cm.business.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.imapi.client.ImRestException;
import com.ot.audit.vo.AuditInfo;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.TradingPartnerRestClient;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.tgms.entity.BusinessUnit;
import com.ot.cm.tgms.entity.InitiatorIdentifier;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.util.ObjectMapper;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;
import com.ot.session.entity.TGOCPSMUserVO;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

@Service
public class TGMSVerifiactionServiceImpl implements TGMSVerifiactionService {
	@Autowired
	TradingPartnerRestClient tpRestClient;

	@Autowired
	private AuditInfo auditInfo;

	private static final String[] tpExistisRequestTypes = { "EXISTING_IC_TP", "EXISTING_TP", "EXISTING_SP_TP",
			"NEW_EDI_EXISTING_IC_TP", "EXIST_IC_EDI_EXIST_IC_TP", "EXIST_SP_EDI_EXIST_SP_TP" };

	@Override
	@Loggable
	public SuccessResponse createTP(Workflow companyDetailsEntity, TGOCPSMUserVO tgocpsmUserVO, UserInfo userInfo)
			throws CMApplicationException, JAXBException, TGOCPRestException, ImRestException {

		try {
			Map<String, String> cmap = new HashMap<>(4);
			String tpRequestType = companyDetailsEntity.getProvisioningRequestData().getTpRequestType();
			if ("EXISTING_SP_TP".equalsIgnoreCase(tpRequestType) || "EXISTING_SP_EDI".equalsIgnoreCase(tpRequestType))
				cmap.put("COMMUNITY_PARTICIPATION", ApplicationConstants.SERVICEPROVIDERTP);
			else
				cmap.put("COMMUNITY_PARTICIPATION", ApplicationConstants.INTERCONNECT);

			cmap.put("COMMUNITY_NAME", ApplicationConstants.TRADINGGRID_COMMUNITY);

			String setup = companyDetailsEntity.getProvisioningRequestData().getSetup();
			// To Do--Have to change the status to request instead of approved
			String status = ApplicationConstants.BPR_STATUS_REQUESTED;
			if (setup.equals("TESTING"))
				status = ApplicationConstants.BPR_STATUS_TESTING;

			if (!existingCompanyRequest(tpRequestType)) {
				// TP Company Creation and BPR
				String tpID = tpRestClient.createTP(ObjectMapper.convertBUTypeVO(companyDetailsEntity, cmap), false)
						.getTargetResourceRefId();
				if (null != tpID && !"".equals(tpID)) {
					companyDetailsEntity.getProcessingContext().getTpIdentifier().setBuId(tpID);

					auditInfo.setCustBuId(tpID);
					createBprwithCMD(tpID, status, tgocpsmUserVO);
					// createBpr(tpID,
					// tgocpsmUserVO.getParentID());//"GC16689358OH");//tgocpsmUserVO.getParentID());
				}
			} else {
				String buId = companyDetailsEntity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
						.getBuId();
				auditInfo.setCustBuId(buId);
				companyDetailsEntity.getProcessingContext().getTpIdentifier().setBuId(buId);
				boolean bprExists = false;
				if (Arrays.asList(tpExistisRequestTypes).stream().anyMatch(tpRequestType::equalsIgnoreCase)) {
					try {
						// changed for TgmsUseCase
						// validating BPR exist for bidirection(hub as initator
						// && partner as Initator)
						bprExists = validateBPRExistForBidirectional(buId, userInfo);
					} catch (TGOCPRestException e) {
						if (e.getHttpStatus().value() == 409)
							bprExists = true;
						else
							throw e;
					}
				}
				if (!bprExists) {
					createBprwithCMD(buId, status, tgocpsmUserVO);
				}
				// Only BPR
				/*
				 * resp = new SuccessResponse();
				 * resp.setStatusMessage("BPR created successfully.");
				 * resp.setTargetResourceRefId(createBpr(companyDetailsEntity.
				 * getProvisioningRequestData()
				 * .getRegistrationData().getBusinessUnit().getBuId(),
				 * tgocpsmUserVO.getCompanyId()));
				 */
			}

			BusinessUnit budetails = companyDetailsEntity.getProvisioningRequestData().getRegistrationData()
					.getBusinessUnit();
			companyDetailsEntity.getProcessingContext().getTpIdentifier()
					.setCity(budetails.getCompanyAddress().getCity());
			companyDetailsEntity.getProcessingContext().getTpIdentifier().setCompanyName(budetails.getCompanyName());
			companyDetailsEntity.getProcessingContext().getTpIdentifier()
					.setCountryCode(budetails.getCompanyAddress().getCountryCode());

			InitiatorIdentifier initiatorIdentifier = new InitiatorIdentifier();
			initiatorIdentifier.setBuId(tgocpsmUserVO.getParentID());
			initiatorIdentifier.setUserId(tgocpsmUserVO.getPrincipalID());
			// This is required only for TGMS use case
			budetails.setInvitingCommunityId(tgocpsmUserVO.getCommunityID());
			companyDetailsEntity.getProcessingContext().setInitiatorIdentifier(initiatorIdentifier);

		} catch (TGOCPRestException e) {
			if (e.getHttpStatus().value() == 409 || e.getRestErrorCode().equals("BSAPI-3004")) {
				throw new CMApplicationException(ErrorCodes.ENTITY_ALREADY_EXIST_IN_IM, "ENTITY_ALREADY_EXIST_IN_IM",
						e);
			} else {
				throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ENTITY_ALREADY_EXIST_IN_IM", e);
			}
		}

		return initiateCPSregistrations(companyDetailsEntity, tgocpsmUserVO);
	}

	/**
	 * Returns if company creation should be processed depending on request type
	 * 
	 * @param reqType
	 * @return boolean
	 */
	@Override
	public boolean existingCompanyRequest(String reqType) {
		for (String type : tpExistisRequestTypes) {
			if (type.equalsIgnoreCase(reqType))
				return true;
		}
		return false;
	}

	@Override
	public Workflow getWorkflowEntity(String tpBuid, String hubBuid)
			throws CMApplicationException, TGOCPRestException, JAXBException {

		com.ot.cm.rest.request.entity.RegistrationRequestType response = tpRestClient
				.getCPSRegistrationDataFromRA(tpBuid, hubBuid);
		String content = response.getRequest().getContent();
		JAXBContext jc = JAXBContext.newInstance(Workflow.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StreamSource streamSource = new StreamSource(new StringReader(content));
		JAXBElement<Workflow> je = unmarshaller.unmarshal(streamSource, Workflow.class);
		Workflow workflowentity = je.getValue();
		workflowentity.getProvisioningRequestData().setTpRequestType(response.getRequest().getStatus());
		return workflowentity;
	}

	@Override
	public SuccessResponse approveTp(Workflow companyDetailsEntity, TGOCPSMUserVO tgocpsmUserVO, String bprId)
			throws CMApplicationException, JAXBException, TGOCPRestException, ImRestException {
		SuccessResponse[] response = tpRestClient.createTPR(
				ObjectMapper.convertTPRTypeVO(companyDetailsEntity, tgocpsmUserVO.getParentID()),
				tgocpsmUserVO.getParentID());
		boolean tprcreated = false;
		for (SuccessResponse resp : response) {
			if (resp.getStatusCode().equals(HttpStatus.CREATED.value())) {
				tprcreated = true;
			} else {
				tprcreated = false;
			}
		}
		SuccessResponse result = new SuccessResponse();
		if (tprcreated) {
			tpRestClient.updateBPRwitCMD(bprId, companyDetailsEntity.getProcessingContext().getTpIdentifier().getBuId(),
					ApplicationConstants.BPR_STATUS_APPROVED, tgocpsmUserVO);
			result = tpRestClient.editTP(ObjectMapper.convertBUStatusVO(),
					companyDetailsEntity.getProcessingContext().getTpIdentifier().getBuId());
		}
		auditInfo.setCustBuId(companyDetailsEntity.getProcessingContext().getTpIdentifier().getBuId());
		return result;
	}

	private SuccessResponse initiateCPSregistrations(Workflow entity, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException, JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Workflow.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		// marshaller.setProperty(CharacterEscapeHandler.class.getName(),new
		// XmlCharacterHandler());
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter writer = new StringWriter();
		marshaller.marshal(entity, writer);
		String payload = writer.toString();
		String workflowid = tpRestClient.initiateCPSworkFlow(payload, tgocpsmUserVO);
		SuccessResponse resp = new SuccessResponse();
		resp.setStatusMessage("Workflow initiated successfully.");
		resp.setTargetResourceRefId(workflowid);
		return resp;
	}

	private String createBprwithCMD(String tpId, String status, TGOCPSMUserVO tgocpsmUserVO)
			throws TGOCPRestException, ImRestException {
		return tpRestClient.createBPRwitCMD(tpId, status, tgocpsmUserVO);
	}

	@Override
	public TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> validateBPRExist(String tpId, String companyId)
			throws TGOCPRestException {
		return tpRestClient.validateBPRExist(ObjectMapper.convertBPRTypeVO(tpId), companyId);
	}

	private boolean validateBPRExistForBidirectional(String tpId, UserInfo userinfo) throws TGOCPRestException {

		return tpRestClient.bprCount(tpId, userinfo.getCompanyId(), userinfo).getCount() > 0
				|| tpRestClient.bprCount(userinfo.getCompanyId(), tpId, userinfo).getCount() > 0;
	}

	@Override
	public TGOCPRestResponseDetails<BusinessUnitDetailsType[], ErrorResponse> validateTPExist(String name, String city,
			String country) throws TGOCPRestException {

		return tpRestClient.validateTPExist(name, city, country);
	}

	public class XmlCharacterHandler implements CharacterEscapeHandler {
		@Override
		public void escape(char[] buf, int start, int len, boolean isAttValue, Writer out) throws IOException {
			StringWriter buffer = new StringWriter();

			for (int i = start; i < start + len; i++) {
				buffer.write(buf[i]);
			}

			String st = buffer.toString();

			out.write(st);
		}
	}

	@Override
	public TradingPartnerRelationshipDetailsEntity[] getTradingpartnerDetails(String ownerBuId, String tradingAddress)
			throws TGOCPRestException {
		TradingPartnerRelationshipDetailsEntity[] hubTprDetails;
		TradingPartnerRelationshipDetailsEntity[] tpTprDetails;
		hubTprDetails = tpRestClient.getHubTradingPartnerList(ownerBuId, tradingAddress);
		tpTprDetails = tpRestClient.getTpTradingPartnerList(ownerBuId, tradingAddress);
		if (hubTprDetails != null && tpTprDetails != null) {
			return Stream.of(hubTprDetails, tpTprDetails).flatMap(Stream::of)
					.toArray(TradingPartnerRelationshipDetailsEntity[]::new);
		} else if (hubTprDetails != null) {
			return hubTprDetails;
		} else {
			return tpTprDetails;
		}
	}
}
