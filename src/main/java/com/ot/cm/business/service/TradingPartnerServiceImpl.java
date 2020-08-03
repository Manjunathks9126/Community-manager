package com.ot.cm.business.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.CommunityType;
import com.ot.audit.vo.AuditInfo;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.cm.rest.client.TradingPartnerRestClient;
import com.ot.cm.tgms.entity.InitiatorIdentifier;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.util.ObjectMapper;
import com.ot.session.entity.TGOCPSMUserVO;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

@Service
public class TradingPartnerServiceImpl implements TradingPartnerService {

	@Autowired
	TradingPartnerRestClient tradingPartnerRestClient;

	@Autowired
	private AuditInfo auditInfo;

	@Autowired
	LocalizationMessages localizationMessages;

	@Override
	public SuccessResponse createTP(Workflow workflowEntity, TGOCPSMUserVO tgocpsmUserVO)
			throws CMApplicationException, TGOCPRestException, JAXBException {
		SuccessResponse resp = null;
		String tpID = null;
		boolean saveExtrernalReference = true;
		resp = saveTP(workflowEntity, tgocpsmUserVO.getParentID(), saveExtrernalReference);
		tpID = resp.getTargetResourceRefId();
		workflowEntity.getProcessingContext().getTpIdentifier().setBuId(tpID);
		return initiateCPSregistrations(workflowEntity, tgocpsmUserVO, tpID);

	}

	@Override
	public SuccessResponse saveTP(Workflow workflowEntity, String hubId, boolean saveExtrernalReference)
			throws CMApplicationException, TGOCPRestException {
		Map cmap = getCommunityDetais(hubId);
		String tpID = null;
		SuccessResponse resp = null;
		// try {
		resp = tradingPartnerRestClient
				.createTP(ObjectMapper.convertKeybankTypeVO(workflowEntity, cmap, saveExtrernalReference), false);
		tpID = resp.getTargetResourceRefId();
		if (null != tpID && !"".equals(tpID)) {
			auditInfo.setCustBuId(tpID);
			String bprId = tradingPartnerRestClient.createBPR(ObjectMapper.convertBPRTypeVO(tpID), hubId);
		}
		/*
		 * } catch (TGOCPRestException e) { if (e.getHttpStatus().equals("409")
		 * || e.getRestErrorCode().equals("BSAPI-3004")) { throw new
		 * CMApplicationException(ErrorCodes.ENTITY_ALREADY_EXIST_IN_IM,
		 * "Exception while save TP   ", e); } else { throw new
		 * CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
		 * "Exception while save TP   ", e); } }
		 */
		return resp;
	}

	@Override
	public SuccessResponse editTP(Workflow companyDetailsEntity, String companyId) throws CMApplicationException {
		SuccessResponse resp = null;
		try {
			auditInfo.setCustBuId(companyId);
			resp = tradingPartnerRestClient.editTP(ObjectMapper.convertKeybankTypeVO(companyDetailsEntity, null, false),
					companyId);

		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "EXCEPTION_SAVING_TP", e);
		}
		return resp;
	}

	@Override
	public SuccessResponse initiateWorkFlow(Workflow workflowEntity, TGOCPSMUserVO tgocpsmUserVO, String companyId)
			throws TGOCPRestException, JAXBException {
		SuccessResponse resp = null;
		String tpID = null;

		resp = tradingPartnerRestClient.editTP(ObjectMapper.convertKeybankTypeVO(workflowEntity, null, true),
				companyId);
		tpID = resp.getTargetResourceRefId();
		if (null != tpID)
			auditInfo.setCustBuId(tpID);
		workflowEntity.getProcessingContext().getTpIdentifier().setBuId(tpID);
		return initiateCPSregistrations(workflowEntity, tgocpsmUserVO, tpID);

	}

	@Override
	public SuccessResponse verifyTP(Workflow workflowEntity, TGOCPSMUserVO tgocpsmUserVO) throws TGOCPRestException {
		SuccessResponse resp = null;
		try {
			Map cmap = getCommunityDetais(tgocpsmUserVO.getParentID());
			resp = tradingPartnerRestClient.createTP(ObjectMapper.convertBUTypeVO(workflowEntity, cmap), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public List getKeyBankMaps() throws CMApplicationException {
		return tradingPartnerRestClient.getKeyBankMaps();
	}

	@Override
	public SuccessResponse createTPForCore(Workflow workflow, TGOCPSMUserVO tgocpsmUserVO)
			throws CMApplicationException, TGOCPRestException, JAXBException {
		return initiateCPSregistrations(workflow, tgocpsmUserVO, null);
	}

	private SuccessResponse initiateCPSregistrations(Workflow entity, TGOCPSMUserVO tgocpsmUserVO, String tpId)
			throws TGOCPRestException, JAXBException {

		InitiatorIdentifier initiatorIdentifier = new InitiatorIdentifier();
		initiatorIdentifier.setBuId(tgocpsmUserVO.getParentID());
		initiatorIdentifier.setUserId(tgocpsmUserVO.getPrincipalID());

		entity.getProcessingContext().setInitiatorIdentifier(initiatorIdentifier);

		String payload = CommonUtil.jaxbObjectToXML(entity);
		SuccessResponse resp = new SuccessResponse();

		String workflowId = tradingPartnerRestClient.initiateCPSworkFlow(payload, tgocpsmUserVO);
		resp.setTargetResourceRefId(workflowId);
		resp.setStatusMessage(
				localizationMessages.getMessage("CPS_WORKFLOW", HttpStatus.OK.value(), tgocpsmUserVO.getLocale()));

		return resp;
	}

	public Map<String, String> getCommunityDetais(String initiatorId) throws CMApplicationException {
		try {
			Map<String, String> cmap = new HashMap<>();
			BusinessUnitDetailsType res = tradingPartnerRestClient.getCommunityDetails(initiatorId);
			List<CommunityType> communitydetails = res.getCommunity();
			String cname = "";
			String participation = "";
			if (communitydetails.size() > 1) {
				for (CommunityType ctyp : communitydetails) {
					if (!ctyp.getCommunityName().equals(ApplicationConstants.TRADINGGRID_COMMUNITY)) {
						cname = ctyp.getCommunityName();
						cmap.put("COMMUNITY_NAME", cname);
					}
					if (ctyp.getCommunityName().equals(ApplicationConstants.TRADINGGRID_COMMUNITY)) {
						participation = ctyp.getParticipationType();
						// expectation is TP is getting created
						if (participation.equals("PRIVATE_COMMUNITY_OWNER"))
							participation = "PRIVATE_COMMUNITY_PARTNER";
						cmap.put("COMMUNITY_PARTICIPATION", participation);
					}

				}
			} else {
				cname = ApplicationConstants.TRADINGGRID_COMMUNITY;
				String participationType = ApplicationConstants.CUSTOMER;
				cmap.put("COMMUNITY_NAME", cname);
				cmap.put("COMMUNITY_PARTICIPATION", participationType);
				// cmap.put(cname,participationType);
			}

			return cmap;
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
					"CMS REST Client : Error while fetching Community Details   ", e);
		}

	}

	public String jaxbObjectToXML(Object classToBeBound) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(classToBeBound.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To
																			// format
																			// XML
			m.setProperty(CharacterEscapeHandler.class.getName(), new XmlCharacterHandler());
			StringWriter sw = new StringWriter();
			m.marshal(classToBeBound, sw);
			xmlString = sw.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlString;
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

}
