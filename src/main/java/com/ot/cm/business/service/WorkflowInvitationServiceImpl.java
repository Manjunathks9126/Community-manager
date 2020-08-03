package com.ot.cm.business.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.mail.InvitationMailClient;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.client.OnboardingRestClient;
import com.ot.cm.rest.request.entity.InvitationDataSentRequest;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.response.entity.InvitationExtendedRequestNeoRest;
import com.ot.cm.rest.response.entity.InvitationImage;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequest;
import com.ot.cm.rest.response.entity.RequestorTask;
import com.ot.cm.vo.UserInfo;

public class WorkflowInvitationServiceImpl implements WorkflowInvitationService {
	@Autowired
	private OnboardingRestClient neoRestClient;

	@Autowired
	private BusinessUntilRestClient businessUntilRestClient;

	@Autowired
	private InvitationMailClient invitationMailClient;

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
			for (InvitationExtendedRequestNeoRest extendedRequest : invitationResponse.getInvitationExtendedRequest()) {
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

	@Override
	public void saveInvitationImage(String invitationId, MultipartFile file)
			throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.saveInvitationImage(invitationId, file);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "Exception while inviting company   ",
					e);
		}

	}

	@Override
	public void updateInvitationImage(Long invitationId, MultipartFile file)
			throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.updateInvitationImage(invitationId, file);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
					"Exception while updating invitation image", e);
		}
	}

	@Override
	public void deleteInvitationImage(Long invitationId) throws TGOCPRestException, CMApplicationException {
		try {
			neoRestClient.deleteInvitationImage(invitationId);
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
					"Exception while deleting invitation image ", e);
		}

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

			saveRequestorTask(invitationWrapper, userInfo, invitationCode);
			sendInvitationMail(dataSentEntity.getEmailAddress(), invitationWrapper, invitationCode, senderAddress, img);

		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR, "Exception while inviting company", e);
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

	private void sendInvitationMail(String emailAddress, OnboardingInvitationWrapper invitationWrapper,
			String invitationCode, String senderAddress, File img) throws CMApplicationException {
		try {
			Map<String, String> mailPlaceHolderData = new HashMap<>();
			mailPlaceHolderData.put("company_name", invitationWrapper.getInvitationEntity().getInvitingCompanyName());
			mailPlaceHolderData.put("invitation_code", invitationCode);
			mailPlaceHolderData.put("inv_msg", invitationWrapper.getInvitationEntity().getEmailMessage());
			mailPlaceHolderData.put("serviceName", invitationWrapper.getServiceName());

			String subject = null;
			String content = null;
			if (invitationWrapper.getInvitationEntity().getEmailMessage() != null) {
				subject = extractElementsContent(invitationWrapper.getInvitationEntity().getEmailMessage(), "SUBJECT");
				content = extractElementsContent(invitationWrapper.getInvitationEntity().getEmailMessage(), "CONTENT")
						.replaceAll("\n", "<br>");

				invitationMailClient.sendMail(emailAddress, invitationWrapper.getInvitationEntity().getCcRecipients(),
						senderAddress, null, subject, mailPlaceHolderData, content, img);
			}
		} catch (Exception e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
					"Exception while sending invitation mail   ", e);
		}
	}

	private String extractElementsContent(String htmlText, String elementKey) {
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
}
