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
package com.ot.cm.filter.session;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.ot.session.filter.TGOCPSessionBuilderFilter;

/**
 * The {@code EnvironmentConfigurationManager} class to check if session exist
 * or not for each rquest.
 * 
 * This class extends SDK Session Builder to build the
 * session which is common logic acrros different applications
 * 
 * @author Dwaraka
 */
public class CommunityMgrSessionBuilder extends TGOCPSessionBuilderFilter {
		@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}

}
