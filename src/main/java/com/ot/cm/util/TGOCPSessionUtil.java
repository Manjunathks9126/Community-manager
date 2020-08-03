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
package com.ot.cm.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gxs.services.imapi.common.entities.ImRestSessionHeaders;
import com.gxs.services.imapi.common.entities.ImRestSessionType;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.vo.UserInfo;
import com.ot.session.entity.TGOCPSMUserVO;

/**
 * This class contains the utility methods required for TGO CP Session.
 * @author Dwaraka
 *
 */
public class TGOCPSessionUtil {

	public static UserInfo getUserInfoInSession(HttpServletRequest httpServletRequest) {
		UserInfo userInfo = (UserInfo) httpServletRequest.getSession()
				.getAttribute(EnvironmentConstants.USER_INFO_SESSION_KEY);

		return userInfo;
	}

	private static Map <String, String> sessionHeadersMap = null;
	public static Map<String, String> prepareUserSessionHeadersMap(TGOCPSMUserVO tgocpSessionVO) {
		    sessionHeadersMap = new HashMap<String, String>();
			sessionHeadersMap.put(ImRestSessionHeaders.im_community_id.name(), tgocpSessionVO.getCommunityID());
			sessionHeadersMap.put(ImRestSessionHeaders.im_principal_type.name(), ImRestSessionType.USER_SESSION.name());
			sessionHeadersMap.put(ImRestSessionHeaders.im_service_instance_id.name(),tgocpSessionVO.getServiceInstanceID() );
			sessionHeadersMap.put(ImRestSessionHeaders.im_user_id.name(), tgocpSessionVO.getPrincipalID());
			sessionHeadersMap.put(ImRestSessionHeaders.im_bu_id.name(), tgocpSessionVO.getParentID());
		return sessionHeadersMap;
	}
}
