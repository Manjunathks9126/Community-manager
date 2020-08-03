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

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.TradingPartnerModifyEntity;
import com.ot.cm.rest.request.entity.search.TPRFilterSearchQuery;
import com.ot.cm.rest.response.CMSSuccessResponse;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.TPDirectoryFilterOptionsEntity;
import com.ot.cm.rest.response.entity.TPRData;
import com.ot.cm.rest.response.entity.TradingPartnerRelationshipDetailsEntity;
import com.ot.cm.vo.UserInfo;

public interface TradingPartnerDirectoryService {

	String STATUS_PENDING = "PENDING";
	String EXPORT_CONTEXT = "TPR List";
	String APPLICATION_NAME = "CM";
	Integer TP_QUERY_LIMIT = 999;

	enum FileType {
		EXCEL, PDF, CSV
	};

	public TPDirectoryFilterOptionsEntity getTPDirectoryFilterOptions() throws CMApplicationException;

	/**
	 * @param filterObject
	 * @return ListingQueryResponse<TPRData>
	 * @throws TGOCPRestException
	 */
	public ListingQueryResponse<TPRData> getTPRData(TPRFilterSearchQuery filterObject) throws TGOCPRestException;

	public TradingPartnerRelationshipDetailsEntity[] getTradingpartnerDetails(String tprId) throws TGOCPRestException;

	/**
	 * @param tprId
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 */
	public CMSSuccessResponse[] terminateTradingPartnerRelationship(TradingPartnerModifyEntity[] tprId)
			throws TGOCPRestException;

	/**
	 * @param tprId
	 * @param updatedData
	 * @param userInfo
	 * @return CMSSuccessResponse
	 * @throws TGOCPRestException
	 */
	public CMSSuccessResponse[] approveRejectTradingPartnerRelationship(TradingPartnerModifyEntity[] updatedData,
			UserInfo userInfo) throws TGOCPRestException;
}
