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
 Feb 11, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.constants;

/**
 * @author ssen
 *
 */
public interface BulkUploadConstants {
	

	String SEPARATOR = System.getProperty("file.separator");
	String TEMP_LOCATION = "temp-cm";
	int BULK_IMPORT_LIMIT = 1000;
	String EXCEL_EXT = ".xlsx";
	String MANDATORY_MARK = "*";
	String ROW_STATUS_HEADER = "Processed Status";
	String ROW_REMARKS_HEADER = "Reason for Failure";
	String STATUS_VALID = "Valid";
	String STATUS_REJECT = "Reject";
	String STATUS_SUCCESS = "Success";
	String STATUS_FAILED = "Failed";
	String STATUS_STAGED = "Staged";
	String STATUS_VALIDATING = "Validating";
	String STATUS_INVITING = "Inviting";
	String STATUS_PROCESSED = "Processed";
	String SENDER_EMAIL = "noreply@opentext.com";
	String BLANK_VALUE = "BLANK_VALUE";

}
