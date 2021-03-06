/**
 * 
 */
package com.ot.cm.constants;

/**
 * @author nkumari
 *
 */
public enum RestActionContext {
	
	INVITED_USERS_SEARCH("INVITED_USERS_SEARCH"), USERS_SEARCH("USERS_SEARCH"), GET_USER("GET_USER"), GET_BILLING(
			"GET_BILLING"), GET_BU_DETAILS("GET_BU_DETAILS"), PUT_BU_DETAILS(
					"PUT_BU_DETAILS"), GET_BU_SUBSCRIPTION("GET_BU_SUBSCRIPTION"), GET_BU_CONTRACT("GET_BU_CONTRACT"),
	GET_BU_MAILBOX("GET_BU_MAILBOX"),GET_BU_EDI("GET_BU_EDI"),CR_BP_DETAILS("CR_BP_DETAILS"),EDIT_TP_DETAILS("EDIT_TP_DETAILS"),CR_BU_REGISTRATION("CR_BU_REGISTRATION"),CR_TP_DETAILS("CR_TP_DETAILS"),GET_BU_WORKFLOW("GET_BU_WORKFLOW"),GET_USERS_SUBSCRIPTION("GET_USERS_SUBSCRIPTION"),GET_BU_VANPROVIDERS("GET_BU_VANPROVIDERS"),GET_EDI_ADDRESS("GET_EDI_ADDRESS"),GET_EDI_COUNT("GET_EDI_COUNT"),GET_TILE_CONTENT("GET_TILE_CONTENT"),TPR_EDI_ADDRESS("TPR_EDI_ADDRESS");
	final String context;

	private RestActionContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

}
	


