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
 Feb 6, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.vo.bulk;

/**
 * @author ssen
 *
 */
public class ValidationMessage {
	private String required;
	private String minLength;
	private String maxLength;
	private String pattern;
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getMinLength() {
		return minLength;
	}
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidationMessage [required=").append(required).append(", minLength=").append(minLength)
				.append(", maxLength=").append(maxLength).append(", pattern=").append(pattern).append("]");
		return builder.toString();
	}

}
