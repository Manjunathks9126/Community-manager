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
 * This class contains all constants related to environment.
 * 
 * @author Dwaraka
 *
 */

public final class EnvironmentConstants {

	private EnvironmentConstants() {

	}

	public static final String AMQ_NAME = "neo.jms.bulkExport";
	public static final int REST_TIME_OUT = 180 * 1000; // 180 sec

	public static final String USER_INFO_SESSION_KEY = "tgoUserInfo";
	public static final String AMQ_BULK_IMPORT = "neo.jms.bulkImport.async";
}
