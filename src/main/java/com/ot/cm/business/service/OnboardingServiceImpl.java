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
 09/03/2018    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gxs.services.bsapi.rs.v2.entity.TradingAddressDetailsType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.BulkExportHelper;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.client.EDIAddressRestClient;
import com.ot.cm.rest.client.OnboardingRestClient;
import com.ot.cm.rest.request.entity.InvitationDataSentRequest;
import com.ot.cm.rest.request.entity.InvitedCompanyRequest;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.request.entity.ReinviteEntity;
import com.ot.cm.rest.request.entity.search.CustomTaskFieldGroupUpdate;
import com.ot.cm.rest.response.entity.EmailMessageResponse;
import com.ot.cm.rest.response.entity.InvitationDataSentResponse;
import com.ot.cm.rest.response.entity.InvitationExtendedRequestNeoRest;
import com.ot.cm.rest.response.entity.InvitationImage;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.OnboardingHistoryResponse;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.rest.response.entity.OnboardingTaskMap;
import com.ot.cm.rest.response.entity.OnboardingWorkflow;
import com.ot.cm.rest.response.entity.OnboardingWorkflowReport;
import com.ot.cm.rest.response.entity.OnboardingWorkflowReportRest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequestData;
import com.ot.cm.rest.response.entity.RequestorTask;
import com.ot.cm.tgms.entity.Workflow;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.UserInfo;
import com.ot.config.properties.GlobalProperties;
import com.ot.session.entity.DataActionsVO;
import com.ot.session.entity.NotificationRequestVO;

@Service
public class OnboardingServiceImpl implements OnboardingService {
	@Autowired
	private OnboardingRestClient neoRestClient;

	@Autowired
	private BusinessUntilRestClient businessUntilRestClient;

	@Autowired
	private BulkExportHelper bulkExportHelper;

	@Autowired
	private ApplicationProperties appProperties;

	@Autowired
	private RequestorTaskService taskRegistrationService;

	@Autowired
	GlobalProperties globalProperties;
	@Autowired
	EDIAddressRestClient ediRestClient;

