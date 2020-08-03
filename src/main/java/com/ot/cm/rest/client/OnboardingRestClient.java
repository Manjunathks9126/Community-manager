package com.ot.cm.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.InvitationDataSentRequest;
import com.ot.cm.rest.request.entity.InvitedCompanyRequest;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.request.entity.search.CustomTaskFieldGroupUpdate;
import com.ot.cm.rest.response.NeoRestErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.entity.InvitationDataSentResponse;
import com.ot.cm.rest.response.entity.InvitationImage;
import com.ot.cm.rest.response.entity.InvitationNeoRest;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.rest.response.entity.NeoRestResponseWrapper;
import com.ot.cm.rest.response.entity.OnboardingHistoryResponse;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.rest.response.entity.OnboardingTaskMap;
import com.ot.cm.rest.response.entity.OnboardingWorkflow;
import com.ot.cm.rest.response.entity.OnboardingWorkflowReportRest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequest;
import com.ot.cm.rest.response.entity.OnboardingWorkflowRequestData;
import com.ot.cm.rest.response.entity.RequestorTask;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.util.CommonUtil;
import com.ot.session.annotation.Loggable;
import com.ot.session.entity.NotificationRequestVO;

@Component
public class OnboardingRestClient extends BaseRestClient {

	@Loggable
	public OnboardingHistoryResponse getOnboardingHistory(String hubBuId, String companyName, String companyCity,
			String companyCountry, String partnerCompanyId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboarding/history");
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("hubBuId", hubBuId);
		queryParams.put("companyName", companyName);
		queryParams.put("companyCity", companyCity);
		queryParams.put("companyCountry", companyCountry);
		if (!CommonUtil.isEmpty(partnerCompanyId))
			queryParams.put("companyId", partnerCompanyId);

		RequestPayload<NeoRestResponseWrapper<OnboardingHistoryResponse>> payload = new RequestPayload<>(
				endPoint.toString(), queryParams, HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<OnboardingHistoryResponse>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public String createInvitation(InvitationNeoRest invitationResponse) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/invitations");

		HttpEntity<InvitationNeoRest> requestEntity = new HttpEntity<InvitationNeoRest>(invitationResponse,
				basicHeaders());

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public List<OnboardingWorkflow> listWorkflow(String buId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows?");
		endPoint.append("ownerBuId=");
		endPoint.append(buId);

		RequestPayload<NeoRestResponseWrapper<List<OnboardingWorkflow>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<OnboardingWorkflow>>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public void saveInvitationWorkflowLink(String invitationId, String workflowId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/invWorkflowMaps");
		Map<String, String> invWorkflowMap = new HashMap<String, String>();
		invWorkflowMap.put("invitationId", invitationId);
		invWorkflowMap.put("workflowId", workflowId);
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(invWorkflowMap,
				basicHeaders());
		RequestPayload<NeoRestResponseWrapper<Map<String, String>>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<Map<String, String>>>() {
				});
		restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public InvitationNeoRest[] listInvitations(String workflowId, String[] invitationStatus) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflow/" + workflowId + "/invitations");

		if (null != invitationStatus && invitationStatus.length > 0) {
			endPoint.append("?invitationStatus=").append(String.join(",", invitationStatus));
		}

		RequestPayload<NeoRestResponseWrapper<InvitationNeoRest[]>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<InvitationNeoRest[]>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public String invite(InvitationDataSentRequest dataSentEntity) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/generateInvitationCode");

		HttpEntity<InvitationDataSentRequest> requestEntity = new HttpEntity<InvitationDataSentRequest>(dataSentEntity,
				basicHeaders());

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public OnboardingWorkflow updateOnbaordingWorkflow(OnboardingWorkflow workflow) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows/update/");
		endPoint.append(workflow.getWorkflowId());

		HttpEntity<OnboardingWorkflow> requestEntity = new HttpEntity<OnboardingWorkflow>(workflow, basicHeaders());

		RequestPayload<NeoRestResponseWrapper<OnboardingWorkflow>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<OnboardingWorkflow>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public String saveInvitationImage(String invitationId, MultipartFile file) throws TGOCPRestException, IOException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/invitations/image/");
		endPoint.append(invitationId);

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public InvitationImage getInvitationImage(Long invitationId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/invitations/image/");
		endPoint.append(invitationId);

		RequestPayload<NeoRestResponseWrapper<InvitationImage>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<InvitationImage>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public ListingQueryResponse<InvitedCompanyRequest> listInvitedPartners(String companyId, Long limit, Long after,
			boolean countOnly, String sortField, Integer sortOrder) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/invitations/invited/");
		endPoint.append(companyId);
		if (countOnly)
			endPoint.append("?countOnly=true");
		else {
			if (null != after)
				endPoint.append("?after=" + after);
			if (null != limit)
				endPoint.append("&limit=" + limit);
		}

		if (!CommonUtil.isEmpty(sortField)) {
			if (sortOrder.equals(1)) {
				endPoint.append("&sortBy=desc_" + sortField);
			} else {
				endPoint.append("&sortBy=" + sortField);
			}
		}
		RequestPayload<NeoRestResponseWrapper<ListingQueryResponse<InvitedCompanyRequest>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<ListingQueryResponse<InvitedCompanyRequest>>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public OnboardingWorkflow getWorkflow(String wfId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows/");
		endPoint.append(wfId);

		RequestPayload<NeoRestResponseWrapper<OnboardingWorkflow>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<OnboardingWorkflow>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public ListingQueryResponse<OnboardingWorkflowReportRest> getOnboardingWorkflowReport(
			OnboardingWorkflowListingFilterQuery filterObject, boolean isCountOnly) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows/report/");
		endPoint.append(filterObject.getUniqueId());
		endPoint.append("?ownerBuId=");
		endPoint.append(filterObject.getCompanyId());
		if (isCountOnly)
			endPoint.append("&countOnly=true");
		else {
			if (null != filterObject.getAfter())
				endPoint.append("&after=" + filterObject.getAfter());
			if (null != filterObject.getLimit())
				endPoint.append("&limit=" + filterObject.getLimit());
		}

		RequestPayload<NeoRestResponseWrapper<ListingQueryResponse<OnboardingWorkflowReportRest>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<ListingQueryResponse<OnboardingWorkflowReportRest>>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	// private class to make multipart rest request
	private class MultipartInputStreamFileResource extends InputStreamResource {

		private final String filename;

		MultipartInputStreamFileResource(InputStream inputStream, String filename) {
			super(inputStream);
			this.filename = filename;
		}

		@Override
		public String getFilename() {
			return this.filename;
		}

		@Override
		public long contentLength() throws IOException {
			return -1; // we do not want to generally read the whole stream into
						// memory ...
		}
	}

	public InvitationNeoRest getInvitation(Long invitationId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/workflowInvitations/");
		endPoint.append(invitationId);

		RequestPayload<NeoRestResponseWrapper<InvitationNeoRest>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<InvitationNeoRest>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public InvitationDataSentResponse getInvitedTPDetails(String invitationCode) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/workflowInvitations/invitedTp/");
		endPoint.append(invitationCode);

		RequestPayload<NeoRestResponseWrapper<InvitationDataSentResponse>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<InvitationDataSentResponse>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public String updateInvitation(InvitationNeoRest invitation) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/workflowInvitations/");
		endPoint.append(invitation.getInvitationId());
		HttpEntity<InvitationNeoRest> requestEntity = new HttpEntity<InvitationNeoRest>(invitation, basicHeaders());
		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public String updateInvitationImage(Long invitationId, MultipartFile file) throws TGOCPRestException, IOException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/workflowInvitations/image/");
		endPoint.append(invitationId);

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public String deleteInvitationImage(Long invitationId) throws TGOCPRestException, IOException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/workflowInvitations/image/");
		endPoint.append(invitationId);
		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.DELETE, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public List<OnboardingTaskMap> listTasks(Long workflowId) throws TGOCPRestException {

		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows/" + workflowId + "/tasks");
		RequestPayload<NeoRestResponseWrapper<List<OnboardingTaskMap>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<OnboardingTaskMap>>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public List<String> listTfieldGroup(Long taskId) throws TGOCPRestException {

		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingTasks/" + taskId + "/fieldGroups");
		RequestPayload<NeoRestResponseWrapper<List<String>>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<String>>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	public String updateFieldGroups(Long taskId, CustomTaskFieldGroupUpdate cutomTaskFieldGoup)
			throws TGOCPRestException {
		// TODO Auto-generated method stub
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingTasks/" + taskId + "/fieldGroups");

		HttpEntity<CustomTaskFieldGroupUpdate> requestEntity = new HttpEntity<CustomTaskFieldGroupUpdate>(
				cutomTaskFieldGoup, basicHeaders());

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();
	}

	/**
	 * method to get task details by using task ID
	 * 
	 * @param taskId
	 * @param locale
	 * @return
	 * @throws TGOBaseException
	 */
	public NeoRestResponseWrapper<OnboardingTask> getTask(Long taskId, String locale) throws TGOCPBaseException {
		String endPoint = globalProperties.getRest().getRaURL() + "/v1/onboardingTasks/{taskId}?locale={locale}";
		HttpEntity<OnboardingTask> requestEnitity = new HttpEntity<>(basicHeaders());

		RequestPayload<NeoRestResponseWrapper<OnboardingTask>> payload = new RequestPayload<>(endPoint, HttpMethod.GET,
				requestEnitity, new ParameterizedTypeReference<NeoRestResponseWrapper<OnboardingTask>>() {
				});
		return restTemplate.exchange(payload, NeoRestResponseWrapper.class, taskId, locale);
	}

	/**
	 * method to save onboarding workflow request
	 * 
	 * @param workflowRequest
	 * @return
	 * @throws TGOCPBaseException
	 */
	public String saveWorkflowRequest(OnboardingWorkflowRequest workflowRequest) throws TGOCPBaseException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingRequests");

		HttpEntity<OnboardingWorkflowRequest> requestEntity = new HttpEntity<OnboardingWorkflowRequest>(workflowRequest,
				basicHeaders());

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getTargetResourceRefId();
	}

	public String saveRequestorTaskData(RequestorTask requestorTask) throws TGOCPBaseException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingRequests/{requestId}/data");

		HttpEntity<RequestorTask> requestEntity = new HttpEntity<RequestorTask>(requestorTask, basicHeaders());

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class, requestorTask.getRequestId())
				.getResponseDetails().getResponseEntity();
	}

	public List<OnboardingWorkflowRequestData> getRequestorTaskData(Long requestId) throws TGOCPBaseException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingRequests/{requestId}/data");

		HttpEntity<List<OnboardingWorkflowRequestData>> requestEnitity = new HttpEntity<>(basicHeaders());

		RequestPayload<NeoRestResponseWrapper<List<OnboardingWorkflowRequestData>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<OnboardingWorkflowRequestData>>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class, requestId).getResponseDetails()
				.getResponseEntity();
	}

	public List<Long> getCustomTaskForFGId(String fgId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingTasks/customTasks/" + fgId);

		RequestPayload<NeoRestResponseWrapper<List<Long>>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.GET, new HttpEntity<String>("headers", basicHeaders()),
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<Long>>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public List<OnboardingTaskMap> saveWorkflowBasedTask(List<OnboardingTaskMap> onboTask, Long workflowId)
			throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		endPoint.append("/v1/onboardingWorkflows/");
		endPoint.append(workflowId);
		endPoint.append("/tasks");
		HttpEntity<List<OnboardingTaskMap>> requestEntity = new HttpEntity<>(onboTask, basicHeaders());
		RequestPayload<NeoRestResponseWrapper<List<OnboardingTaskMap>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<OnboardingTaskMap>>>() {
				});

		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public List<OnboardingTaskMap> deleteTask(List<Long> workflowTaskIds, Long workflowId) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		HttpEntity<List<Long>> requestType = new HttpEntity<>(workflowTaskIds, basicHeaders());
		endPoint.append("/v1/onboardingWorkflows/");
		endPoint.append(workflowId);
		endPoint.append("/tasks/delete");

		RequestPayload<NeoRestResponseWrapper<List<OnboardingTaskMap>>> payload = new RequestPayload<>(
				endPoint.toString(), HttpMethod.DELETE, requestType,
				new ParameterizedTypeReference<NeoRestResponseWrapper<List<OnboardingTaskMap>>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class).getResponseDetails().getResponseEntity();

	}

	public String deleteSentInvitation(String invitationCode) throws TGOCPRestException {
		StringBuilder endPoint = new StringBuilder(globalProperties.getRest().getRaURL());
		HttpEntity<String> requestType = new HttpEntity<>(basicHeaders());
		endPoint.append("/v1/workflowInvitations/{invitationCode}");
		// endPoint.append(invitationCode);

		RequestPayload<NeoRestResponseWrapper<String>> payload = new RequestPayload<>(endPoint.toString(),
				HttpMethod.DELETE, requestType, new ParameterizedTypeReference<NeoRestResponseWrapper<String>>() {
				});
		return restTemplate.exchange(payload, NeoRestErrorResponse.class, invitationCode).getResponseDetails()
				.getResponseEntity();
	}

	public String sendNotifyRequest(com.ot.session.entity.NotificationRequestVO requestObj) throws TGOCPRestException {
		String endPoint = globalProperties.getRest().getRaURL() + "/v1/notifications";
		HttpEntity<NotificationRequestVO> requestType = new HttpEntity<NotificationRequestVO>(requestObj,
				basicHeaders());
		TGOCPRestResponse<String, Object> notifyResponse = restTemplate.exchange(endPoint, HttpMethod.POST, requestType,
				String.class);

		return notifyResponse.getResponseDetails().getResponseEntity();

	}

}
