package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.NeoRolesRestClient;
import com.ot.cm.rest.response.entity.CMDNeoRole;
import com.ot.cm.rest.response.entity.CMDUser;
import com.ot.cm.vo.UserInfo;

@Service
public class CustomRolesServiceImpl implements CustomRolesService {

	@Autowired
	private NeoRolesRestClient neoRolesRestClient;

	private static final String[] customFieldRoleQualifiers = { "NEO_VIEW_CFG", "NEO_EDIT_CFG" };

	@Override
	public List<CMDNeoRole> getCustomFieldRoles(String buId, String userId) throws TGOCPRestException {
		CMDNeoRole[] customRoles = StringUtils.isEmpty(userId) ? neoRolesRestClient.getBURoles(buId)
				: neoRolesRestClient.getUserNeoRoles(buId, userId);

		return Arrays.asList(customRoles).stream()
				.filter(role -> Arrays.asList(customFieldRoleQualifiers).contains(role.getRoleQualifier()))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getUsers(String roleId, UserInfo userInfo) throws TGOCPRestException {
		List<String> userlist = new ArrayList<>();

		CMDUser[] respone = neoRolesRestClient.getUsers(roleId, userInfo);

		userlist = Arrays.asList(respone).stream()
				.map(user -> user.getFirstName() + " " + user.getLastName() + " (" + user.getLoginName() + ")")
				.collect(Collectors.toList());

		return userlist;
	}

}
