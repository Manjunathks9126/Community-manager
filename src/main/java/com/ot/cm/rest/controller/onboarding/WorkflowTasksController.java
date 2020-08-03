package com.ot.cm.rest.controller.onboarding;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.OnboardingService;
import com.ot.cm.business.service.RequestorTaskService;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.CustomTaskFieldGroupUpdate;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.rest.response.entity.OnboardingTaskMap;
import com.ot.cm.util.TGOCPSessionUtil;

@RestController
@RequestMapping("workflow")
@CheckPermissions(permissions = PermissionConstants.VIEW_ONBOARDING)
public class WorkflowTasksController {

	@Autowired
	OnboardingService onboardingService;

	@Autowired
	RequestorTaskService taskRegistrationService;

	@GetMapping(value = "/{workflowId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<OnboardingTaskMap>, ErrorResponse> workflowTaskList(
			@PathVariable("workflowId") Long workflowId,
			@RequestParam(value = "taskStage", required = false) String taskStage) throws TGOCPRestException {

		TGOCPRestResponse<List<OnboardingTaskMap>, ErrorResponse> restResponse = null;
		TGOCPRestResponseDetails<List<OnboardingTaskMap>, ErrorResponse> restResponseDetail = new TGOCPRestResponseDetails<>();

		List<OnboardingTaskMap> taskList = null;
		taskList = onboardingService.getOnboardingTasks(workflowId, taskStage);

		restResponseDetail.setResponseEntity(taskList);
		restResponseDetail.setSuccess(true);
		restResponseDetail.setHttpStatus(HttpStatus.OK);

		restResponse = new TGOCPRestResponse<>(restResponseDetail);
		return restResponse;
	}

	/**
	 * API to fetch standard task schema using task ID
	 * 
	 * @param taskId
	 * @return
	 * @throws TGOCPBaseException
	 */
	@GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OnboardingTask getTask(@PathVariable("taskId") Long taskId, @RequestParam("locale") String locale)
			throws TGOCPBaseException {
		return taskRegistrationService.getTask(taskId, locale);
	}

	@GetMapping(value = "/tasks/{taskId}/fieldgroups", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<String>, ErrorResponse> getFieldGroupList(@PathVariable("taskId") Long taskId)
			throws TGOCPRestException {
		List<String> fgList = null;
		fgList = onboardingService.getFieldGroups(taskId);

		TGOCPRestResponse<List<String>, ErrorResponse> restResponse = null;
		TGOCPRestResponseDetails<List<String>, ErrorResponse> restResponseDetail = new TGOCPRestResponseDetails<>();
		restResponseDetail.setResponseEntity(fgList);
		restResponseDetail.setSuccess(true);
		restResponseDetail.setHttpStatus(HttpStatus.OK);

		restResponse = new TGOCPRestResponse<>(restResponseDetail);
		return restResponse;
	}

	@PostMapping(value = "/tasks/{taskId}/fieldgroups", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<String, ErrorResponse> updateFieldGroupList(@PathVariable("taskId") Long taskId,
			@RequestBody OnboardingTaskMap taskMap, HttpServletRequest httpServletRequest)
			throws TGOCPRestException, IOException {
		long position = 0;
		List<OnboardingTaskMap> list = onboardingService.createOrUpdateTask(taskMap.getWorkflowId(), taskMap,
				TGOCPSessionUtil.getUserInfoInSession(httpServletRequest));

		onboardingService.updateCustomTaskFieldGroups(taskId, taskMap.getFieldgroup());

		TGOCPRestResponse<String, ErrorResponse> restResponse = null;
		TGOCPRestResponseDetails<String, ErrorResponse> restResponseDetail = new TGOCPRestResponseDetails<>();
		restResponseDetail.setResponseEntity(null);
		restResponseDetail.setSuccess(true);
		restResponseDetail.setHttpStatus(HttpStatus.OK);

		restResponse = new TGOCPRestResponse<>(restResponseDetail);

		return restResponse;
	}

	@PostMapping(value = "/{workflowId}/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<List<OnboardingTaskMap>, ErrorResponse> createTask(
			@PathVariable("workflowId") Long workflowId, @RequestBody OnboardingTaskMap onboTask,
			HttpServletRequest httpServletRequest) throws TGOCPRestException, IOException {
		long position = 0;
		onboTask.setPosition(position);
		List<OnboardingTaskMap> list = onboardingService.createOrUpdateTask(workflowId, onboTask,
				TGOCPSessionUtil.getUserInfoInSession(httpServletRequest));
		long taskId = 0;
		for (OnboardingTaskMap task : list) {
			taskId = task.getTask().getTaskId();
		}
		CustomTaskFieldGroupUpdate cutomTaskFieldGoup = new CustomTaskFieldGroupUpdate();
		cutomTaskFieldGoup.setFgId(onboTask.getFgId());
		cutomTaskFieldGoup.setTaskId(taskId);
		onboardingService.updateCustomTaskFieldGroups(taskId, cutomTaskFieldGoup);
		TGOCPRestResponse<List<OnboardingTaskMap>, ErrorResponse> restResponse = null;
		TGOCPRestResponseDetails<List<OnboardingTaskMap>, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<List<OnboardingTaskMap>, ErrorResponse>();
		responseDetails.setResponseEntity(list);
		responseDetails.setSuccess(true);
		responseDetails.setHttpStatus(HttpStatus.OK);
		restResponse = new TGOCPRestResponse<>(responseDetails);
		return restResponse;

	}

	@Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-ONBOARD-WRKFLWFACADE-D", actionSummary = "Onboarding Workflow Facade Deleted", actionDetail = "Deleted Workflow facade for : {0} ", hasContent = false)
	@PostMapping(value = "/{workflowId}/tasks/delete")
	public TGOCPRestResponse<Boolean, ErrorResponse> deleteByWorkflowTaskId(@RequestBody Long[] Ids,
			HttpServletRequest httpServletRequest, @PathVariable("workflowId") Long workflowId)
			throws TGOCPRestException, CMApplicationException {
		TGOCPRestResponse<Boolean, ErrorResponse> response = null;
		TGOCPRestResponseDetails<Boolean, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<Boolean, ErrorResponse>();

		try {
			onboardingService.delete(Ids, workflowId, TGOCPSessionUtil.getUserInfoInSession(httpServletRequest));
			responseDetails.setSuccess(true);

		} catch (TGOCPRestException e) {

		}
		response = new TGOCPRestResponse<>(responseDetails);
		return response;
	}

}
