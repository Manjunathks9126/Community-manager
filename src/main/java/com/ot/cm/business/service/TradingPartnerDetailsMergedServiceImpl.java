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
 02/15/2018    Madan                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v2.entity.TradingPartnerRelationshipDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.TPRRestClient;
import com.ot.cm.rest.request.entity.search.TPRSearchQuery;
import com.ot.cm.rest.response.entity.SearchQueryResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.TradingParnterMergedEDIAddress;
import com.ot.cm.vo.TradingPartnerBillingSplit;

/**
 * The {@code TradingPartnerDetailsServiceImpl} Implements all of
 * TradingPartnerDetailsService operations
 */
@Component
public class TradingPartnerDetailsMergedServiceImpl implements TradingPartnerDetailsMergedService {

	@Autowired
	TPRRestClient tprRestClient;
	@Autowired
	EDIAddressService EDIAddressService;

	/**
	 * Fetches Trading Partner EDI address from CMS
	 * 
	 * @see com.ot.cm.business.service.TradingPartnerDetailsService#getPartnerEDIAddresses(com.ot.cm.request.search.entity.TPRSearchQuery,
	 *      java.lang.String)
	 */
	@Override
	public SearchQueryResponse<TradingParnterMergedEDIAddress> getPartnerMergedEDIAddresses(
			TPRSearchQuery tprSearchQuery, String senderCompanyId) throws TGOCPRestException {

		SearchQueryResponse<TradingParnterMergedEDIAddress> tprResponseEntity = new SearchQueryResponse<TradingParnterMergedEDIAddress>();
		StringBuilder serarchParameters = new StringBuilder();
		serarchParameters.append("where_sender_company_id=");
		serarchParameters.append(senderCompanyId);
		serarchParameters.append("&where_receiver_company_id=");
		serarchParameters.append(tprSearchQuery.getPartnerCompanyId());

		if (tprSearchQuery.isCountOnly()) {
			serarchParameters.append("&count_only=true");
			String itemCount = tprRestClient.getTPREDICount(serarchParameters.toString());
			if (!CommonUtil.isEmpty(itemCount))
				tprResponseEntity.setItemCount(Integer.parseInt(itemCount));
		} else {
			Set<String> partnerMasterEDIAddresses = partnerMasterEDIAddresses(tprSearchQuery);
			List<TradingParnterMergedEDIAddress> ediAddressList = mergedPartnerEDIAddresses(
					tprRestClient.getTPREDIAddresses(serarchParameters.toString()), partnerMasterEDIAddresses);
			tprResponseEntity.setItemList(ediAddressList);
		}
		return tprResponseEntity;
	}

	/**
	 * Transform the list of EDI address from CMS to provide list of Merged EDI
	 * Address i.e If EDI address partner ship direction is both display data in
	 * a single row in UI
	 * 
	 * @param tprDetails
	 * @return
	 */
	private List<TradingParnterMergedEDIAddress> mergedPartnerEDIAddresses(
			TradingPartnerRelationshipDetailsType[] tprDetails, Set<String> partnerMasterEDIAddresses) {
		List<TradingParnterMergedEDIAddress> ediAddressList = new ArrayList<>();
		Map<String, TradingParnterMergedEDIAddress> mergedEDIMap = new HashMap<>();
		for (TradingPartnerRelationshipDetailsType tpr : tprDetails) {
			// Derives EDI address
			String ediAddress = deriveEDIAddress(partnerMasterEDIAddresses, tpr);
			if (mergedEDIMap.containsKey(ediAddress)) {
				TradingParnterMergedEDIAddress edi = mergedEDIMap.get(ediAddress);
				edi.getPartnershipDirection().add(tpr.getPartnershipDirection());
				edi.getTradingPartnerRelationshipId().add(tpr.getTradingPartnerRelationshipId());
				edi.getBillingSplit().add(buildBillingInfo(tpr));
			} else {
				TradingParnterMergedEDIAddress edi = new TradingParnterMergedEDIAddress();
				edi.setCreateTimeStamp(tpr.getActivityLog().getCreateTimeStamp());
				ArrayList<String> direction = new ArrayList<>();
				direction.add(tpr.getPartnershipDirection());
				ArrayList<String> tpid = new ArrayList<>();
				tpid.add(tpr.getTradingPartnerRelationshipId());
				edi.setPartnershipDirection(direction);
				edi.setTradingPartnerRelationshipId(tpid);
				edi.setEdiAddress(ediAddress);
				ArrayList<TradingPartnerBillingSplit> billingSplitList = new ArrayList<>();
				billingSplitList.add(buildBillingInfo(tpr));
				edi.setBillingSplit(billingSplitList);
				mergedEDIMap.put(ediAddress, edi);
				ediAddressList.add(edi);
			}
		}
		return ediAddressList;

	}

