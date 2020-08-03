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
 Nov 26, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ssen
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomFieldChoice {
	
	private String id;
	private String value;
	private String position;
	private String questionId;
	private String choiceIdentifier;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getChoiceIdentifier() {
		return choiceIdentifier;
	}
	public void setChoiceIdentifier(String choiceIdentifier) {
		this.choiceIdentifier = choiceIdentifier;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomFieldChoice [id=").append(id).append(", value=").append(value).append(", position=")
				.append(position).append(", questionId=").append(questionId).append(", choiceIdentifier=").append(choiceIdentifier).append("]");
		return builder.toString();
	}
	
	
	
}
