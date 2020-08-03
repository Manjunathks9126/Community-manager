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
 09/03/2018    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ot.audit.vo.AuditInfo;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.rest.response.entity.ListingQueryResponse;

@Service
public class AuditLogService {
	@Autowired
	private NeoRestClient neoRestClient;

	/**
	 * 
	 * @param buId
	 * @return
	 * @throws TGOCPRestException
	 */
	public ListingQueryResponse<AuditInfo> getAuditLogs(String initiatorBUId, String custBUId,Boolean countOnly, Integer limit, Integer startsFrom) throws TGOCPRestException {
		if (!StringUtils.isEmpty(initiatorBUId) && !StringUtils.isEmpty(custBUId)) {
			return neoRestClient.getAuditLogs(initiatorBUId, custBUId, countOnly, limit, startsFrom);
		}
		return null;
	}

}
