package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.client.OnboardingTasksRestClient;
import com.ot.cm.rest.response.entity.CustomFieldEntity;
import com.ot.cm.rest.response.entity.OnboardingCustomFieldGroupType;
import com.ot.cm.rest.response.entity.OnboardingTask;
import com.ot.cm.rest.response.entity.OnboardingWorkflowTaskMap;
import com.ot.cm.vo.RegistrationTask;

@Component
public class CompanyRegistrationServiceImpl implements CompanyRegistrationService {

	@Autowired
	OnboardingTasksRestClient onboardingTasksRestClient;

	private final String OR_CONDITION = "OR";
	private final String AND_CONDITION = "AND";

	/**
	 * method to fetch list of task for registration workflow based on workflow
	 * ID
	 * 
	 * @param workFlowId
	 * @param locale
	 * @return
	 * @throws TGOBaseException
	 */
	public List<RegistrationTask> getTaskList(String invitingCompanyId, Long workFlowId, String locale)
			throws TGOCPBaseException {
		List<OnboardingWorkflowTaskMap> onboardingTaskList = onboardingTasksRestClient.getTaskList(workFlowId, locale)
				.getResponseDetails().getResponseEntity();

		Comparator<RegistrationTask> comparator = Comparator.comparing(RegistrationTask::getTaskType).reversed()
				.thenComparing(RegistrationTask::getPosition);

		List<RegistrationTask> registrationTasks = onboardingTaskList.stream().map(onboardingTask -> {
			RegistrationTask task = new RegistrationTask();
			task.setCreatedBy(onboardingTask.getTask().getCreatedBy());
			task.setCreatedTimestamp(onboardingTask.getTask().getCreatedTimestamp());
			task.setDescription(onboardingTask.getTask().getDescription());
			task.setDisplayName(onboardingTask.getTask().getDisplayName());
			task.setPosition(onboardingTask.getPosition());
			task.setSchemaName(onboardingTask.getTask().getSchemaName());
			task.setTaskId(onboardingTask.getTask().getTaskId());
			task.setTaskL10ns(onboardingTask.getTask().getTaskL10ns());
			task.setTaskStage(onboardingTask.getTask().getTaskStage());
			task.setTaskName(onboardingTask.getTask().getTaskName());
			task.setTaskType(onboardingTask.getTask().getTaskType());
			task.setSchemaUrl(
					buildTaskURL(ApplicationConstants.REGISTRATION_TASKS_BASE_URL, onboardingTask.getTask().getTaskId(),
							locale, onboardingTask.getTask().getTaskType(), invitingCompanyId, workFlowId));
			task.setInputUrl(ApplicationConstants.INPUT_URI);

			return task;
		}).filter(x -> !x.getTaskStage().equalsIgnoreCase("Requester")).sorted(comparator).collect(Collectors.toList());

		return registrationTasks;
	}

	private String buildTaskURL(String context, Long taskId, String locale, String taskType, String invitingCompanyId,
			Long workflowId) {
		StringBuffer sb = new StringBuffer(context);
		sb.append("/");
		sb.append(workflowId);
		sb.append("/tasks/");
		if ("Custom".equalsIgnoreCase(taskType)) {
			sb.append("custom/").append(taskId).append("?locale=" + locale)
					.append("&invitingCompanyId=" + invitingCompanyId);
		} else {
			sb.append(taskId).append("?locale=" + locale);
		}
		return sb.toString();
	}

	public OnboardingTask getTask(Long taskId, String locale) throws TGOCPBaseException {
		OnboardingTask task = onboardingTasksRestClient.getTask(taskId, locale).getResponseDetails()
				.getResponseEntity();
		return task;
	}

	/**
	 * method to fetch custom field groups linked to a task ID
	 * 
	 * @param taskId
	 * @return
	 * @throws TGOBaseException
	 */
	@Override
	public List<OnboardingCustomFieldGroupType> getCustomTask(String invitingCompanyId, Long taskId)
			throws TGOCPBaseException {
		// ra call to get selected group ids
		List<String> groupIds = onboardingTasksRestClient.getCustomTask(taskId).getResponseDetails()
				.getResponseEntity();

		List<OnboardingCustomFieldGroupType> fieldGroups = getParentChildFieldGroups(invitingCompanyId, groupIds);

		// Sorting the list to show Visible and then Hidden field groups
		return fieldGroups.stream().sorted((group1, group2) -> group1.isHidden().compareTo(group2.isHidden()))
				.collect(Collectors.toList());

	}

	/**
	 * This methods Gets all the Field groups under HUB and filters out the
	 * available groups(Parent & Child) based on the Custom Task configuration
	 * 
	 * @param invitingCompanyId
	 * @param groupIds
	 * @return
	 * @throws TGOBaseException
	 */
	private List<OnboardingCustomFieldGroupType> getParentChildFieldGroups(String invitingCompanyId,
			List<String> groupIds) throws TGOCPBaseException {
		// CMD call to get the details of all groups under HUB
		List<OnboardingCustomFieldGroupType> allGroups = onboardingTasksRestClient.getAllFieldGroups(invitingCompanyId);

		// Sorting the list to Show Parent field groups first then Hidden
		allGroups.sort(Comparator.comparing(OnboardingCustomFieldGroupType::getDependentCustomfieldId,
				Comparator.nullsFirst(String::compareTo)));

		// Construct list of all dependent custom field Ids
		String dependentCustomfieldIds = allGroups.stream()
				.filter(group -> (null != group.getDependentCustomfieldId() && null != group.getDependentChoiceIds()
						&& group.getDependentChoiceIds().length > 0))
				.map(OnboardingCustomFieldGroupType::getDependentCustomfieldId).collect(Collectors.joining(","));

		// Get dependent field info from CMD for all groups and Set to the list
		List<CustomFieldEntity> dependentCustomfields = onboardingTasksRestClient
				.searchCustomFields(dependentCustomfieldIds);
		allGroups.forEach(group -> group.setDependentCustomField(dependentCustomfields.stream()
				.filter(field -> field.getUniqueId().equals(group.getDependentCustomfieldId())).findFirst()
				.orElse(null)));

		return getAllowedFieldGroups(allGroups, groupIds);
	}