	/**
	 * Derives whose mail address is partner EDI address in context of TPR by
	 * comparing with master addresses of partner EDI address Reason is, CMS
	 * sends data always initiator BU address as owner address in either cases
	 * of TPR i.e A-B or B-A. Hence it is not correct to consider always partner
	 * EDI address
	 *
	 * @param partnerMasterEDIAddresses
	 * @param tpr
	 * @return
	 */
	private String deriveEDIAddress(Set<String> partnerMasterEDIAddresses, TradingPartnerRelationshipDetailsType tpr) {

		String qualifier = tpr.getOwnerTradingAddress().getQualifier();
		String ownerTradingAddress = (qualifier != null) ? (qualifier + ":" + tpr.getOwnerTradingAddress().getAddress())
				: ":" + tpr.getOwnerTradingAddress().getAddress();

		qualifier = tpr.getPartnerTradingAddress().getQualifier();
		String partnerTradingAddress = (qualifier != null)
				? (qualifier + ":" + tpr.getPartnerTradingAddress().getAddress())
				: ":" + tpr.getPartnerTradingAddress().getAddress();
		return partnerMasterEDIAddresses.contains(ownerTradingAddress) ? ownerTradingAddress : partnerTradingAddress;
	}

	/**
	 * Build Split Info
	 * 
	 * @param tpr
	 * @return
	 */
	private TradingPartnerBillingSplit buildBillingInfo(TradingPartnerRelationshipDetailsType tpr) {
		TradingPartnerBillingSplit billingSplit = new TradingPartnerBillingSplit();
		billingSplit.setConnectOutPayee(tpr.getBillingInfo().getConnectOutPayee().toString());
		billingSplit.setReceivePayee((tpr.getBillingInfo().getReceivePayee().toString()));
		billingSplit.setStoragePayee(tpr.getBillingInfo().getStoragePayee().toString());
		billingSplit.setSendPayee((tpr.getBillingInfo().getSendPayee().toString()));
		billingSplit.setPartnershipDirection(tpr.getPartnershipDirection());
		return billingSplit;
	}

	/**
	 * Fetches all ediaddresss of partner company which is master list
	 * 
	 * @param tprSearchQuery
	 * @return
	 * @throws TGOCPRestException
	 */
	private Set<String> partnerMasterEDIAddresses(TPRSearchQuery tprSearchQuery) throws TGOCPRestException {
		List<TradingAddressDetailsType> masterList = EDIAddressService
				.getTotalEDIAddresses(tprSearchQuery.getPartnerCompanyId());
		Set<String> ediSet = new HashSet<>();
		for (TradingAddressDetailsType tpr : masterList) {
			String qualifier = tpr.getTradingAddress().getQualifier();
			String ediAddress = (qualifier != null) ? (qualifier + ":" + tpr.getTradingAddress().getAddress())
					: ":" + tpr.getTradingAddress().getAddress();
			ediSet.add(ediAddress);
		}
		return ediSet;
	}
}
