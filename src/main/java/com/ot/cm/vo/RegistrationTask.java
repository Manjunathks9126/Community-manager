package com.ot.cm.vo;

import com.ot.cm.rest.response.entity.OnboardingTask;

public class RegistrationTask extends OnboardingTask{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long position;
	private String schemaUrl;
	private String inputUrl;

	public String getSchemaUrl() {
		return schemaUrl;
	}

	public void setSchemaUrl(String schemaUrl) {
		this.schemaUrl = schemaUrl;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}
	
	
}
