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
 May 18, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.gxs.services.bsapi.rs.v3.entity.BaseTradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.PartnerTradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.TPDirectoryDetailsType;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkExportHelper;
import com.ot.cm.rest.client.BusinessPartnerRestClient;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.request.entity.BusinessPartnerFacades;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.TPDirectory;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.session.annotation.Loggable;

/**
 * @author ssen
 *
 */
@Service
public class BusinessPartnerServiceImpl implements BusinessPartnerService {
	private static final Logger logger = LoggerFactory.getLogger(BusinessPartnerServiceImpl.class);

	@Autowired
	private BusinessPartnerRestClient bpClient;

	@Autowired
	private ApplicationProperties appProperties;

	@Autowired
	private BulkExportHelper bulkExportHelper;
	
	@Override
	public CMDSuccessResponse addOrUpdateBpFacades(BusinessPartnerFacades fc, String ownerBuId, UserInfo userInfo)
			throws TGOCPRestException {

		fc.setOwnerBuId(ownerBuId);
		if (CommonUtil.isEmpty(fc.getUniqueId())) {
			// Sometimes the Facade entry gets created at a later point during
			// Onboarding and TP Overview page might not have the info by then.
			// - TGOCP-2505
			String facadeId = getIfFacadeExists(ownerBuId, fc.getTargetBuId(), userInfo);
			if (null != facadeId) {
				return bpClient.editFacades(fc, facadeId, userInfo);
			}
			return bpClient.createFacades(fc, userInfo);
		} else {
			String facadeId = fc.getUniqueId();
			// fc.setUniqueId(null);
			return bpClient.editFacades(fc, facadeId, userInfo);
		}
	}

	private String getIfFacadeExists(String ownerBuId, String targetBuId, UserInfo userInfo) throws TGOCPRestException {
		BusinessPartnerFacades[] facadeList = bpClient.getFacades(ownerBuId, targetBuId, userInfo);
		if (facadeList.length > 0) {
			return facadeList[0].getUniqueId();
		}

		return null;
	}

	private String addWildCards(String param) {
		StringBuilder str = new StringBuilder();

		return str.append("*").append(param).append("*").toString();
	}

