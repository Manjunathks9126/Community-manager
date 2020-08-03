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

import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v2.entity.TradingPartnerRelationshipDetailsType;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.TPRRestClient;
import com.ot.cm.rest.request.entity.search.TPRSearchQuery;
import com.ot.cm.rest.response.entity.SearchQueryResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.TradingParnterEDIAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The {@code TradingPartnerDetailsServiceImpl} Implements all of
 * TradingPartnerDetailsService operations
 */
@Component
public class TradingPartnerDetailsServiceImpl implements TradingPartnerDetailsService {

	@Autowired
	TPRRestClient tprRestClient;
	@Autowired
	EDIAddressService EDIAddressService;

	/*
	 * Fetches Trading Partner EDI address from CMS
	 *
	 * @see com.ot.cm.business.service.TradingPartnerDetailsService#getPartnerEDIAddresses(com.ot.cm.request.search.entity.TPRSearchQuery,
	 *      java.lang.String)
	 */
	@Override
	public SearchQueryResponse<TradingParnterEDIAddress> getPartnerEDIAddresses(TPRSearchQuery tprSearchQuery,
			String ownerCompanyId) throws TGOCPRestException {

		SearchQueryResponse<TradingParnterEDIAddress> tprResponseEntity = new SearchQueryResponse<TradingParnterEDIAddress>();
		StringBuilder serarchParameters = new StringBuilder();
		serarchParameters.append("ownerCompanyId=");
		serarchParameters.append(ownerCompanyId);
		serarchParameters.append("&partnerCompanyId=");
		serarchParameters.append(tprSearchQuery.getPartnerCompanyId());
		if (!CommonUtil.isEmpty(tprSearchQuery.getPartnerediaddress())) {
			serarchParameters.append("&tradingAddress=");
			serarchParameters.append(tprSearchQuery.getPartnerediaddress());
		}
		if (!CommonUtil.isEmpty(tprSearchQuery.getDateFrom()) || !CommonUtil.isEmpty(tprSearchQuery.getDateTo())) {
			try {
				serarchParameters.append("&creationDate="
						+ CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", tprSearchQuery.getDateFrom()) + ","
						+ CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", tprSearchQuery.getDateTo()));
			} catch (ParseException e) {
			} catch (TGOCPBaseException e) {
			}

		}
		if (tprSearchQuery.isCountOnly()) {
			serarchParameters.append("&countOnly=true");
			String itemCount = tprRestClient.getTPREDICount(serarchParameters.toString());
			if (!CommonUtil.isEmpty(itemCount))
				tprResponseEntity.setItemCount(Integer.parseInt(itemCount));
		} else {
			serarchParameters.append("&billingsplitRequired=true");
			Set<String> partnerMasterEDIAddresses = partnerMasterEDIAddresses(tprSearchQuery);
			List<TradingParnterEDIAddress> ediAddressList = buildPartnerEDIAddresses(
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
	private List<TradingParnterEDIAddress> buildPartnerEDIAddresses(TradingPartnerRelationshipDetailsType[] tprDetails,
			Set<String> partnerMasterEDIAddresses) {
		List<TradingParnterEDIAddress> ediAddressList = new ArrayList<>();
		for (TradingPartnerRelationshipDetailsType tpr : tprDetails) {
			TradingParnterEDIAddress edi = new TradingParnterEDIAddress();
			deriveEDIAddressRelation(partnerMasterEDIAddresses, tpr, edi);
			edi.setCreateTimeStamp(tpr.getActivityLog().getCreateTimeStamp());
			edi.setPartnershipDirection(tpr.getPartnershipDirection());
			edi.setTradingPartnerRelationshipId(tpr.getTradingPartnerRelationshipId());
			edi.setBillingSplit(tpr.getBillingInfo());
			ediAddressList.add(edi);
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
	private void deriveEDIAddressRelation(Set<String> partnerMasterEDIAddresses,
			TradingPartnerRelationshipDetailsType tpr, TradingParnterEDIAddress edi) {

		String ownerQualifer = tpr.getOwnerTradingAddress().getQualifier();
		String ownerTradingAddress = (ownerQualifer != null)
				? (ownerQualifer + ":" + tpr.getOwnerTradingAddress().getAddress())
				: ":" + tpr.getOwnerTradingAddress().getAddress();

		String partnerQualifier = tpr.getPartnerTradingAddress().getQualifier();

		String partnerTradingAddress = (partnerQualifier != null)
				? (partnerQualifier + ":" + tpr.getPartnerTradingAddress().getAddress())
				: ":" + tpr.getPartnerTradingAddress().getAddress();

		if (partnerMasterEDIAddresses.contains(ownerTradingAddress)) {
			edi.setPartnerEDIAddress(ownerTradingAddress);
			edi.setOwnerEDIAddress(partnerTradingAddress);
		} else {

			edi.setPartnerEDIAddress(partnerTradingAddress);
			edi.setOwnerEDIAddress(ownerTradingAddress);
		}

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


    /**
     * Get All EdI List for given company
     * Needed in Onboarding Flow */
    @Override
	public Set<String> getAllEdis(String compnayId) throws TGOCPRestException {
        TPRSearchQuery tprSearchQuery = new TPRSearchQuery();
        tprSearchQuery.setPartnerCompanyId(compnayId);
        Set<String> ediSet = this.partnerMasterEDIAddresses(tprSearchQuery);

        return ediSet;
    }

}
