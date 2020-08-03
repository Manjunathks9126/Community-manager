package com.ot.cm.business.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.InvitedCompanyRequest;
import com.ot.cm.rest.request.entity.OnboardingInvitationWrapper;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.request.entity.ReinviteEntity;
import com.ot.cm.rest.request.entity.search.CustomTaskFieldGroupUpdate;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.OnboardingHistoryResponse;
import com.ot.cm.rest.response.entity.OnboardingTaskMap;
import com.ot.cm.rest.response.entity.OnboardingWorkflow;
import com.ot.cm.rest.response.entity.OnboardingWorkflowReport;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequestData;
import com.ot.cm.vo.UserInfo;

public interface OnboardingService {

	String INVITATION_STATUS_SAVED = "SAVED";
	String INVITATION_STATUS_ACTIVE = "ACTIVE";
	String INVITEE_TYPE_COMPANY = "COMPANY";
	String INVITATION_DATE_FORMAT = "MM/dd/yyyy";
	String SENDER_ADDR_ATTRIBUTE = "LAST_SENT_FROM";
	String WORKFLOW_STATUS_ACTIVE = "ACTIVE";
	String UPDATE_INVITATION_DATE_FORMAT = "yyyy-MM-dd";
	String BULK_INVITE_ATTRIBUTE = "INVITE_BULK_TP";

	/**
	 * 
	 * @param companyName
	 * @param companyCity
	 * @param companyCountry
	 * @return
	 * @throws TGOCPRestException
	 * @throws JAXBException
	 */
	OnboardingHistoryResponse getOnboardingHistory(String hubBuId, String companyName, String companyCity,
			String companyCountry, String partnerCompanyId) throws TGOCPRestException, JAXBException;

	String createOrUpdateInvitation(InvitationNeoRest invitationResponse, UserInfo userInfo, String workflowId)
			throws TGOCPRestException, CMApplicationException, TGOCPBaseException;

	List<OnboardingWorkflow> listWorkflows(String buId, boolean bulkOnly) throws TGOCPRestException;

	OnboardingWorkflow getWorkflow(String wfId) throws TGOCPRestException;

	List<InvitationNeoRest> listInvitations(String workflowId, String[] invitationStatus) throws TGOCPRestException;

	InvitationNeoRest getInvitationData(Long invitationId) throws TGOCPRestException, IOException;

	String invite(OnboardingInvitationWrapper invitationWrapper, UserInfo userInfo) throws TGOCPBaseException;

	OnboardingWorkflow updateWorkflow(OnboardingWorkflow workflow, UserInfo userInfo) throws TGOCPRestException;

	void saveInvitationImage(String inviatationId, MultipartFile file)
			throws TGOCPRestException, CMApplicationException;

	ListingQueryResponse<InvitedCompanyRequest> listInvitedPartners(String companyId, Long limit, Long after,
			boolean countOnly, String sortField, Integer sortOrder) throws TGOCPRestException;

	ListingQueryResponse<OnboardingWorkflowReport> getOnboardingWorkflowReport(
			OnboardingWorkflowListingFilterQuery filterObject) throws TGOCPRestException, CMApplicationException;

	ListingQueryResponse<OnboardingWorkflowReport> getBulkOnboardingWorkflowReport(
			OnboardingWorkflowListingFilterQuery filterObject) throws TGOCPRestException;

	String getInvitationCode(String invitationId, String emailId) throws TGOCPRestException;

	void updateInvitationImage(Long invitationId, MultipartFile file) throws TGOCPRestException, CMApplicationException;

	void deleteInvitationImage(Long invitationId) throws TGOCPRestException, CMApplicationException;

	List<OnboardingTaskMap> getOnboardingTasks(Long workflowId, String taskStage) throws TGOCPRestException;

	List<String> getFieldGroups(Long taskId) throws TGOCPRestException;

	String updateCustomTaskFieldGroups(Long taskId, CustomTaskFieldGroupUpdate cutomTaskFieldGoup)
			throws TGOCPRestException;

	List<Long> getCustomTaskForFGId(String fgId) throws TGOCPRestException;

	public List<OnboardingTaskMap> createOrUpdateTask(Long workflowId, OnboardingTaskMap onbotask, UserInfo userInfo)
			throws TGOCPRestException, IOException;

	public void delete(Long[] ids, Long workflowId, UserInfo userInfo)
			throws TGOCPRestException, CMApplicationException;

	OnboardingWorkflowRequestData getRequesterTaskdata(Long requestId) throws TGOCPBaseException;

	void deleteSentInvitation(String invitationCode) throws TGOCPRestException;

	void resendInvitation(List<ReinviteEntity> reInvitationEntities) throws TGOCPRestException, IOException;

	String resendInvitationWithNewinvCode(ReinviteEntity reInvitationEntities, UserInfo userInfo)
			throws TGOCPBaseException;

	/**
	 * @param workflowId
	 * @param taskStage
	 * @return OnboardingTaskMap
	 * @throws TGOCPRestException
	 * 
	 */
	OnboardingTaskMap getSingleMatchedOnboardingTask(@NonNull Long workflowId, @NonNull String taskStage)
			throws TGOCPRestException;

	/**
	 * @param workflowId
	 * @param locale
	 * @return OnboardingTaskMap
	 * @throws TGOCPRestException
	 */
	String getOnboardingRequesterTaskSchema(Long workflowId, Locale locale) throws TGOCPRestException;

	/**
	 * @param workflowId
	 * @param locale
	 * @return Map<String,Object>
	 * @throws TGOCPRestException
	 * 
	 */
	Map<String, Object> getRequesterTaskSchemaMap(Long workflowId, Locale locale) throws TGOCPRestException;

	String getInvitationCode(String invitationId, String companyIdentifier, String recipientCompanyId, String emailId)
			throws TGOCPRestException;

}
