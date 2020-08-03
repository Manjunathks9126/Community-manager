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
 Aug 17, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.request.entity.RegistrationRequestVO;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.RARestResponseWrapper;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.OnboardingCustomFieldGroupType;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.rest.response.entity.OnboardingWorkflowTaskMap;
import com.ot.cm.rest.template.RequestPayload;


@Component
public class OnboardingTasksRestClient extends BaseRestClient {
	
	
	public RARestResponseWrapper<List<OnboardingWorkflowTaskMap>> getTaskList(Long workflowId, String locale)
			throws TGOCPBaseException {
		String endPoint = serviceURLResolver.RAREST("REGISTRATION_TASKS_LIST", "V1");
		HttpEntity<RegistrationRequestVO> requestEnitity = new HttpEntity<>(basicHeaders());

		RequestPayload<RARestResponseWrapper<List<OnboardingWorkflowTaskMap>>> payload = new RequestPayload<>(endPoint,
				HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<RARestResponseWrapper<List<OnboardingWorkflowTaskMap>>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, workflowId,locale);	
	}


	public RARestResponseWrapper<OnboardingTask> getTask(Long taskId, String locale)
			throws TGOCPBaseException {
		String endPoint = serviceURLResolver.RAREST("REGISTRATION_TASK", "V1");
		HttpEntity<RegistrationRequestVO> requestEnitity = new HttpEntity<>(basicHeaders());

		RequestPayload<RARestResponseWrapper<OnboardingTask>> payload = new RequestPayload<>(endPoint,
				HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<RARestResponseWrapper<OnboardingTask>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, taskId, locale);
	}


	public RARestResponseWrapper<List<String>> getCustomTask(Long taskId) throws TGOCPBaseException {
		String endPoint = serviceURLResolver.RAREST("CUSTOMTASK", "V1");
		HttpEntity<RegistrationRequestVO> requestEnitity = new HttpEntity<>(basicHeaders());

		RequestPayload<RARestResponseWrapper<List<String>>> payload = new RequestPayload<>(endPoint,
				HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<RARestResponseWrapper<List<String>>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, taskId);
	}


	/**
	 * Get all field groups under businessUnitId
	 * 
	 * @param businessUnitId
	 * @return
	 * @throws TGOBaseException
	 */
	public List<OnboardingCustomFieldGroupType> getAllFieldGroups(String businessUnitId) throws TGOCPBaseException {
		String endPoint = serviceURLResolver.CMD("ALL_FIELDS_GROUP", "V1");
		HttpEntity<String> requestEnitity = new HttpEntity<>(cmdAppSession());
		RequestPayload<List<OnboardingCustomFieldGroupType>> payload = new RequestPayload<>(endPoint, HttpMethod.GET,
				requestEnitity, new ParameterizedTypeReference<List<OnboardingCustomFieldGroupType>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, businessUnitId);
	}


	/**
	 * method to search custom fields
	 * 
	 * @param groupId
	 * @return
	 * @throws TGOBaseException
	 */
	public List<CustomFieldEntity> searchCustomFields(String fieldIds) throws TGOCPBaseException {
		String endPoint = serviceURLResolver.CMD("CUSTOMFIELDS", "V1");
		HttpEntity<String> requestEnitity = new HttpEntity<>(cmdAppSession());
		RequestPayload<List<CustomFieldEntity>> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<List<CustomFieldEntity>>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, fieldIds);
	}
	
	
	/**
	 * method to fetch custom fields of a particular custom field
	 * 
	 * @param groupId
	 * @return
	 * @throws TGOBaseException
	 */
	public CustomFieldEntity getCustomFieldsByFieldId(String fieldId) throws TGOCPBaseException {
		String endPoint = serviceURLResolver.CMD("CUSTOMFIELDSBYID", "V1");
		HttpEntity<String> requestEnitity = new HttpEntity<>(cmdAppSession());
		RequestPayload<CustomFieldEntity> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<CustomFieldEntity>() {
				});
		return restTemplate.exchange(payload, ErrorResponse.class, fieldId);
	}
	
}
