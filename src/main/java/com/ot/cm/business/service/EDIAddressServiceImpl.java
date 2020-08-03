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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.EDIAddressRestClient;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.CommonUtil;

/**
 * The {@code EDIAddressServiceImpl} Implements all of EDIAddressService
 * operations
 */
@Component
public class EDIAddressServiceImpl implements EDIAddressService {

	@Autowired
	EDIAddressRestClient EDIAddressRestClient;

	/**
	 * Method communication method of passed EDI Address. Step1:Fetch MailBox Id
	 * of EDI Address Step2:Return DeliveryMethod for MailBox Id which is comms
	 * method
	 * 
	 */
	@Override
	public String getEDIAddressesComms(String ediAddress) throws TGOCPRestException {

		TradingAddressDetailsType tradingAddressDetailsType = EDIAddressRestClient
				.getEdiInfo(tranformEDIForCMS(ediAddress));

		return tradingAddressDetailsType.getDeliveryMethodName();
	}

	private String tranformEDIForCMS(String ediAddress) {
		return ediAddress.charAt(0) == ':' ? ediAddress.substring(1) : ediAddress;
	}

	/**
	 * Method to fetch all EDI Addresses for given Company
	 * 
	 */
	@Override
	public List<TradingAddressDetailsType> getTotalEDIAddresses(String companyID) throws TGOCPRestException {

		String countOnly = "count_only=true";
		ArrayList<TradingAddressDetailsType> ediAddressList = new ArrayList<>();
		int itemCount = Integer.parseInt(EDIAddressRestClient.getEDICount(companyID, countOnly));
		int after = 0;
		int limit = 1000;

		do {
			StringBuilder serarchParameters = new StringBuilder();
			serarchParameters.append("?&after=");
			serarchParameters.append(after);
			serarchParameters.append("&limit=");
			serarchParameters.append(limit);
			TradingAddressDetailsType[] edis = EDIAddressRestClient.getEDIAddresses(companyID,
					serarchParameters.toString());
			Collections.addAll(ediAddressList, edis);
			after = after + 1000;
			limit = itemCount - after;
		} while (limit >= 0);
		return ediAddressList;

	}

	/**
	 * Fetch only Basic Trading addresses
	 * 
	 * @throws TGOCPRestException
	 */
	@Override
	public ListingQueryResponse<Map<String, String>> getBasicEdiAddresses(String companyId, boolean countOnly,
			Integer limit, Integer after) throws TGOCPRestException {
		ListingQueryResponse<Map<String, String>> basicEdiAddress = new ListingQueryResponse<Map<String, String>>();
		if (countOnly) {
			int count = getEdiAddressCount(companyId);
			basicEdiAddress.setItemCount(count);

		} else {
			List<Map<String, String>> tradingAddress = new ArrayList<>();
			List<TradingAddressDetailsType> ediAdderessList = getEDIAddresses(companyId, limit, after);
			for (TradingAddressDetailsType tradingAddressDetail : ediAdderessList) {
				String qualifier = CommonUtil.isEmpty(tradingAddressDetail.getTradingAddress().getQualifier()) ? ":"
						: tradingAddressDetail.getTradingAddress().getQualifier() + ":";
				String address = CommonUtil.isEmpty(tradingAddressDetail.getTradingAddress().getAddress()) ? ""
						: tradingAddressDetail.getTradingAddress().getAddress();
				String edi = qualifier + address;
				Map<String, String> ediMap = new HashMap<>();
				ediMap.put("ediAddress", edi);
				tradingAddress.add(ediMap);
			}
			basicEdiAddress.setItemList(tradingAddress);
		}
		return basicEdiAddress;
	}

	/**
	 * provide Trading addresses based on parameters
	 * 
	 * @param companyID
	 * @param limit
	 * @param after
	 * @return
	 * @throws TGOCPRestException
	 */
	private List<TradingAddressDetailsType> getEDIAddresses(String companyID, Integer limit, Integer after)
			throws TGOCPRestException {
		ArrayList<TradingAddressDetailsType> ediAddressList = new ArrayList<>();
		StringBuilder searchParam = new StringBuilder();
		if (limit != null) {
			searchParam.append("?&limit=").append(limit);
		}
		if (after != null) {
			if (searchParam.indexOf("?") > -1) {
				searchParam.append("&after=").append(after);
			} else {
				searchParam.append("?&after=").append(after);
			}
		}
		TradingAddressDetailsType[] edis = EDIAddressRestClient.getEDIAddresses(companyID, searchParam.toString());
		Collections.addAll(ediAddressList, edis);

		return ediAddressList;
	}

	/**
	 * provide total count of Trading addresses based on companyId
	 * 
	 * @param companyId
	 * @return
	 * @throws TGOCPRestException
	 */
	private int getEdiAddressCount(String companyId) throws TGOCPRestException {
		String countOnly = "count_only=true";
		int itemCount = Integer.parseInt(EDIAddressRestClient.getEDICount(companyId, countOnly));

		return itemCount;
	}


	@Override
	public String getVanProvider(@NonNull String companyId) throws TGOCPRestException {
		TradingAddressDetailsType[] edis = EDIAddressRestClient.getEDIAddresses(companyId, "?after=0&limit=1");
		if(null!=edis && edis.length!=0){
			return edis[0].getVanProvider().getCompanyName();
		}else return null;
	}

}
