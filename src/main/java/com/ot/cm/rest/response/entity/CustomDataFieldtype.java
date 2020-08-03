package com.ot.cm.rest.response.entity;

import java.util.Arrays;

public class CustomDataFieldtype extends CustomFieldEntity {

	private String[] answers;

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "CustomDataFieldtype [answers=" + Arrays.toString(answers) + "]";
	}
	
	
}
