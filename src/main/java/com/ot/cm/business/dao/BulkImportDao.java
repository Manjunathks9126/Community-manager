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
 Feb 25, 2019      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.dao;

import com.ot.cm.jpa.entity.BulkImport;

/**
 * @author ssen
 *
 */
public interface BulkImportDao {

	BulkImport uploadBulkImportData(BulkImport importData);

	/**
	 * @param invData
	 */
	void updateBulkImportRequest(BulkImport importData);

	/**
	 * @param requestId
	 * @return BulkImport
	 */
	BulkImport getBulkImportData(String requestId);
}
