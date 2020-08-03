package com.ot.cm.business.service;

import com.ge.gxs.icpx.utils.EncryptionException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.vo.UserInfo;

public interface TradingPartnerGraphService {

	public String getEncryptedUserDetails(String userDetailsString) throws EncryptionException;

	public boolean getTPGraphSubsription(UserInfo userInfo) throws TGOCPRestException;

}
