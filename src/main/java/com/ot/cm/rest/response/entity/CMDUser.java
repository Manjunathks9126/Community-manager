package com.ot.cm.rest.response.entity;

import com.ot.cm.cms.response.entity.User;

public class CMDUser extends User {
	private String loginName;

	public CMDUser() {
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
