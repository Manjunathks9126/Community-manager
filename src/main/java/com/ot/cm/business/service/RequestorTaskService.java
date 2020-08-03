package com.ot.cm.business.service;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.entity.OnboardingTask;

public interface RequestorTaskService {

	/**
	 * method to get task details by using task ID
	 * @param taskId
	 * @param locale
	 * @return
	 * @throws TGOBaseException
	 */
	OnboardingTask getTask(Long taskId, String locale) throws TGOCPBaseException;

}