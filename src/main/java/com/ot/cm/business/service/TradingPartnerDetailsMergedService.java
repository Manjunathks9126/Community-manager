package com.ot.cm.business.service;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.TPRSearchQuery;
import com.ot.cm.rest.response.entity.SearchQueryResponse;
import com.ot.cm.vo.TradingParnterMergedEDIAddress;

public interface TradingPartnerDetailsMergedService {
	public SearchQueryResponse<TradingParnterMergedEDIAddress> getPartnerMergedEDIAddresses(TPRSearchQuery searchQuery,
			String senderCompanyId) throws TGOCPRestException;
}
