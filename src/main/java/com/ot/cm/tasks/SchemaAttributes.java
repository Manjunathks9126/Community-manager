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
 Mar 25, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.tasks;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author ssen
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaAttributes {
	private String title;
//	private boolean mandatory;
	private int maximum;
	private int minimum;
	private String pattern;
	private String type;
	private Map<String, SchemaAttributes> properties;
	private List<String> required;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}*/

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Map<String, SchemaAttributes> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, SchemaAttributes> properties) {
		this.properties = properties;
	}
	
	public List<String> getRequired() {
		return required;
	}

	public void setRequired(List<String> required) {
		this.required = required;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SchemaAttributes [title=").append(title)
				.append(", maximum=").append(maximum).append(", minimum=").append(minimum).append(", pattern=")
				.append(pattern).append(", type=").append(type).append(", properties=").append(properties)
				.append(", required=").append(required).append("]");
		return builder.toString();
	}

}
