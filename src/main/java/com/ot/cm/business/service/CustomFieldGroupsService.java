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
 Aug 17, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.CustomFieldCategoryType;
import com.ot.cm.rest.response.entity.CustomFieldGroupAndRoles;
import com.ot.cm.rest.response.entity.CustomFieldGroupType;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
public interface CustomFieldGroupsService {

	/**
	 * @param filterObject
	 * @param userInfo
	 * @return ListingQueryResponse<CustomFieldGroupType>
	 * @throws TGOCPRestException
	 * 
	 */
	ListingQueryResponse<CustomFieldGroupType> list(CustomFieldGroupFilterSearchQuery filterObject, UserInfo userInfo)
			throws TGOCPRestException;

	CustomFieldGroupAndRoles get(CustomFieldGroupFilterSearchQuery filterObject, UserInfo userInfo)
			throws TGOCPRestException;

	/**
	 * @param groupData
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse add(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException;

	/**
	 * @param groupId
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 * @throws CMApplicationException
	 */
	CMDSuccessResponse delete(String groupId, UserInfo userInfo) throws TGOCPRestException, CMApplicationException;

	/**
	 * @param groupData
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse edit(CustomFieldGroupType groupData, UserInfo userInfo) throws TGOCPRestException;

	CMDSuccessResponse grantAndRevokeRoles(CustomFieldGroupAndRoles groupData) throws TGOCPRestException;

	CustomFieldCategoryType getDefaultCategory(UserInfo userInfo) throws TGOCPRestException;

}
