package com.ot.cm.business.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.vo.UserInfo;

public interface WorkflowInvitationService {

	String UPDATE_INVITATION_DATE_FORMAT = "yyyy-MM-dd";
	String INVITATION_DATE_FORMAT = "MM/dd/yyyy";
	String INVITATION_STATUS_SAVED = "SAVED";
	String INVITEE_TYPE_COMPANY = "COMPANY";
	String SENDER_ADDR_ATTRIBUTE = "LAST_SENT_FROM";

	List<InvitationNeoRest> listInvitations(String workflowId, String[] invitationStatus) throws TGOCPRestException;

	String createOrUpdateInvitation(InvitationNeoRest invitationResponse, UserInfo userInfo, String workflowId)
			throws TGOCPRestException, CMApplicationException, TGOCPBaseException;

	void saveInvitationImage(String invitationId, MultipartFile file) throws TGOCPRestException, CMApplicationException;

	void updateInvitationImage(Long invitationId, MultipartFile file) throws TGOCPRestException, CMApplicationException;

	void deleteInvitationImage(Long invitationId) throws TGOCPRestException, CMApplicationException;

	String invite(OnboardingInvitationWrapper invitationWrapper, UserInfo userInfo) throws TGOCPBaseException;

}
