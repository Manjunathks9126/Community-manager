/******************************************************************************
 All rights reserved. All information contained in this software is confidential
 and proprietary to opentext. No part of this software may be
 reproduced or transmitted in any form or any means, electronic, mechanical,
 photocopying, recording or otherwise stored in any retrieval system of any
 nature without the prior written permission of opentext.
 This material is a trade secret and its confidentiality is strictly maintained.
 Use of any copyright notice does not imply unrestricted public access to this
 material.

 (c) opentext

 *******************************************************************************
 Change Log:
 Date          Name                Defect#           Description
 -------------------------------------------------------------------------------
 08/03/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.gxs.services.bsapi.rs.v3.entity.ModifyStatus;
import com.gxs.services.bsapi.rs.v3.entity.TradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.TradingPartnerRelationshipDetailsType;
import com.ot.cm.business.dao.BulkDownloadDao;
import com.ot.cm.cms.response.entity.User;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.jpa.entity.BulkDownload;
import com.ot.cm.rest.client.TPRDataRestClient;
import com.ot.cm.rest.client.UsersRestClient;
import com.ot.cm.rest.request.entity.ListingFilterQuery;
import com.ot.cm.rest.request.entity.TradingPartnerModifyEntity;
import com.ot.cm.rest.request.entity.search.TPRFilterSearchQuery;
import com.ot.cm.rest.response.CMSSuccessResponse;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.TPDirectoryFilterOptionsEntity;
import com.ot.cm.rest.response.entity.TPRData;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.config.properties.GlobalProperties;

@Component
public class TradingPartnerDirectoryServiceImpl implements TradingPartnerDirectoryService {
	private static final Logger logger = LoggerFactory.getLogger(TradingPartnerDirectoryServiceImpl.class);

	private static final String TPR_SENDER_ROLE = "Sender";
	private static final String TPR_RECEIVER_ROLE = "Receiver";

	private static final String TPR_RECEIVE = "RECEIVE";
	private static final String TPR_SEND = "SEND";

	@Autowired
	private ApplicationProperties appProperties;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private BulkDownloadDao bulkDownloadDao;

	@Autowired
	private UsersRestClient userRestClient;

	@Autowired
	private TPRDataRestClient tprDataRestClient;

	@Autowired
	private GlobalProperties globalProperties;

	/**
	 * This service method returns TP Directory filters predefined options
	 */
	@Override
	public TPDirectoryFilterOptionsEntity getTPDirectoryFilterOptions() throws CMApplicationException {
		TPDirectoryFilterOptionsEntity filterOptions = new TPDirectoryFilterOptionsEntity();

		try {
			filterOptions.setRowsPerPage(appProperties.getTpDirectoryRowsPerPage());
		} catch (Exception ex) {
			throw new CMApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "TPDirectoryFilterOptions_500",
					ex.getMessage(), CommonUtil.getStackTrace(ex));
		}

		return filterOptions;
	}

	@Override
	public TradingPartnerRelationshipDetailsEntity[] getTradingpartnerDetails(String tprId) throws TGOCPRestException {
		TradingPartnerRelationshipDetailsEntity[] tprDetails;
		tprDetails = tprDataRestClient.ViewTradingPartnerDetails(tprId);
		return tprDetails;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void doBulkExport(ListingFilterQuery request) throws CMApplicationException, TGOCPRestException {
		String emailId = getUserEmail(request.getUserId());
		BulkDownload bulkDownload = new BulkDownload();
		bulkDownload.setCompanyId(request.getId());
		bulkDownload.setUserEmailId(emailId);
		bulkDownload.setDataExtractionUrl(globalProperties.getRest().getBulkExportURL());
		bulkDownload.setCallerApplication(APPLICATION_NAME);
		bulkDownload.setFileType(FileType.EXCEL.toString());
		bulkDownload.setExportContext(EXPORT_CONTEXT);
		bulkDownload.setTotalRecords(Integer.valueOf(request.getLimit()));
		bulkDownload.setStatus(STATUS_PENDING);
		bulkDownload.setCreatedBy(request.getUserId());
		bulkDownload.setCreatedTimeStamp(new Date());
		ObjectMapper mapper = new ObjectMapper();
		try {
			bulkDownload.setFilterString(mapper.writeValueAsString(request));
			bulkDownload = bulkDownloadDao.saveBulkRequest(bulkDownload);
			jmsTemplate.convertAndSend(EnvironmentConstants.AMQ_NAME, mapper.writeValueAsString(bulkDownload));

		} catch (IOException e) {
			throw new CMApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "TP_BULK_EXPORT_500", e.getMessage(),
					CommonUtil.getStackTrace(e));
		}

	}

	private String getUserEmail(String userId) throws TGOCPRestException {
		User userDetails = userRestClient.getUser(userId);
		return userDetails.getContactInformation().getEmail();
	}

	@Override
	public ListingQueryResponse<TPRData> getTPRData(TPRFilterSearchQuery filterObject) throws TGOCPRestException {
		ListingQueryResponse<TPRData> tprResponseEntity = new ListingQueryResponse<>();
		Map<String, String> queryParams = new HashMap<>();
		if (!CommonUtil.isEmpty(filterObject.getPartnerCompanyId())) {
			queryParams.put("partnerCompanyId", filterObject.getPartnerCompanyId());
		}
		if (!CommonUtil.isEmpty(filterObject.getCompanyName())) {
			queryParams.put("partnerCompanyName", addWildCards(filterObject.getCompanyName()));
		}
		if (!CommonUtil.isEmpty(filterObject.getDisplayName())) {
			queryParams.put("partnerCompanyDisplayName", filterObject.getDisplayName());
		}
		if (!CommonUtil.isEmpty(filterObject.getEdiAddress())) {
			queryParams.put("tradingAddress", filterObject.getEdiAddress());
			// queryParams.put("tradingAddress",
			// addWildCards(filterObject.getEdiAddress()));
		}

		if (!CommonUtil.isEmpty(filterObject.getDateFrom()) || !CommonUtil.isEmpty(filterObject.getDateTo())) {
			queryParams.put("creationDate", "(" + filterObject.getDateFrom() + "," + filterObject.getDateTo() + ")");
		}

		if (!ObjectUtils.isEmpty(filterObject.getStatus())) {
			queryParams.put("partnershipStatus", (String.join(",", filterObject.getStatus())));
		}

		/*
		 * if (CommonUtil.isEmpty(filterObject.getSortOrder())) {
		 * queryParams.put("sortBy", "desc_creationDate"); }
		 */
		if (filterObject.isCountOnly()) {
			queryParams.put("countOnly", filterObject.isCountOnly() + "");

			Integer count = tprDataRestClient.getTPRsCount(null, queryParams, filterObject.getCompanyId());
			tprResponseEntity.setItemCount(count);

		} else if (!CommonUtil.isEmpty(filterObject.getLimit())
				&& Integer.parseInt(filterObject.getLimit()) <= TP_QUERY_LIMIT) {
			queryParams.put("after", filterObject.getAfter());
			queryParams.put("limit", filterObject.getLimit());

			TradingPartnerRelationshipDetailsType[] tprDetails = null;
			try {
				tprDetails = tprDataRestClient.getTPRList(queryParams, filterObject.getCompanyId());
			} catch (TGOCPRestException e) {
				if (e.getErrorCode().equals("TGOCPREST-4000")) {
					throw new TGOCPRestException(HttpStatus.BAD_REQUEST, null, "TPR_LIST_400", e.getMessage(),
							CommonUtil.getStackTrace(e));
				} else {
					throw new TGOCPRestException(HttpStatus.CONFLICT, null, "TPR_LIST_500", e.getMessage(),
							CommonUtil.getStackTrace(e));
				}
			}

			List<TPRData> mappedTPRDataResponse = mapTPRDataResponse(tprDetails, filterObject.getCompanyId());

			if (!ObjectUtils.isEmpty(mappedTPRDataResponse)) {
				tprResponseEntity.setItemList(mappedTPRDataResponse);
			}
		} else if (!CommonUtil.isEmpty(filterObject.getLimit())
				&& Integer.parseInt(filterObject.getLimit()) > TP_QUERY_LIMIT) {
			// Bulk Export
		}

		return tprResponseEntity;
	}

	/**
	 * This method forms the TPR View response from CMS API response
	 * 
	 * @param tprDetails
	 * @param contextCompanyId
	 * @return
	 */
	private List<TPRData> mapTPRDataResponse(TradingPartnerRelationshipDetailsType[] tprDetails,
			String contextCompanyId) {
		List<TPRData> list = new ArrayList<>();

		for (TradingPartnerRelationshipDetailsType tpr : tprDetails) {
			TPRData tprData = new TPRData();

			tprData.setTprId(tpr.getTradingPartnerRelationshipId());

			tprData.setTprType(tpr.getTprType());
			tprData.setStatus(tpr.getStatus());

			if (contextCompanyId.equals(tpr.getOwnerTradingAddressCompanyId())) {
				tprData.setCompanyId(tpr.getPartnerTradingAddressCompanyId());
				tprData.setCompanyName(tpr.getPartnerTradingAddressCompanyName());
				tprData.setDisplayName(tpr.getPartnerTradingAddressCompanyDisplayName());
				tprData.setEdiAddress(formEDIAddress(tpr.getPartnerTradingAddress()));
				tprData.setVanProvider(tpr.getPartnerTradingAddressVanprovider());

				tprData.setOwnerTradingAddressCompanyId(tpr.getPartnerTradingAddressCompanyId());

				tprData.setYourAddress(formEDIAddress(tpr.getOwnerTradingAddress()));
				tprData.setYourRole(formTPRRole(tpr.getPartnershipDirection(), false));
				tprData.setYourVanProvider(tpr.getOwnerTradingAddressVanprovider());
				if ("PENDING".equals(tpr.getStatus())) {
					tprData.setStatus("REQUESTED");
				}

			} else {
				tprData.setCompanyId(tpr.getOwnerTradingAddressCompanyId());
				tprData.setCompanyName(tpr.getOwnerTradingAddressCompanyName());
				tprData.setDisplayName(tpr.getOwnerTradingAddressCompanyDisplayName());
				tprData.setEdiAddress(formEDIAddress(tpr.getOwnerTradingAddress()));
				tprData.setVanProvider(tpr.getOwnerTradingAddressVanprovider());

				tprData.setOwnerTradingAddressCompanyId(tpr.getOwnerTradingAddressCompanyId());

				tprData.setYourAddress(formEDIAddress(tpr.getPartnerTradingAddress()));
				tprData.setYourRole(formTPRRole(tpr.getPartnershipDirection(), true));
				tprData.setYourVanProvider(tpr.getPartnerTradingAddressVanprovider());

			}

			if (null == tprData.getDisplayName()) {
				tprData.setDisplayName(tprData.getCompanyName());
			}

			list.add(tprData);
		}

		return list;
	}

	/**
	 * This method forms the EDI Address Text ( Qualifier : Address )
	 * 
	 * @param tradingAddressType
	 * @return
	 */
	private String formEDIAddress(TradingAddressType tradingAddressType) {
		return (!CommonUtil.isEmpty(tradingAddressType.getQualifier()) ? tradingAddressType.getQualifier() : "") + ":"
				+ tradingAddressType.getAddress();
	}

	/**
	 * This method transforms TPR Direction from CMS response to UI field
	 * Sender/Receiver
	 * 
	 * @param tprDirection
	 * @param reverse
	 * @return
	 */
	private String formTPRRole(String tprDirection, boolean reverse) {
		if (TPR_SEND.equals(tprDirection)) {
			return reverse ? TPR_RECEIVER_ROLE : TPR_SENDER_ROLE;
		} else if (TPR_RECEIVE.equals(tprDirection)) {
			return reverse ? TPR_SENDER_ROLE : TPR_RECEIVER_ROLE;
		}

		return "";
	}

	private String addWildCards(String param) {
		StringBuilder str = new StringBuilder();

		return str.append("*").append(param).append("*").toString();
	}

	/**
	 * call to terminate trading partner relationship
	 * 
	 * @param tprIdd
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 */
	@Override
	public CMSSuccessResponse[] terminateTradingPartnerRelationship(TradingPartnerModifyEntity[] tprData)
			throws TGOCPRestException {
		CMSSuccessResponse[] response = new CMSSuccessResponse[tprData.length];
		int failureCount = 0;
		if (tprData.length <= 10) {
			for (int i = 0; i < tprData.length; i++) {
				response[i] = new CMSSuccessResponse();
				try {
					response[i] = tprDataRestClient.terminateTradingPartnerRelationship(tprData[i].getTprId());
				} catch (Exception e) {
					response[i].setStatusCode("545");
					failureCount++;
				}
			}
		}
		if (tprData.length == failureCount) {
			throw new TGOCPRestException(HttpStatus.EXPECTATION_FAILED, "TGCOP-REST4170", "TPR_TERMINATE_417",
					"Failed to process the request", null);
		}
		response[0].setTargetResourceRefId(Integer.toString(failureCount));
		return response;
	}

	/**
	 * call to approve/reject tpr
	 * 
	 * @param tprId
	 * @param updatedData
	 * @param userInfo
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 */
	@Override
	public CMSSuccessResponse[] approveRejectTradingPartnerRelationship(TradingPartnerModifyEntity[] tprData,
			UserInfo userInfo) throws TGOCPRestException {
		CMSSuccessResponse[] response = new CMSSuccessResponse[tprData.length];
		int failureCount = 0;
		for (int i = 0; i < tprData.length; i++) {
			response[i] = new CMSSuccessResponse();
			if (!CommonUtil.isEmpty(tprData[i].getTprId()) && tprData.length <= 10) {

				if (ModifyStatus.REJECT.equals(tprData[i].getModifiedData().getModifyStatus())
						&& CommonUtil.isEmpty(tprData[i].getModifiedData().getReasonForRejection())) {
					throw new TGOCPRestException(HttpStatus.EXPECTATION_FAILED, "TGCOP-REST4170", "TPR_REJECT_417",
							"Rejection reason mandetory to reject TPR", null);
				}
				try {
					response[i] = tprDataRestClient.approveRejectTradingPartnerRelationship(tprData[i].getTprId(),
							userInfo.getCompanyId(), tprData[i].getModifiedData());
				} catch (Exception e) {
					response[i].setStatusCode("545");
					failureCount++;

				}
			}

		}
		if (tprData.length == failureCount) {
			throw new TGOCPRestException(HttpStatus.EXPECTATION_FAILED, "TGCOP-REST4170", "TPR_TERMINATE_417",
					"Failed to process the request", null);
		}

		response[0].setTargetResourceRefId(Integer.toString(failureCount));
		return response;
	}
}
