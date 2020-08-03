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
 Jul 30, 2019  Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.util.List;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.entity.CMDNeoRole;
import com.ot.cm.vo.UserInfo;

/**
 * Service class to handle custom roles
 *
 */
public interface CustomRolesService {

	/**
	 * 
	 * @param buId
	 * @return
	 * @throws TGOCPRestException
	 */
	List<CMDNeoRole> getCustomFieldRoles(String buId, String userId) throws TGOCPRestException;

	List<String> getUsers(String roleId, UserInfo userInfo) throws TGOCPRestException;

}
