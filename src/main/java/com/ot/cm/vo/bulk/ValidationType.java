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
 Feb 4, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.vo.bulk;

import java.util.regex.Pattern;

import com.gxs.services.bsapi.rs.v3.entity.PreferredLanguageType;
import com.gxs.services.bsapi.rs.v3.entity.PreferredTimezoneType;
import com.ot.cm.constants.ApplicationConstants;

/**
 * @author ssen
 *
 */
public enum ValidationType {

	/*
	 * REQUIRED {
	 * 
	 * @Override public boolean validate(String dataToValidate, Object against)
	 * { return !CommonUtil.isEmpty(dataToValidate); } },
	 */
	MINLENGTH {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			if (dataToValidate != null && against instanceof Integer) {
				return dataToValidate.length() >= (int) against;
			}
			return false;
		}
	},
	MAXLENGTH {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			if (dataToValidate != null && against instanceof Integer) {
				return dataToValidate.length() <= (int) against;
			}

			return false;
		}
	},
	PATTERN {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			if (dataToValidate != null && against instanceof String) {
				String ptrn = (String) against;
				return Pattern.matches(ptrn, dataToValidate);
			}

			return false;
		}
	},
	FORBIDDEN_PATTERN {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			if (dataToValidate != null && against instanceof String) {
				String ptrn = (String) against;
				return !Pattern.compile(ptrn).matcher(dataToValidate).find();
			}

			return false;
		}
	},
	COUNTRY_CODE {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			try {
				// CountryCodeType.fromValue(dataToValidate); //Commenting from
				// time being
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	},
	LANGUAGE_CODE {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			try {
				PreferredLanguageType.fromValue(dataToValidate);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	},
	TIMEZONE {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			try {
				PreferredTimezoneType.fromValue(dataToValidate);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	},
	EMAIL {
		@Override
		public boolean validate(String dataToValidate, Object against) {
			if (dataToValidate != null) {
				return Pattern.matches(ApplicationConstants.EMAIL_PATTERN, dataToValidate);
			}

			return false;
		}

	};

	public String value() {
		return this.name();
	}

	public static ValidationType fromValue(String arg) {
		return valueOf(arg);
	}

	public abstract boolean validate(String dataToValidate, Object value);

}
