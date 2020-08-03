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
package com.ot.cm.constants;

/**
 * This class contains all error constants.
 * 
 * @author Dwaraka
 *
 */
public final class ErrorCodes {
	private ErrorCodes() {

	}

	public static final String INVALID_INPUT = "TGOCPREST-4000";
	public static final String ENVIRONMENT_NOT_FOUND = "TGOCPENV-4000";
	public static final String UNACCEPTABLE_DATA_RECEIVED = "TGOCPAPP-4060";
	public static final String INTERNAL_SERVER_ERROR = "TGOCPAPP-5000";
	public static final String SM_SERVER_ERROR = "TGOCPSM-5000";

	public static final String SESSION_NOT_FOUND = "TGOCPREST-4040";
	public static final String UNAUTHORIZED = "TGOCPREST-4010";
	public static final String ENTITY_ALREADY_EXIST_IN_IM = "TGOCP-CMS-REST-1414";
	public static final String ENTITY_NOT_EXISTS_IN_IM = "TGOCP-CMS-REST-4040";
}
