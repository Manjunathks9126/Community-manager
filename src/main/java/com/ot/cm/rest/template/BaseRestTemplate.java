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
package com.ot.cm.rest.template;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.ot.cm.constants.RestActionContext;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.TGOCPRestResponse;

public interface BaseRestTemplate {
	public <T1, T2> TGOCPRestResponse<T1, T2> exchange(String endpoint, HttpMethod httpMethod,
			HttpEntity<?> requestEntity, Class<T1> responseType, Object... uriVariables) throws TGOCPRestException;

	public <T1, T2> TGOCPRestResponse<T1, T2> exchange(String endpoint, HttpMethod httpMethod,
			HttpEntity<?> requestEntity, Class<T1> responseType, RestActionContext actionContext, String[] msgParams,
			Object... uriVariables) throws TGOCPRestException;

	public <T, T2> T exchange(RequestPayload<T> payload, T2 errorEntity, Object... uriVariables)
			throws TGOCPRestException;
}