	@Override
	public OnboardingHistoryResponse getOnboardingHistory(String hubBuId, String companyName, String companyCity,
			String companyCountry, String partnerCompanyId) throws TGOCPRestException, JAXBException {
		Workflow workflow = new Workflow();
		OnboardingHistoryResponse response = new OnboardingHistoryResponse();
		if (!StringUtils.isEmpty(companyName)) {

			response = neoRestClient.getOnboardingHistory(hubBuId, companyName, companyCity, companyCountry,
					partnerCompanyId);

			workflow = CommonUtil.XMLStringTojavaXObject(response.getProvisioningDetails().getRequestContent());
			response.getProvisioningDetails().setWorkflow(workflow);

		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ot.cm.service.OnboardingService#createInvitation(com.ot.cm.request.
	 * entity .InvitationCM, com.ot.cm.vo.UserInfo, java.lang.String)
	 */
	@Override
	public String createOrUpdateInvitation(InvitationNeoRest invitationResponse, UserInfo userInfo, String workflowId)
			throws TGOCPBaseException {
		if (invitationResponse.getInvitationId() == null) {
			BusinessUnitDetailsType businessType = businessUntilRestClient
					.getBusinessUnitDetailsType(userInfo.getCompanyId());
			invitationResponse.setCommunityId(userInfo.getCommunityID());
			invitationResponse.setCreatedTimestamp(new SimpleDateFormat(INVITATION_DATE_FORMAT).format(new Date()));
			invitationResponse.setCreatorCompanyId(userInfo.getCompanyId());
			invitationResponse.setCreatorCompanyName(businessType.getCompanyName());
			invitationResponse.setCreatorUserId(userInfo.getUserId());
			invitationResponse.setCreatorFirstName(userInfo.getFirstName());
			invitationResponse.setCreatorLastName(userInfo.getLastName());
			invitationResponse.setInvitationStatus(INVITATION_STATUS_SAVED);
			invitationResponse.setInviteeType(INVITEE_TYPE_COMPANY);
			invitationResponse.setInvitingCompanyId(userInfo.getCompanyId());
			invitationResponse.setInvitingCompanyName(businessType.getCompanyName());
			invitationResponse
					.setLastModifiedTimestamp(new SimpleDateFormat(INVITATION_DATE_FORMAT).format(new Date()));// check
		}

		List<InvitationExtendedRequestNeoRest> extendedRequestList = new ArrayList<>();
		if (null != invitationResponse.getInvitationExtendedRequest()
				&& !invitationResponse.getInvitationExtendedRequest().isEmpty()) {
			// Getting the mailBoxeNodeId from the very first selected edi's
			for (InvitationExtendedRequestNeoRest extendedRequest : invitationResponse.getInvitationExtendedRequest()) {
				if (ApplicationConstants.INVITING_COMPANY_EDI_ADDRESS_LIST
						.equals(extendedRequest.getInvitationAttrName())) {
					TradingAddressDetailsType[] edis = ediRestClient.getEDIAddresses(userInfo.getCompanyId(), "");
					String[] selectedEdis = extendedRequest.getInvitationAttrValue().split(",");
					for (TradingAddressDetailsType ediAddress : edis) {
						StringBuilder tradingAddress = new StringBuilder();
						tradingAddress
								.append(Optional.ofNullable(ediAddress.getTradingAddress().getQualifier()).orElse(""));
						tradingAddress.append(":");
						tradingAddress.append(ediAddress.getTradingAddress().getAddress());

						if (tradingAddress.toString().equalsIgnoreCase(selectedEdis[0])) {
							extendedRequestList.add(new InvitationExtendedRequestNeoRest(
									ApplicationConstants.INVITATION_SPOKE_NODE_ID_SELECTED, ediAddress.getNodeId()));
							break;
						}
					}
				}
				extendedRequestList.add(extendedRequest);
			}
		}
		invitationResponse.setInvitationExtendedRequest(extendedRequestList);
		String invitationId = null;
		if (invitationResponse.getInvitationId() != null) {
			invitationResponse
					.setCreatedTimestamp(new SimpleDateFormat(UPDATE_INVITATION_DATE_FORMAT).format(new Date()));
			invitationResponse
					.setLastModifiedTimestamp(new SimpleDateFormat(UPDATE_INVITATION_DATE_FORMAT).format(new Date()));// check

			invitationId = neoRestClient.updateInvitation(invitationResponse);
		} else {
			invitationId = neoRestClient.createInvitation(invitationResponse);
			neoRestClient.saveInvitationWorkflowLink(invitationId, workflowId);
		}

		// link invitationResponse to workflowFacade

		return invitationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ot.cm.service.OnboardingService#listWorkflowFacades(java.lang.String)
	 */
	@Override
	public List<OnboardingWorkflow> listWorkflows(String buId, boolean bulkOnly) throws TGOCPRestException {
		List<OnboardingWorkflow> workflowList = neoRestClient.listWorkflow(buId);
		return workflowList.stream().filter(workflow -> isValidOnboardingWorkflow(workflow, bulkOnly))
				.collect(Collectors.toList());
	}

	private boolean isValidOnboardingWorkflow(OnboardingWorkflow workflow, boolean bulkCheck) {
		if (!workflow.getStatus().equalsIgnoreCase(WORKFLOW_STATUS_ACTIVE)) {
			return false;
		}
		if (bulkCheck) {
			if (ObjectUtils.isEmpty(workflow.getOwfExtended())) {
				return false;
			} else
				return workflow.getOwfExtended().stream()
						.anyMatch(extendedData -> extendedData.getAttrName().equalsIgnoreCase(BULK_INVITE_ATTRIBUTE)
								&& extendedData.getAttrValue().equalsIgnoreCase("true"));
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ot.cm.service.OnboardingService#getInvitationList(java.lang.String)
	 */
	@Override
	public List<InvitationNeoRest> listInvitations(String workflowId, String[] invitationStatus)
			throws TGOCPRestException {
		InvitationNeoRest[] invitations = neoRestClient.listInvitations(workflowId, invitationStatus);
		List<InvitationNeoRest> invitationResponseList = new ArrayList<>();
		for (InvitationNeoRest invitation : invitations) {

			List<InvitationExtendedRequestNeoRest> extendedRequestList = new ArrayList<>();
			if (null != invitation.getInvitationExtendedRequest()
					&& !invitation.getInvitationExtendedRequest().isEmpty()) {
				for (InvitationExtendedRequestNeoRest extendedRequest : invitation.getInvitationExtendedRequest()) {
					extendedRequestList.add(extendedRequest);
				}
			}
			invitation.setInvitationExtendedRequest(extendedRequestList);
			invitationResponseList.add(invitation);
		}
		return invitationResponseList;
	}

	private void sendInvitationMail(String emailAddress, OnboardingInvitationWrapper invitationWrapper,
			String invitationCode, String senderAddress, File img) throws CMApplicationException {
		try {

			String subject = null;
			String content = null;
			if (invitationWrapper.getInvitationEntity().getEmailMessage() != null) {
				subject = extractElementsContent(invitationWrapper.getInvitationEntity().getEmailMessage(), "SUBJECT");
				content = extractElementsContent(invitationWrapper.getInvitationEntity().getEmailMessage(), "CONTENT")
						.replaceAll("\n", "<br>");

				sendNotificationRequest(emailAddress, invitationWrapper.getInvitationEntity().getCcRecipients(),
						senderAddress, invitationWrapper.getInvitationEntity().getInvitingCompanyName(),
						invitationWrapper.getServiceName(), invitationCode, content, subject);

			}
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN_MAIL_ERROR", e);
		}
	}

	private void sendNotificationRequest(String mailTo, String mailCc, String mailFrom, String companyName,
			String serviceName, String invitationCode, String invitationMsg, String subject) throws TGOCPRestException {
		Map<String, Object> contentsMap = new HashMap<>();
		contentsMap.put("MAILTO", mailTo);
		contentsMap.put("MAILCC", mailCc);
		contentsMap.put("MAILFROM", mailFrom);
		contentsMap.put("COMPANYNAME", companyName);
		contentsMap.put("SERVICENAME", serviceName);
		contentsMap.put("INVITATIONCODE", invitationCode);
		contentsMap.put("INVITATIONMSG", invitationMsg);
		contentsMap.put("SUBJECT", subject);
		contentsMap.put("REGISTRATIONURL", appProperties.getRegistrationAppURL() + "/company/" + invitationCode);
		contentsMap.put("HEADERIMAGE", globalProperties.getSignupURL() + "notifications/image/" + invitationCode);
		contentsMap.put("REGISTRATIONGRIDURL", appProperties.getRegistrationAppURL());

	NotificationRequestVO notificationRequestVO = new NotificationRequestVO();
		notificationRequestVO.setApplicationSource(ApplicationConstants.APPLICATION_SOURCE);
		notificationRequestVO.setContent(contentsMap);
		notificationRequestVO.setType(ApplicationConstants.NOTIFICATION_TYPE);
		notificationRequestVO.setLanguage(ApplicationConstants.NOTIFICATION_LANG_CODE);
		notificationRequestVO.setForwardingService(ApplicationConstants.FWD_SERVICE);
		notificationRequestVO.setServiceType(ApplicationConstants.SERVICE_TYPE);

		DataActionsVO dataActionsVO = new DataActionsVO();
		List<String> fieldsList = new ArrayList<>();
		fieldsList.add("COMPANYNAME");
		fieldsList.add("INVITATIONMSG");
		dataActionsVO.setContentType(ApplicationConstants.ESC_SPECIAL_CHARS);
		dataActionsVO.setContentFields(fieldsList);
		notificationRequestVO.setDataActionsVO(dataActionsVO);

		neoRestClient.sendNotifyRequest(notificationRequestVO);

	}

	public String extractElementsContent(String htmlText, String elementKey) {
		String elementContent = "";
		if (htmlText.contains(elementKey)) {
			String startTag = "<" + elementKey + ">";
			String endTag = "</" + elementKey + ">";
			final Pattern pattern = Pattern.compile(startTag + "(.*?)" + endTag, Pattern.DOTALL);
			final Matcher matcher = pattern.matcher(htmlText);
			if (matcher.find()) {
				elementContent = matcher.group(1);
			}
			// elementContent=elementContent.replaceAll("\n", "<br>");
		}
		return elementContent;
	}

	@Override
	public String invite(OnboardingInvitationWrapper invitationWrapper, UserInfo userInfo) throws TGOCPBaseException {

		InvitationDataSentRequest dataSentEntity = fromDataSentEntity(invitationWrapper);
		String senderAddress = "";
		for (InvitationExtendedRequestNeoRest extendedRequest : invitationWrapper.getInvitationEntity()
				.getInvitationExtendedRequest()) {
			if (extendedRequest.getInvitationAttrName().equalsIgnoreCase(SENDER_ADDR_ATTRIBUTE)) {
				senderAddress = extendedRequest.getInvitationAttrValue();
			}
		}
		String invitationCode = null;
		try {
			invitationCode = neoRestClient.invite(dataSentEntity);
			File img = getImage(invitationWrapper.getInvitationEntity().getInvitationId());
			/*
			 * = null; InvitationImage inviImage = null; try { inviImage =
			 * neoRestClient.getInvitationImage(invitationWrapper.
			 * getInvitationEntity().getInvitationId()); } catch (Exception e) {
			 * // if invitation image not available, send without invitation }
			 * if (null != inviImage && null != inviImage.getImageContent()) {
			 * img = new File("header.jpg"); FileUtils.writeByteArrayToFile(img,
			 * inviImage.getImageContent()); }
			 */
			saveRequestorTask(invitationWrapper, userInfo, invitationCode);
			sendInvitationMail(dataSentEntity.getEmailAddress(), invitationWrapper, invitationCode, senderAddress, img);

		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN_ERROR", e);
		}
		return invitationCode;
	}

	private void saveRequestorTask(OnboardingInvitationWrapper invitationWrapper, UserInfo userInfo,
			String invitationCode) throws TGOCPBaseException {
		OnboardingWorkflowRequest workflowRequest = new OnboardingWorkflowRequest();
		workflowRequest.setInvitationId(invitationWrapper.getInvitationEntity().getInvitationId());
		workflowRequest.setInvitationCode(invitationCode);
		workflowRequest.setStatus("new");
		workflowRequest.setCreatedBy(userInfo.getUserId());
		workflowRequest.setCreatedTimeStamp(new Date());
		workflowRequest.setWorkflowId(invitationWrapper.getWorkflowId());
		String requestId = neoRestClient.saveWorkflowRequest(workflowRequest);

		RequestorTask requestorTask = new RequestorTask();
		requestorTask.setRequestId(Long.parseLong(requestId));
		requestorTask.setOutputData(invitationWrapper.getRequestorTask());
		requestorTask.setStatus("new");
		requestorTask.setTaskId(invitationWrapper.getTaskId());
		requestorTask.setCreatedBy(userInfo.getUserId());
		requestorTask.setCreatedTimestamp(new Date());
		neoRestClient.saveRequestorTaskData(requestorTask);
	}

	private InvitationDataSentRequest fromDataSentEntity(OnboardingInvitationWrapper invitationWrapper) {
		InvitationDataSentRequest dataSentEntity = new InvitationDataSentRequest();
		dataSentEntity.setEmailAddress(invitationWrapper.getCompanyDetailsEntity().getContact().getEmail());
		dataSentEntity.setInvitationId(invitationWrapper.getInvitationEntity().getInvitationId());
		dataSentEntity.setCompanyIdentifier(invitationWrapper.getCompanyDetailsEntity().getCompanyName());
		dataSentEntity.setRecipientCompanyId(invitationWrapper.getCompanyDetailsEntity().getCompanyId());

		return dataSentEntity;
	}

	@Override
	public OnboardingWorkflow updateWorkflow(OnboardingWorkflow workflow, UserInfo userInfo) throws TGOCPRestException {
		workflow.setModifiedBy(userInfo.getUserId());
		workflow.setModifiedTimestamp(new Date());
		OnboardingWorkflow response = neoRestClient.updateOnbaordingWorkflow(workflow);
		return response;
	}

	@Override
	public void saveInvitationImage(String invitationId, MultipartFile file)
			throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.saveInvitationImage(invitationId, file);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN_ERROR", e);
		}
	}

	@Override
	public ListingQueryResponse<InvitedCompanyRequest> listInvitedPartners(String companyId, Long limit, Long after,
			boolean countOnly, String sortField, Integer sortOrder) throws TGOCPRestException {
		return neoRestClient.listInvitedPartners(companyId, limit, after, countOnly, sortField, sortOrder);
	}

	@Override
	public ListingQueryResponse<OnboardingWorkflowReport> getOnboardingWorkflowReport(
			OnboardingWorkflowListingFilterQuery request) throws TGOCPRestException, CMApplicationException {
		ListingQueryResponse<OnboardingWorkflowReportRest> response = null;

		response = neoRestClient.getOnboardingWorkflowReport(request, true);// only
																			// count
		if (response.getItemCount() > Integer.parseInt(appProperties.getOnboardingReportMaxQueryLimit())) {
			request.setAfter("0");
			request.setLimit(response.getItemCount().toString());
			JsonPropertyOrder jpo = OnboardingWorkflowReport.class.getAnnotation(JsonPropertyOrder.class);
			request.setExportColumns(jpo.value());
			bulkExportHelper.initiateOWRBulkExport(request);
		} else {
			response = neoRestClient.getOnboardingWorkflowReport(request, false);
		}

		return transform(response);
	}

	@Override
	public ListingQueryResponse<OnboardingWorkflowReport> getBulkOnboardingWorkflowReport(
			OnboardingWorkflowListingFilterQuery request) throws TGOCPRestException {
		ListingQueryResponse<OnboardingWorkflowReportRest> response = neoRestClient.getOnboardingWorkflowReport(request,
				false);

		return transform(response);
	}

	private ListingQueryResponse<OnboardingWorkflowReport> transform(
			ListingQueryResponse<OnboardingWorkflowReportRest> list) {
		ListingQueryResponse<OnboardingWorkflowReport> response = new ListingQueryResponse<>();

		response.setItemCount(list.getItemCount());

		if (null != list.getItemList() && !list.getItemList().isEmpty()) {
			response.setItemList(new ArrayList<>());

			list.getItemList().forEach(item -> {
				OnboardingWorkflowReport newObj = new OnboardingWorkflowReport();
				BeanUtils.copyProperties(item, newObj);
				response.getItemList().add(newObj);
			});
		}

		return response;
	}

	@Override
	public OnboardingWorkflow getWorkflow(String wfId) throws TGOCPRestException {
		return neoRestClient.getWorkflow(wfId);
	}

	@Override
	public String getInvitationCode(String invitationId, String emailId) throws TGOCPRestException {
		InvitationDataSentRequest dataSentEntity = new InvitationDataSentRequest();
		dataSentEntity.setInvitationId(Long.valueOf(invitationId));
		dataSentEntity.setEmailAddress(emailId);
		// IT
		// AFTER
		// TESTING
		String invitationCode = neoRestClient.invite(dataSentEntity);
		return invitationCode;
	}

	@Override
	public String getInvitationCode(String invitationId, String companyIdentifier, String recipientCompanyId,
			String emailId) throws TGOCPRestException {
		InvitationDataSentRequest dataSentEntity = new InvitationDataSentRequest();
		dataSentEntity.setInvitationId(Long.valueOf(invitationId));
		dataSentEntity.setEmailAddress(emailId);
		dataSentEntity.setCompanyIdentifier(companyIdentifier);
		dataSentEntity.setRecipientCompanyId(recipientCompanyId);

		String invitationCode = neoRestClient.invite(dataSentEntity);
		return invitationCode;
	}

	@Override
	public InvitationNeoRest getInvitationData(Long invitationId) throws TGOCPRestException, IOException {
		EmailMessageResponse emailResponse = new EmailMessageResponse();
		InvitationNeoRest invitations = neoRestClient.getInvitation(invitationId);
		if (invitations.getEmailMessage() != null) {
			String emailSubject = extractElementsContent(invitations.getEmailMessage(), "SUBJECT");
			String emailContent = extractElementsContent(invitations.getEmailMessage(), "CONTENT");
			emailResponse.setContent(emailContent);
			emailResponse.setSubject(emailSubject);
			invitations.setInvitationMessage(emailResponse);
		}
		List<InvitationExtendedRequestNeoRest> extendedRequestList = new ArrayList<>();
		if (null != invitations.getInvitationExtendedRequest()
				&& !invitations.getInvitationExtendedRequest().isEmpty()) {
			for (InvitationExtendedRequestNeoRest extendedRequest : invitations.getInvitationExtendedRequest()) {
				extendedRequestList.add(extendedRequest);
			}
		}
		invitations.setInvitationExtendedRequest(extendedRequestList);
		try {
			InvitationImage invImage = neoRestClient.getInvitationImage(invitationId);
			if (invImage != null) {
				invitations.setImageContent(invImage.getImageContent());
				invitations.setImageName(invImage.getImageName());
			}
		} catch (Exception e) {
			// logger
		}
		return invitations;
	}

	@Override
	public void updateInvitationImage(Long invitationId, MultipartFile file)
			throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.updateInvitationImage(invitationId, file);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN__IMAGE_UPDATE_ERROR",
					e);
		}
	}

	@Override
	public void deleteInvitationImage(Long invitationId) throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.deleteInvitationImage(invitationId);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN__IMAGE_DELETE_ERROR",
					e);
		}
	}

	@Override
	public List<OnboardingTaskMap> getOnboardingTasks(Long workflowId, String taskStage) throws TGOCPRestException {
		// TODO Auto-generated method stub

		List<OnboardingTaskMap> tasks = neoRestClient.listTasks(workflowId);

		if (!StringUtils.isEmpty(taskStage)) {
			tasks = tasks.stream().filter(task -> taskStage.equals(task.getTask().getTaskStage()))
					.collect(Collectors.toList());

			if (tasks.isEmpty()) {
				throw new TGOCPRestException(HttpStatus.NOT_FOUND, "TGOCPREST-404", "TASKS_SEARCH_404",
						ApplicationConstants.REQUESTER_TASK_STAGE + " Task(s) Not Available", null);
			}
		}

		return tasks;

	}

	@Override
	public String getOnboardingRequesterTaskSchema(@NonNull Long workflowId, Locale locale) throws TGOCPRestException {
		Map<String, Object> taskJsonMap = getRequesterTaskSchemaMap(workflowId, locale);
		return (String) taskJsonMap.get("jsonSchema");

	}

	@Override
	public Map<String, Object> getRequesterTaskSchemaMap(@NonNull Long workflowId, Locale locale)
			throws TGOCPRestException {
		OnboardingTaskMap taskMap = getSingleMatchedOnboardingTask(workflowId,
				ApplicationConstants.REQUESTER_TASK_STAGE);
		try {
			OnboardingTask task = taskRegistrationService.getTask(taskMap.getTask().getTaskId(), locale.getLanguage());
			Map<String, Object> taskJsonMap = new HashMap<>(2);
			taskJsonMap.put("taskId", taskMap.getTask().getTaskId());
			taskJsonMap.put("jsonSchema", task.getJsonSchema());
			return taskJsonMap;
		} catch (TGOCPBaseException e) {
			throw new TGOCPRestException(HttpStatus.NOT_FOUND, "TGOCPREST-404", "TASKS_SCHEMA_404",
					"Task Schema Not Available", null);
		}
	}

	/**
	 * Like RequesterTask etc which will be only one
	 * 
	 * @param workflowId
	 * @param taskStage
	 * @return OnboardingTaskMap
	 * @throws TGOCPRestException
	 * 
	 */
	@Override
	public OnboardingTaskMap getSingleMatchedOnboardingTask(@NonNull Long workflowId, @NonNull String taskStage)
			throws TGOCPRestException {

		List<OnboardingTaskMap> tasks = neoRestClient.listTasks(workflowId);

		return tasks.stream().filter(task -> taskStage.equals(task.getTask().getTaskStage())).findFirst()
				.orElseThrow(() -> new TGOCPRestException(HttpStatus.NOT_FOUND, "TGOCPREST-404", "TASKS_SEARCH_404",
						" Task(s) Not Available", null));
	}

	@Override
	public List<String> getFieldGroups(Long taskId) throws TGOCPRestException {
		// TODO Auto-generated method stub
		return neoRestClient.listTfieldGroup(taskId);
	}

	@Override
	public String updateCustomTaskFieldGroups(Long taskId, CustomTaskFieldGroupUpdate cutomTaskFieldGoup)
			throws TGOCPRestException {
		// TODO Auto-generated method stub

		return neoRestClient.updateFieldGroups(taskId, cutomTaskFieldGoup);
	}

	@Override
	public List<Long> getCustomTaskForFGId(String fgId) throws TGOCPRestException {
		// TODO Auto-generated method stub
		return neoRestClient.getCustomTaskForFGId(fgId);
	}

	@Override
	public List<OnboardingTaskMap> createOrUpdateTask(Long workflowId, OnboardingTaskMap onboTaskMap, UserInfo userInfo)
			throws TGOCPRestException, IOException {
		onboTaskMap.setModifiedBy(userInfo.getUserId());
		List<OnboardingTaskMap> onboWorkflowTaskList = new ArrayList<OnboardingTaskMap>();
		onboWorkflowTaskList.add(onboTaskMap);
		return neoRestClient.saveWorkflowBasedTask(onboWorkflowTaskList, workflowId);

	}

	@Override
	public void delete(Long[] ids, Long workflowId, UserInfo userInfo)
			throws TGOCPRestException, CMApplicationException {
		List<Long> workflowTaskIds = new ArrayList<Long>(Arrays.asList(ids));
		try {
			neoRestClient.deleteTask(workflowTaskIds, workflowId);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "ONBOARDING_INVTN__IMAGE_DELETE_ERROR",
					e);
		}

	}

	@Override
	public OnboardingWorkflowRequestData getRequesterTaskdata(Long requestId) throws TGOCPBaseException {
		// TODO Auto-generated method stub
		return neoRestClient.getRequestorTaskData(requestId).get(0);
	}

	@Override
	public void deleteSentInvitation(String invitationCode) throws TGOCPRestException {
		// TODO Auto-generated method stub
		neoRestClient.deleteSentInvitation(invitationCode);
	}

	@Override
	public void resendInvitation(List<ReinviteEntity> reInvitationList) throws TGOCPRestException {
		// TODO Auto-generated method stub

		for (ReinviteEntity entity : reInvitationList) {
			CompletableFuture.runAsync(() -> {
				String invitationCode = entity.getInvitationCode();

				OnboardingInvitationWrapper invWrapper = new OnboardingInvitationWrapper();

				invWrapper.setServiceName(entity.getServiceName());
				// invWrapper.setTaskId(entity.getTaskId());
				invWrapper.setWorkflowId(entity.getWorkflowId());

				InvitationDataSentResponse invitationDetails = new InvitationDataSentResponse();
				try {
					invitationDetails = neoRestClient.getInvitedTPDetails(invitationCode);
					invWrapper.setInvitationEntity(invitationDetails.getInvitation());
				} catch (TGOCPRestException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String senderAddress = getSenderAddress(invWrapper);
				File img = getImage(invWrapper.getInvitationEntity().getInvitationId());

				try {
					sendInvitationMail(invitationDetails.getEmailAddress(), invWrapper, invitationCode, senderAddress,
							img);
				} catch (CMApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}

	}

	public String getSenderAddress(OnboardingInvitationWrapper invWrapper) {
		String senderAddress = "";
		for (InvitationExtendedRequestNeoRest extendedRequest : invWrapper.getInvitationEntity()
				.getInvitationExtendedRequest()) {
			if (extendedRequest.getInvitationAttrName().equalsIgnoreCase(SENDER_ADDR_ATTRIBUTE)) {
				senderAddress = extendedRequest.getInvitationAttrValue();
			}
		}
		return senderAddress;
	}

	public File getImage(Long invitationId) {
		File img = null;
		InvitationImage inviImage = null;

		try {
			inviImage = neoRestClient.getInvitationImage(invitationId);
		} catch (Exception e) {
			// if invitation image not available, send without invitation
		}
		if (null != inviImage && null != inviImage.getImageContent()) {
			img = new File("header.jpg");
			try {
				FileUtils.writeByteArrayToFile(img, inviImage.getImageContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return img;
	}

	@Override
	public String resendInvitationWithNewinvCode(ReinviteEntity reInvitationEntity, UserInfo userInfo)
			throws TGOCPBaseException {
		// TODO Auto-generated method stub

		OnboardingInvitationWrapper invEntity = new OnboardingInvitationWrapper();
		invEntity.setCompanyDetailsEntity(reInvitationEntity.getCompanyDetailsEntity());
		invEntity.setRequestorTask(reInvitationEntity.getRequestorTask());
		invEntity.setWorkflowId(reInvitationEntity.getWorkflowId());
		invEntity.setTaskId(reInvitationEntity.getTaskId());
		invEntity.setServiceName(reInvitationEntity.getServiceName());
		// get Invitation Entity
		InvitationDataSentResponse invitationDetails = neoRestClient
				.getInvitedTPDetails(reInvitationEntity.getInvitationCode());
		invEntity.setInvitationEntity(invitationDetails.getInvitation());

		// delete old sent invitation related details e.g code,requester data
		deleteSentInvitation(reInvitationEntity.getInvitationCode());

		// create new Invitation code,save requester data and send mail
		String newInvitationCode = invite(invEntity, userInfo);

		return newInvitationCode;
	}

}
