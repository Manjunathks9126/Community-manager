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
 Aug 23, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.io.File;
import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.CMDSuccessResponse;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.vo.UserInfo;

/**
 * Service class to handle custom fields opetations
 *
 */
public interface CustomFieldsService {

	/**
	 * @param groupId
	 * @param userInfo
	 * @param sortBy 
	 * @return CustomFieldEntity []
	 * @throws TGOCPRestException
	 */
	CustomFieldEntity[] list(String groupId, UserInfo userInfo,String[] type, String[] sortBy) throws TGOCPRestException;

	/**
	 * @param customFieldEntity
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse create(CustomFieldEntity customFieldEntity, UserInfo userInfo) throws TGOCPRestException;

	/**
	 * @param ids
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse delete(String[] ids, UserInfo userInfo) throws TGOCPRestException;

	/**
	 * @param customFieldEntity
	 * @param userInfo
	 * @return CMDSuccessResponse
	 * @throws TGOCPRestException
	 */
	CMDSuccessResponse edit(CustomFieldEntity customFieldEntity, UserInfo userInfo) throws TGOCPRestException;
	
	
	
	/**
	 * @param userInfo
	 * @param type
	 * @param sortBy
	 * @return
	 * @throws TGOCPRestException
	 */
	CustomFieldEntity[] fieldlistForGroupDependency(String groupId,UserInfo userInfo,String[] type, String[] sortBy) throws TGOCPRestException;
	
	
	/**
	 * @param groupId
	 * @param userInfo
	 * @param type
	 * @param sortBy
	 * @return
	 * @throws TGOCPRestException
	 * @throws CMApplicationException 
	 */
	
	CustomFieldEntity[] listForFieldDependency(String fieldId,String groupId,UserInfo userInfo,String[] type, String[] sortBy) throws TGOCPRestException, CMApplicationException;
	byte[] download(String fileId,String fileName, UserInfo userInfo) throws TGOCPRestException, IOException;
}
