package com.ot.cm.business.service;

import java.util.List;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.entity.OnboardingCustomFieldGroupType;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.vo.RegistrationTask;

public interface CompanyRegistrationService {

	List<RegistrationTask> getTaskList(String invitingCompanyId, Long workflowId, String locale) throws TGOCPBaseException;

	OnboardingTask getTask(Long taskId, String locale) throws TGOCPBaseException;

	List<OnboardingCustomFieldGroupType> getCustomTask(String invitingCompanyId, Long taskId) throws TGOCPBaseException;

}
