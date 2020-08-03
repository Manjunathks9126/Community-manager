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
 Aug 30, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response.entity;

import java.util.Arrays;

/**
 * @author ssen
 *
 */
public class CustomFieldAnswersResponse {

	private String[] createdBPCustomFieldAnswers;
	private String[] updatedBPCustomFieldAnswers;
	private String[] deletedBPCustomFieldAnswers;
	public String[] getCreatedBPCustomFieldAnswers() {
		return createdBPCustomFieldAnswers;
	}
	public void setCreatedBPCustomFieldAnswers(String[] createdBPCustomFieldAnswers) {
		this.createdBPCustomFieldAnswers = createdBPCustomFieldAnswers;
	}
	public String[] getUpdatedBPCustomFieldAnswers() {
		return updatedBPCustomFieldAnswers;
	}
	public void setUpdatedBPCustomFieldAnswers(String[] updatedBPCustomFieldAnswers) {
		this.updatedBPCustomFieldAnswers = updatedBPCustomFieldAnswers;
	}
	public String[] getDeletedBPCustomFieldAnswers() {
		return deletedBPCustomFieldAnswers;
	}
	public void setDeletedBPCustomFieldAnswers(String[] deletedBPCustomFieldAnswers) {
		this.deletedBPCustomFieldAnswers = deletedBPCustomFieldAnswers;
	}
	@Override
	public String toString() {
		return "CustomFieldAnswersResponse [createdBPCustomFieldAnswers=" + Arrays.toString(createdBPCustomFieldAnswers)
				+ ", updatedBPCustomFieldAnswers=" + Arrays.toString(updatedBPCustomFieldAnswers)
				+ ", deletedBPCustomFieldAnswers=" + Arrays.toString(deletedBPCustomFieldAnswers) + "]";
	}
	
	
}
