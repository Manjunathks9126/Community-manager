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
 08/03/2017    Dwaraka                              Initial Creation
 ******************************************************************************/
package com.ot.cm.vo;

import java.io.Serializable;
import java.util.Locale;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String companyId;
	private String firstName;
	private String lastName;
	private Locale locale;
	private String serviceInstanceID;
	private String communityID;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getServiceInstanceID() {
		return serviceInstanceID;
	}

	public void setServiceInstanceID(String serviceInstanceID) {
		this.serviceInstanceID = serviceInstanceID;
	}

	public String getCommunityID() {
		return communityID;
	}

	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}
}
