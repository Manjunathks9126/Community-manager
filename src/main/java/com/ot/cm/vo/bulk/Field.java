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
 Feb 1, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.vo.bulk;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * @author ssen
 *
 */
public class Field {
	@NotNull
	private String label;
	@NotNull
	private String key;
	private boolean required;
	private Map<ValidationType, Object> validationRules;

	public Field() {

	}

	public Field(String key) {
		this.key = key;
		this.label = key;
	}

	public Field(String key, boolean required) {
		this.key = key;
		this.label = key;
		this.required = required;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Map<ValidationType, Object> getValidationRules() {
		return validationRules != null ? validationRules : new HashMap<>();
	}

	public void setValidationRules(Map<ValidationType, Object> validationTypeValueMap) {
		this.validationRules = validationTypeValueMap;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Field [label=").append(label).append(", key=").append(key).append(", required=")
				.append(required).append(", validationRules=").append(validationRules).append("]");
		return builder.toString();
	}

}
