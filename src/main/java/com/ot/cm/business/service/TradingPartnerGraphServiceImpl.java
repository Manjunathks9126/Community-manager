package com.ot.cm.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ge.gxs.icpx.utils.EncryptionException;
import com.ge.gxs.icpx.utils.EncryptionManager;
import com.gxs.services.bsapi.rs.v2.entity.ServiceInstanceDetailType;
import com.gxs.services.bsapi.rs.v2.entity.UserSubcriptionDetailsType;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.UsersRestClient;
import com.ot.cm.vo.UserInfo;

@Service
public class TradingPartnerGraphServiceImpl implements TradingPartnerGraphService {

	private EncryptionManager encryptionManager;

	@Autowired
	private UsersRestClient userRest;

	public TradingPartnerGraphServiceImpl() throws EncryptionException {

		encryptionManager = EncryptionManager.getInstance(
				TradingPartnerGraphServiceImpl.class.getClassLoader().getResource("").getPath() + "config/");

	}

	@Override
	public String getEncryptedUserDetails(String userDetailsString) throws EncryptionException {
		return encryptionManager.encrypt(userDetailsString);
	}

	@Override
	public boolean getTPGraphSubsription(UserInfo userInfo) throws TGOCPRestException {

		UserSubcriptionDetailsType userRoles = userRest.getRoles(userInfo.getCompanyId(), userInfo.getUserId());

		List<ServiceInstanceDetailType> siDetailList = userRoles.getUserSubcriptions();
		ServiceInstanceDetailType siDetailType = siDetailList.stream().filter(si -> null != si.getServiceInstanceMap()
				&& si.getServiceInstanceMap().trim().equals("cms_analytics_si")).findAny().orElse(null);

		if (null != siDetailType) {
			return true;
		} else {
			return false;
		}
	}

}
