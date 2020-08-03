/******************************************************************************
 All rights reserved. All information contained in this software is confidential
 and proprietary to opentext. No part of this software may be
 reproduced or transmitted in any form or any means, electronic, mechanical,
 photocopying, recording or otherwise stored in any retrieval system of any
 nature without the prior written permission of opentext.
 This material is a trade secret and its confidentiality is strictly maintained.
 Use of any copyright notice does not imply unrestricted public access to this
 material.

 (c) opentext

 *******************************************************************************
 Change Log:
 Date          Name                Defect#           Description
 -------------------------------------------------------------------------------
 11/16/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.business.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ot.cm.cms.response.entity.User;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.UsersRestClient;
import com.ot.cm.rest.request.entity.UserFilter;
import com.ot.cm.rest.response.entity.ListingQueryResponse;
import com.ot.cm.util.CommonUtil;
import com.ot.session.annotation.Loggable;

@Service
public class UserService {
	@Autowired
	private UsersRestClient userRestClient;

	public UserService() {
	}

	/**
	 * @param userId
	 * @return
	 */
	public User getUserInfo(String userId) throws TGOCPRestException {
		if (!StringUtils.isEmpty(userId)) {
			return userRestClient.getUser(userId);
		}
		return new User();
	}

	@Loggable
	public ListingQueryResponse<User> getUsers(UserFilter filterObject)
			throws ParseException, TGOCPBaseException, TGOCPRestException {
		ListingQueryResponse<User> tprResponseEntity = new ListingQueryResponse<User>();
		if (!StringUtils.isEmpty(filterObject.getId())) {
			StringBuilder filterQuery = new StringBuilder();
			filterQuery.append("where_company_id=");
			filterQuery.append(filterObject.getId());
			filterQuery.append(CommonUtil.isEmpty(filterObject.getFirstName()) ? ""
					: ("&where_user_first_name=*" + filterObject.getFirstName() + "*"));
			filterQuery.append(CommonUtil.isEmpty(filterObject.getLastName()) ? ""
					: ("&where_user_last_name=*" + filterObject.getLastName() + "*"));
			filterQuery.append(CommonUtil.isEmpty(filterObject.getEmail()) ? ""
					: ("&where_user_email=*" + filterObject.getEmail() + "*"));
			filterQuery.append(CommonUtil.isEmpty(filterObject.getDateFrom()) ? ""
					: ("&where_last_login_date_from="
							+ CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", filterObject.getDateFrom())));
			filterQuery.append(CommonUtil.isEmpty(filterObject.getDateTo()) ? ""
					: ("&where_last_login_date_to="
							+ CommonUtil.formatDate("MM/dd/yyyy", "dd-MM-yyyy", filterObject.getDateTo())));
			filterQuery.append("&where_consent_cause=");
			filterQuery.append("YES");

			if (filterObject.getStatus().length > 0) {
				String statusString = "";
				for (String status : filterObject.getStatus()) {
					statusString += status.toLowerCase() + ",";
				}
				filterQuery.append("&where_user_status=" + statusString);
			}

			if (filterObject.isCountOnly()) {
				filterQuery.append("&count_only=true");
				String itemCount = userRestClient.getUsersCount(filterQuery.toString());
				if (!CommonUtil.isEmpty(itemCount)) {
					tprResponseEntity.setItemCount(Integer.parseInt(itemCount));
				}
			} else {
				filterQuery.append("&after=");
				filterQuery.append(filterObject.getAfter());
				filterQuery.append("&limit=");
				filterQuery.append(filterObject.getLimit());
				User[] users = userRestClient.getUsers(filterQuery.toString());
				if (users.length > 0) {
					ArrayList<User> userList = new ArrayList<User>(Arrays.asList(users));
					userList.stream().forEach(user -> {
						user.setStatus(CommonUtil.toTitleCase(user.getStatus()));

					});

					if (null != userList && userList.size() > 0) {
						tprResponseEntity.setItemList(userList);

					}
				}
			}

		}
		return tprResponseEntity;
	}

}
