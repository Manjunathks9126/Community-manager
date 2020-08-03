package com.ot.cm.rest.controller.onboarding.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.CompanyRegistrationService;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.entity.OnboardingCustomFieldGroupType;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.vo.RegistrationTask;



@RestController
@RequestMapping(value = "v1/onboardingservices/workflows")
public class OnboardingRegistrationTasksController {
	@Autowired
	CompanyRegistrationService taskRegistrationService;

	/**
	 * API to fetch list of task for registration workflow based on workflow ID
	 * 
	 * @param workflowId
	 * @return
	 * @throws TGOBaseException
	 */
	@GetMapping(value = "/{workflowId}/tasks")
	public List<RegistrationTask> getTaskList(@PathVariable("workflowId") Long workflowId,
			@RequestParam("locale") String locale, @RequestParam("invitingCompanyId") String invitingCompanyId)
			throws TGOCPBaseException {

		return taskRegistrationService.getTaskList(invitingCompanyId, workflowId, locale);

	}

	/**
	 * API to fetch standard task schema using task ID
	 * 
	 * @param taskId
	 * @return
	 * @throws TGOBaseException
	 */
	@GetMapping(value = "/{workflowId}/tasks/{taskId}")
	public OnboardingTask getTask(@PathVariable("workflowId") Long workflowId, @PathVariable("taskId") Long taskId,
			@RequestParam("locale") String locale) throws TGOCPBaseException {
		return taskRegistrationService.getTask(taskId, locale);

	}

	/**
	 * API to fetch custom task schema using task ID
	 * 
	 * @param taskId
	 * @return
	 * @throws TGOBaseException
	 */
	@GetMapping(value = "/{workflowId}/tasks/custom/{taskId}")
	public List<OnboardingCustomFieldGroupType> getCustomTask(@PathVariable("workflowId") Long workflowId,
			@PathVariable("taskId") Long taskId, @RequestParam("locale") String locale,
			@RequestParam("invitingCompanyId") String invitingCompanyId) throws TGOCPBaseException {
		return taskRegistrationService.getCustomTask(invitingCompanyId, taskId);
	}
}
