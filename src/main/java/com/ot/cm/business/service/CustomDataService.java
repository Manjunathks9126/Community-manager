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
 Sep 7, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.util.List;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.CustomFieldGroupFilterSearchQuery;
import com.ot.cm.rest.response.entity.BpCustomFieldAndAnswersData;
import com.ot.cm.rest.response.entity.CustomDataFieldGroupType;
import com.ot.cm.rest.response.entity.CustomDataSave;
import com.ot.cm.rest.response.entity.CustomFieldAnswersResponse;
import com.ot.cm.rest.response.entity.ImportCustomDataSave;
import com.ot.cm.vo.UserInfo;

/**
 * @author ssen
 *
 */
public interface CustomDataService{

	/**
	 * @param groupId
	 * @param partnerBuId
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException 
	 */
	BpCustomFieldAndAnswersData[] getPartnerFieldAnswerData(String groupId, String partnerBuId, UserInfo userInfo) throws TGOCPRestException;
	
	List<CustomDataFieldGroupType> getCustomFieldGroups(CustomFieldGroupFilterSearchQuery filterObject,String partnerBuId, UserInfo userInfo) throws TGOCPRestException;

	List<CustomFieldAnswersResponse> updatePartnerFieldAnswerData(String partnerBuId, UserInfo userInfo, List<CustomDataSave> answerData) throws TGOCPRestException;

	List<CustomFieldAnswersResponse> updateCustomFieldAnswerData(String targetBuid, UserInfo userInfo, List<ImportCustomDataSave> answerData) throws TGOCPRestException;
}
