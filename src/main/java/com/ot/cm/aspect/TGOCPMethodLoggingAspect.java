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
package com.ot.cm.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect to log statements to log file during different phased of execution
 * like method level,class level ..etc .
 * 
 * @author Dwaraka
 */
@Aspect
@Component
public class TGOCPMethodLoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(TGOCPMethodLoggingAspect.class);
	
	@Around("execution(* *(..)) && @annotation(com.ot.session.annotation.Loggable)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		logger.debug("{} : {}", joinPoint.getSignature().getName() , " Enter");

		Object result = joinPoint.proceed(joinPoint.getArgs()); // continue on
																// the
																// intercepted
																// method

		logger.debug(joinPoint.getSignature().getName(), " Exit");

		return result;
	}
}