	@Override
	public ListingQueryResponse<TPDirectoryDetailsType> getBusinessPartnerList(BPRFilterSearchQuery filterObject)
			throws ParseException, TGOCPBaseException {
		ListingQueryResponse<TPDirectoryDetailsType> listResponse = new ListingQueryResponse<>();
		Map<String, String> queryParams = new HashMap<>();

		if (!CommonUtil.isEmpty(filterObject.getPartnerCompanyId())) {
			queryParams.put("facadeCompanyId", filterObject.getPartnerCompanyId());
		}
		if (!CommonUtil.isEmpty(filterObject.getCompanyName())) {

			queryParams.put("partnerCompanyName", addWildCards(filterObject.getCompanyName()));
		}
		if (!CommonUtil.isEmpty(filterObject.getDisplayName())) {

			queryParams.put("partnerCompanyDisplayName", addWildCards(filterObject.getDisplayName()));
		}
		if (!CommonUtil.isEmpty(filterObject.getEdiAddress())) {
			queryParams.put("tradingAddress", filterObject.getEdiAddress());
			// commented as was not working. cms change required,
			// queryParams.put("tradingAddress",
			// addWildCards(filterObject.getEdiAddress()));
		}

		if (!CommonUtil.isEmpty(filterObject.getDateFrom()) || !CommonUtil.isEmpty(filterObject.getDateTo())) {
			queryParams.put("partnershipActivationDate",
					CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", filterObject.getDateFrom()) + ","
							+ CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", filterObject.getDateTo()));
		}
		if (!ObjectUtils.isEmpty(filterObject.getStatus())) {
			queryParams.put("bprStatus", String.join(",", filterObject.getStatus()));
		}
		if (CommonUtil.isEmpty(filterObject.getSortOrder())) {
			queryParams.put("sortBy", "desc_partnershipActivationDate");
		} else {
			if (filterObject.getSortOrder().equalsIgnoreCase("1")) {
				queryParams.put("sortBy", "desc_" + filterObject.getSortField());
			} else {
				queryParams.put("sortBy", filterObject.getSortField());
			}
		}
		if (filterObject.isCountOnly()) {
			queryParams.put("countOnly", filterObject.isCountOnly() + "");
			Integer count = bpClient.getBPRsCount(null, queryParams, filterObject.getCompanyId());
			listResponse.setItemCount(count);

		} else if (!CommonUtil.isEmpty(filterObject.getLimit()) && !filterObject.isExportAll()) {
			queryParams.put("after", filterObject.getAfter());
			queryParams.put("limit", filterObject.getLimit());
			List<TPDirectoryDetailsType> list = bpClient.getBPRList(queryParams, filterObject.getCompanyId());
			// modifying Bpr list for Status And DisplayName
			modifyList(list);
			if (!ObjectUtils.isEmpty(list)) {
				listResponse.setItemList(list);
			}
		} else if (!CommonUtil.isEmpty(filterObject.getLimit())) {
			filterObject.setExportAll(false); // Setting to false to make sure
												// bulk export is not call
												// recursively
			bulkExportHelper.initiateBPRBulkExport(filterObject);
		}
		return listResponse;
	}

	String[] BPR_STATUS = {"?bprStatus=APPROVED&countOnly=true",
							"?bprStatus=Testing&countOnly=true",
							"?bprStatus=REQUESTED&countOnly=true",
							"?bprStatus=REJECTED&countOnly=true",
							"?bprStatus=Error&countOnly=true",
							"?countOnly=true"};
	@Override
	public Map<String, Integer> statsByBPRStatus(String companyId) throws TGOCPRestException {
		Map<String, Integer> countMap = new HashMap<>();
		CompletableFuture<Integer> approved_count = bpClient.getBPRsCountCompletable(BPR_STATUS[0], companyId);
		CompletableFuture<Integer> inactive_count = bpClient.getBPRsCountCompletable(BPR_STATUS[1], companyId);
		CompletableFuture<Integer> request_count = bpClient.getBPRsCountCompletable(BPR_STATUS[2], companyId);
		CompletableFuture<Integer> rejected_count = bpClient.getBPRsCountCompletable(BPR_STATUS[3], companyId);
		CompletableFuture<Integer> error_count = bpClient.getBPRsCountCompletable(BPR_STATUS[4], companyId);
		CompletableFuture<Integer> total_count = bpClient.getBPRsCountCompletable(BPR_STATUS[5], companyId);

		CompletableFuture.allOf(approved_count,inactive_count,request_count,rejected_count,error_count,total_count).join();
		try {
			countMap.put("TOTAL", total_count.get());
			countMap.put("ERROR", error_count.get());
			countMap.put("REJECTED", rejected_count.get());
			countMap.put("REQUESTED", request_count.get());
			countMap.put("TESTING", inactive_count.get());
			countMap.put("APPROVED", approved_count.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return Collections.unmodifiableMap(countMap);
	}

	@Override
	public ListingQueryResponse<BaseTradingAddressType> getEdis(String companyId, String bprId, boolean countOnly,
			Integer limit, Integer startingFrom) throws TGOCPRestException {
		ListingQueryResponse<BaseTradingAddressType> listResponse = new ListingQueryResponse<>();
		if (!CommonUtil.isEmpty(bprId)) {
			StringBuilder filterQuery = new StringBuilder();
			filterQuery.append("?bprId=").append(bprId);
			if (countOnly) {
				filterQuery.append("&countOnly=").append(countOnly);
				Integer count = bpClient.getEdiCount(filterQuery.toString(), companyId, bprId);
				listResponse.setItemCount(count);

			} else {
				filterQuery.append("&after=").append(startingFrom);
				filterQuery.append("&limit=").append(limit);
				PartnerTradingAddressType resp = bpClient.getEdiList(filterQuery.toString(), companyId, bprId);
				if (!ObjectUtils.isEmpty(resp)) {
					List<BaseTradingAddressType> list = resp.getTradingAddress();
					if (!ObjectUtils.isEmpty(list)) {
						listResponse.setItemList(list);
					}
				}
			}

		}
		return listResponse;
	}

	@Autowired
	private BusinessUntilRestClient businessUntilRestClient;

	@Override
	@Loggable
	public ListingQueryResponse<TPDirectory> getBpDetails(BPRFilterSearchQuery request)
			throws ParseException, TGOCPBaseException {

		ListingQueryResponse<TPDirectory> tprResponseEntity = new ListingQueryResponse<>();
		if (CommonUtil.isEmpty(request.getLimit())
				|| (!CommonUtil.isEmpty(request.getLimit()) && Integer.parseInt(request.getLimit()) > TP_QUERY_LIMIT)) {
			request.setLimit(appProperties.getTpDirectoryRowsPerPage());
		}
		// tprResponseEntity
		List<TPDirectoryDetailsType> bplist = getBusinessPartnerList(request).getItemList();
		List<TPDirectory> bpDetails = new ArrayList<>();

		if (null != bplist && !bplist.isEmpty()) {
			ListIterator<TPDirectoryDetailsType> tplistIterator = bplist.listIterator();
			while (tplistIterator.hasNext()) {
				TPDirectory bpr = new TPDirectory();
				try {

					TPDirectoryDetailsType detail = tplistIterator.next();
					bpr.setBprId(detail.getBprId());
					bpr.setBusinessUnitId(detail.getPartnerCompanyId());
					bpr.setBprStatus(detail.getBprStatus());
					bpr.setFacadeCompanyId(detail.getFacadeCompanyId());
					bpr.setName(detail.getPartnerCompanyName());
					bpr.setDisplayName(detail.getPartnerCompanyDisplayName());
					bpr.setStatus(detail.getPartnerShipStatus());
					bpr.setCreateTimeStamp(detail.getPartnerShipActivationDate());
					try {
						List<BaseTradingAddressType> edis = getEdis(request.getCompanyId(), detail.getBprId(), false,
								TP_QUERY_LIMIT, 0).getItemList();
						List<String> ediList = new ArrayList<>();
						for (BaseTradingAddressType edi : edis) {
							String qualifier = "";
							if (edi.getQualifier() != null) {
								qualifier = edi.getQualifier();
							}
							ediList.add(qualifier + ":" + edi.getAddress());
						}
						bpr.setEdiAddresses(ediList);
					} catch (Exception e) {
						logger.error("EDI extraction failed ::", e);
					}

					BusinessUnitDetailsType businessType = businessUntilRestClient
							.getBusinessUnitDetailsType(detail.getPartnerCompanyId());
					bpr.setTradingPartnerType(CommonUtil.isEmpty(businessType.getCompanyParticipationType()) ? ""
							: businessType.getCompanyParticipationType());
					bpr.setTradingPartnerType(CommonUtil.isEmpty(businessType.getCompanyParticipationType()) ? ""
							: businessType.getCompanyParticipationType());
					if (null != businessType.getCompanyAddress()) {
						bpr.setAddressLine1(CommonUtil.isEmpty(businessType.getCompanyAddress().getAddressLine1()) ? ""
								: businessType.getCompanyAddress().getAddressLine1());
						bpr.setAddressLine2(CommonUtil.isEmpty(businessType.getCompanyAddress().getAddressLine2()) ? ""
								: businessType.getCompanyAddress().getAddressLine2());
						bpr.setCity(CommonUtil.isEmpty(businessType.getCompanyAddress().getCity()) ? ""
								: businessType.getCompanyAddress().getCity());
						bpr.setState(CommonUtil.isEmpty(businessType.getCompanyAddress().getState()) ? ""
								: businessType.getCompanyAddress().getState());
						bpr.setCountryCode(businessType.getCompanyAddress().getCountryCode() == null ? ""
								: businessType.getCompanyAddress().getCountryCode().toString());

					}
					bpr.setWebsite(CommonUtil.isEmpty(businessType.getCompanyWebsiteURL()) ? ""
							: businessType.getCompanyWebsiteURL());

				} catch (Exception e) {
					logger.error("Exception while fetching BPR details", e);
				} finally {
					bpDetails.add(bpr);
				}
			}
		}

		tprResponseEntity.setItemList(bpDetails);

		return tprResponseEntity;

	}

	@Override
	@Loggable
	public CMDSuccessResponse deleteFacade(String facadeId, UserInfo userInfo) throws TGOCPRestException {

		return bpClient.deleteFacade(facadeId, userInfo);
	}

	private List<TPDirectoryDetailsType> modifyList(List<TPDirectoryDetailsType> list) {
		list.stream().forEach(tp -> {
			tp.setPartnerCompanyDisplayName(
					(!StringUtils.isEmpty(tp.getPartnerCompanyDisplayName())) ? tp.getPartnerCompanyDisplayName()
							: (!StringUtils.isEmpty(tp.getFacadeCompanyName())) ? tp.getFacadeCompanyName()
									: tp.getPartnerCompanyName());
			tp.setBprStatus(CommonUtil.toTitleCase(tp.getBprStatus()));

		});
		return list;

	}

}
