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
package com.ot.cm.rest.request.entity;

import java.util.Arrays;

import com.ot.cm.rest.response.entity.FileObject;

/**
 * @author ssen
 *
 */
public class ImCustomFieldAnswerMap {
	
	private String questionId;
	private String[] answers;
	private FileObject[] fileObjects;
	
	public FileObject[] getFileObjects() {
		return fileObjects;
	}
	public void setFileObjects(FileObject[] fileObjects) {
		this.fileObjects = fileObjects;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//		builder.append("ImCustomFieldAnswerMap [questionId=").append(questionId).append(", answers=")
//				.append(Arrays.toString(answers)).append(", fileObjects=")
//				.append(Arrays.toString(fileObjects)).append("]");
//		return builder.toString();
//	}
//	
	@Override
	public String toString() {
		return "ImCustomFieldAnswerMap [questionId=" + questionId + ", answers=" + Arrays.toString(answers)
				+ ", fileObjects=" + Arrays.toString(fileObjects) + "]";
	}

}
