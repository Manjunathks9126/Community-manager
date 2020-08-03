package com.ot.cm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.client.OnboardingRestClient;
import com.ot.cm.rest.response.entity.OnboardingTask;


/**
 * service class for registration tasks
 * @author vishwajk
 *
 */
@Component
public class RequestorTaskServiceImpl implements RequestorTaskService {
	
	@Autowired
    OnboardingRestClient onboardingRestClient;
	
	/**
	 * method to get task details by using task ID
	 * @param taskId
	 * @param locale
	 * @return
	 * @throws TGOBaseException
	 */
	@Override
	public OnboardingTask getTask(Long taskId, String locale) throws TGOCPBaseException {
		return onboardingRestClient.getTask(taskId, locale).getResponseDetails().getResponseEntity();
	}

}