	private List<OnboardingCustomFieldGroupType> getAllowedFieldGroups(List<OnboardingCustomFieldGroupType> allGroups,
			List<String> groupIds) throws TGOCPBaseException {
		List<OnboardingCustomFieldGroupType> allowedGroups = new ArrayList<>();

		// Construct allowed groups list
		for (OnboardingCustomFieldGroupType group : allGroups) {

			if (checkIfGroupIsAllowed(allGroups, groupIds, group)) {
				// construct customFieldURL for each group
				group.setUrl(buildCustomFieldsURL(group.getUniqueId()));

				// for each group check dependency to update visibility
				if (null != group.getDependentCustomfieldId() && null != group.getDependentChoiceIds()
						&& group.getDependentChoiceIds().length > 0) {
					CustomFieldEntity parentField = group.getDependentCustomField();
					group.setDependentGroupId(parentField.getGroupId());

					if (null == parentField.getDefaultChoice()
							|| allowedGroups.stream().filter(grp -> parentField.getGroupId().equals(grp.getUniqueId()))
									.findFirst().get().isHidden()) {
						group.setHidden(true);
					} else {
						if (OR_CONDITION.equalsIgnoreCase(group.getDependencyConjunction())) {
							if (Collections.disjoint(Arrays.asList(group.getDependentChoiceIds()),
									Arrays.asList(parentField.getDefaultChoice().getId()))) {
								group.setHidden(true);
							} else {
								group.setHidden(recurrsiveVisibiltyCheck(parentField));
							}
						} else if (AND_CONDITION.equalsIgnoreCase(group.getDependencyConjunction())) {
							if (Arrays.asList(group.getDependentChoiceIds())
									.containsAll(Arrays.asList(parentField.getDefaultChoice().getId()))) {
								group.setHidden(recurrsiveVisibiltyCheck(parentField));
							} else {
								group.setHidden(true);
							}
						}
					}
				}

				allowedGroups.add(group);
			}

		}

		return allowedGroups;
	}

	/**
	 * Filtering the available field groups(Parent & Child) for the Custom task
	 * - Supports only 2 levels
	 * 
	 * @param groupIds
	 * @param group
	 * @return
	 */
	private boolean checkIfGroupIsAllowed(List<OnboardingCustomFieldGroupType> allGroups, List<String> groupIds,
			OnboardingCustomFieldGroupType group) {
		OnboardingCustomFieldGroupType parentGroup = allGroups.stream()
				.filter(grp -> group.getDependentCustomField() != null
						&& group.getDependentCustomField().getGroupId().equals(grp.getUniqueId()))
				.findFirst().orElse(null);

		return groupIds.contains(group.getUniqueId()) || (null != group.getDependentCustomField()
				&& (groupIds.contains(group.getDependentCustomField().getGroupId())
						|| (null != parentGroup.getDependentCustomField()
								&& groupIds.contains(parentGroup.getDependentCustomField().getGroupId()))));

	}

	private String buildCustomFieldsURL(String groupId) {
		StringBuffer sb = new StringBuffer("/registration/groups/");
		sb.append(groupId).append("/customFields");
		return sb.toString();
	}

	/**
	 * method to check visibility of custom fields recursively
	 * 
	 * @param customField
	 * @return
	 * @throws TGOBaseException
	 */
	private boolean recurrsiveVisibiltyCheck(CustomFieldEntity customField) throws TGOCPBaseException {
		boolean isHidden = false;
		if (null != customField.getDependentCustomfieldId() && null != customField.getDependentChoiceIds()) {
			CustomFieldEntity parentCustomField = onboardingTasksRestClient
					.getCustomFieldsByFieldId(customField.getDependentCustomfieldId());
			if (null == parentCustomField.getDefaultChoice()) {
				isHidden = true;
			} else {
				if (OR_CONDITION.equalsIgnoreCase(customField.getDependencyConjunction())) {
					if (Collections.disjoint(Arrays.asList(customField.getDependentChoiceIds()),
							Arrays.asList(parentCustomField.getDefaultChoice().getId()))) {
						isHidden = true;
					} else {
						isHidden = recurrsiveVisibiltyCheck(parentCustomField);
					}
				} else if (AND_CONDITION.equalsIgnoreCase(customField.getDependencyConjunction())) {
					if (Arrays.asList(customField.getDependentChoiceIds())
							.containsAll(Arrays.asList(parentCustomField.getDefaultChoice().getId()))) {
						isHidden = recurrsiveVisibiltyCheck(parentCustomField);
					} else {
						isHidden = true;
					}
				}
			}

		} else {
			isHidden = false;
		}

		return isHidden;
	}

}
