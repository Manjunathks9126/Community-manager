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

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.TPRSearchQuery;
import com.ot.cm.rest.response.entity.SearchQueryResponse;
import com.ot.cm.vo.TradingParnterEDIAddress;

import java.util.Set;

/**
 * The {@code TradingPartnerDetailsService} interface contains set of methods
 * related Trading Partner Relation details
 */
public interface TradingPartnerDetailsService {
    public SearchQueryResponse<TradingParnterEDIAddress> getPartnerEDIAddresses(TPRSearchQuery searchQuery,
                                                                                String senderCompanyId) throws TGOCPRestException;

     Set<String> getAllEdis(String compnayId) throws TGOCPRestException;

}
