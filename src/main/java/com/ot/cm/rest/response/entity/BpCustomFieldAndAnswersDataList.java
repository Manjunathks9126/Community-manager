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
 Aug 27, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ssen
 *
 */
public class BpCustomFieldAndAnswersDataList {
	
	@JsonProperty("im-business-partner-customfield-answers")
	private BpCustomFieldAndAnswersData[] bpCustomFieldAndAnswersList;

	public BpCustomFieldAndAnswersData[] getBpCustomFieldAndAnswersList() {
		return bpCustomFieldAndAnswersList;
	}

	public void setBpCustomFieldAndAnswersList(BpCustomFieldAndAnswersData[] bpCustomFieldAndAnswersList) {
		this.bpCustomFieldAndAnswersList = bpCustomFieldAndAnswersList;
	}
	
	

}
