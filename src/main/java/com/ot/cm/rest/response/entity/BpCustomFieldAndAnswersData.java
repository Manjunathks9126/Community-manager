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

import java.util.Arrays;

/**
 * @author ssen
 *
 */
public class BpCustomFieldAndAnswersData {
	
	private CustomFieldEntity customField;
	private String[] answers;
    private String answerCreationDate;
    private String answerLastModifiedDate;
    private String answerCreatedBy;
    private String answerModifiedBy;
    private FileObject[] fileObjects;
    
	public FileObject[] getFileObjects() {
		return fileObjects;
	}
	public void setFileObjects(FileObject[] fileObjects) {
		this.fileObjects = fileObjects;
	}
	public CustomFieldEntity getCustomField() {
		return customField;
	}
	public void setCustomField(CustomFieldEntity customField) {
		this.customField = customField;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}	
	public String getAnswerCreationDate() {
		return answerCreationDate;
	}
	public void setAnswerCreationDate(String answerCreationDate) {
		this.answerCreationDate = answerCreationDate;
	}
	public String getAnswerLastModifiedDate() {
		return answerLastModifiedDate;
	}
	public void setAnswerLastModifiedDate(String answerLastModifiedDate) {
		this.answerLastModifiedDate = answerLastModifiedDate;
	}
	public String getAnswerCreatedBy() {
		return answerCreatedBy;
	}
	public void setAnswerCreatedBy(String answerCreatedBy) {
		this.answerCreatedBy = answerCreatedBy;
	}
	public String getAnswerModifiedBy() {
		return answerModifiedBy;
	}
	public void setAnswerModifiedBy(String answerModifiedBy) {
		this.answerModifiedBy = answerModifiedBy;
	}
	@Override
	public String toString() {
		return "BpCustomFieldAndAnswersList [customField=" + customField + ", answers=" + Arrays.toString(answers)
				+ ", answerCreationDate=" + answerCreationDate + ", answerLastModifiedDate=" + answerLastModifiedDate
				+ ", answerCreatedBy=" + answerCreatedBy + ", answerModifiedBy=" + answerModifiedBy + "]";
	}	

}
