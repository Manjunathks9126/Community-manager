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
 08/03/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.util;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;

@Component("jacksonWrapper")
public class JacksonWrapper extends com.fasterxml.jackson.databind.ObjectMapper {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(JacksonWrapper.class);

	public JacksonWrapper() {
		super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Override
	public String writeValueAsString(Object value) {
		String jsonString = null;
		try {
			jsonString = super.writeValueAsString(value);
		} catch (IOException ex) {
			logger.error("writeValueAsString", ex);
		}
		return jsonString;
	}

	@Override
	public <T> T readValue(InputStream src, Class<T> valueType) {
		T t = null;
		try {
			t = super.readValue(src, valueType);
		} catch (IOException ex) {
			logger.error( "readValue", ex);
		}
		return t;
	}

	@Override
	public <T> T readValue(String response, Class<T> valueType) {
		T t = null;
		try {
			t = super.readValue(response, valueType);
		} catch (IOException ex) {
			logger.error("readValue", ex);
		}
		return t;
	}

}
