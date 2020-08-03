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
package com.ot.cm.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.ot.audit.vo.AuditInfo;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.messages.LocalizationMessages;
import com.ot.config.properties.GlobalProperties;

public class BaseRestController {

	@Autowired
	protected AuditInfo auditInfo;
	@Autowired
	protected ApplicationProperties appProperties;

	@Autowired
	protected GlobalProperties globalProperties;
	
	@Autowired
	LocalizationMessages localizationMessages;
}
