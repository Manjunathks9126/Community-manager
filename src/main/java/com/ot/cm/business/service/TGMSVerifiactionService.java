package com.ot.cm.business.service;

import javax.xml.bind.JAXBException;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.imapi.client.ImRestException;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.vo.UserInfo;
import com.ot.session.entity.TGOCPSMUserVO;

public interface TGMSVerifiactionService {

	public SuccessResponse createTP(Workflow companyDetailsEntity, TGOCPSMUserVO tgocpsmUserVO, UserInfo userInfo)
			throws CMApplicationException, JAXBException, TGOCPRestException, ImRestException;

	public Workflow getWorkflowEntity(String tpBuid, String hubBuid)
			throws CMApplicationException, TGOCPRestException, JAXBException;

	public SuccessResponse approveTp(Workflow companyDetailsEntity, TGOCPSMUserVO tgocpsmUserVO, String bprId)
			throws CMApplicationException, JAXBException, TGOCPRestException, ImRestException;

	public TGOCPRestResponseDetails<SuccessResponse, ErrorResponse> validateBPRExist(String tpId, String tgocpsmUserVO)
			throws TGOCPRestException;

	TGOCPRestResponseDetails<BusinessUnitDetailsType[], ErrorResponse> validateTPExist(String name, String city,
			String country) throws TGOCPRestException;

	/**
	 * @param tpRequestType
	 * @return boolean
	 */
	public boolean existingCompanyRequest(String tpRequestType);

	TradingPartnerRelationshipDetailsEntity[] getTradingpartnerDetails(String ownerBuId, String tradingAddress)
			throws TGOCPRestException;
}
