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

import java.util.List;
import java.util.Map;

import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.entity.ListingQueryResponse;

/**
 * The {@code EDIAddressService} interface contains set of methods related EDI
 * Address details
 *
 */
public interface EDIAddressService {
	/**
	 * Returns EDI comms method.
	 * 
	 * @param ediAddress
	 * @return
	 * @throws TGOCPRestException
	 */
	public String getEDIAddressesComms(String ediAddress) throws TGOCPRestException;
	
	public List<TradingAddressDetailsType> getTotalEDIAddresses(String companyId) throws TGOCPRestException;

	public ListingQueryResponse<Map<String,String>> getBasicEdiAddresses(String companyId,boolean countOnly,Integer limit,Integer after) throws TGOCPRestException;

	/**
	 * @param companyId
	 * @return String
	 * @throws TGOCPRestException 
	 */
	public String getVanProvider(String companyId) throws TGOCPRestException;
	
}
