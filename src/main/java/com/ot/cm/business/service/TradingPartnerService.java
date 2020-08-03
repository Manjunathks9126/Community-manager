package com.ot.cm.business.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.session.entity.TGOCPSMUserVO;

public interface TradingPartnerService {

	public SuccessResponse saveTP(Workflow workflowEntity, String hubId, boolean saveExtrernalReference)
			throws CMApplicationException, TGOCPRestException;

	public SuccessResponse editTP(Workflow workflowEntity, String hubId) throws CMApplicationException;

	public SuccessResponse initiateWorkFlow(Workflow workflowEntity, TGOCPSMUserVO tgocpsmUserVO, String companyId)
			throws CMApplicationException, TGOCPRestException, JAXBException;

	public SuccessResponse verifyTP(Workflow workflowEntity, TGOCPSMUserVO tgocpsmUserVO) throws TGOCPRestException;

	public List getKeyBankMaps() throws CMApplicationException;

	public SuccessResponse createTPForCore(Workflow workflow, TGOCPSMUserVO tgocpsmUserVO)
			throws CMApplicationException, TGOCPRestException, JAXBException;

	public SuccessResponse createTP(Workflow workflow, TGOCPSMUserVO tgocpsmUserVO)
			throws CMApplicationException, TGOCPRestException, JAXBException;
}
