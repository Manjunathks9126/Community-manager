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
import java.util.Map;

import com.gxs.services.bsapi.rs.v3.entity.BaseTradingAddressType;
import com.gxs.services.bsapi.rs.v3.entity.TPDirectoryDetailsType;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.BusinessPartnerFacades;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.TPDirectory;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
public interface BusinessPartnerService {

	String STATUS_PENDING = "PENDING";
	String EXPORT_CONTEXT_BPR = "BPR List";
	String APPLICATION_NAME = "CM";
	Integer TP_QUERY_LIMIT = 1000;

	enum FileType {
		EXCEL, PDF, CSV
	};

	CMDSuccessResponse addOrUpdateBpFacades(BusinessPartnerFacades fc, String ownerBuId, UserInfo userInfo)
			throws TGOCPRestException;

	/**
	 * @param filterObject
	 * @return ListingQueryResponse<TPDirectoryDetailsType>
	 * @throws TGOCPRestException
	 * @throws CMApplicationException
	 * @throws TGOCPBaseException 
	 * @throws ParseException 
	 */
	ListingQueryResponse<TPDirectoryDetailsType> getBusinessPartnerList(BPRFilterSearchQuery filterObject)
			throws TGOCPRestException, CMApplicationException, ParseException, TGOCPBaseException;

	/**
	 * @param companyId
	 * @param bprId
	 * @param countOnly
	 * @param limit
	 * @param startingFrom
	 * @return ListingQueryResponse<BaseTradingAddressType>
	 * @throws TGOCPRestException
	 */
	ListingQueryResponse<BaseTradingAddressType> getEdis(String companyId, String bprId, boolean countOnly,
			Integer limit, Integer startingFrom) throws TGOCPRestException;

	/**
	 * @param filterObject
	 * @return ListingQueryResponse<TPDirectory>
	 * @throws CMApplicationException
	 * @throws TGOCPRestException,
	 *             CMApplicationException
	 * @throws TGOCPBaseException 
	 * @throws ParseException 
	 */
	ListingQueryResponse<TPDirectory> getBpDetails(BPRFilterSearchQuery filterObject)
			throws TGOCPRestException, CMApplicationException, ParseException, TGOCPBaseException;

	/**
	 * @param facadeId
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse deleteFacade(String facadeId, UserInfo userInfo) throws TGOCPRestException;

	/**
	 * @param companyId
	 * @return Count Map
	 * @throws TGOCPRestException
	 */
	public Map<String, Integer> statsByBPRStatus(String companyId) throws TGOCPRestException;
}
