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
package com.ot.cm.messages;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for providing locale based error messages from
 * properties files.
 *
 */
@Component
public class LocalizationMessages {

	private static final Logger logger = LoggerFactory.getLogger(LocalizationMessages.class);

	@Autowired
	private MessageSource messageSource;

	private final String NOT_FOUND = "There is no description associated with given code:";

	public String getMessage(String code, Locale userLocale) {
		return getMessage(code, null, userLocale);
	}

	public String getMessage(String code, int statusCode, Locale userLocale) {
		return getMessage(code + "_" + statusCode, null, userLocale);
	}

	public String getMessage(String code, String[] params, Locale userLocale) {
		String message = null;

		try {
			message = messageSource.getMessage(code, params, userLocale);
		} catch (Exception e) {
			logger.error("getErrorMessage() ", e);
		}

		return (message != null ? message : NOT_FOUND.concat(code));
	}

}
