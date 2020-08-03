
package com.ot.cm.constants;

/**
 * This class contains all constants related to application.
 * 
 * @author Dwaraka
 *
 */
public final class ApplicationConstants {

	private ApplicationConstants() {
	}

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String USER_ID = "userId";
	public static final String USER_LOCALE = "userLocale";
	public static final int IM_QUERY_MAX_RECORDS = 1000;
	public static final String RESELLER_CUTOMER = "RESELLER_CUSTOMER";
	public static final String CUSTOMER = "CUSTOMER";
	public static final String INTERCONNECT = "INTERCONNECT_CUSTOMER";
	public static final String SERVICEPROVIDERTP = "SERVICE_PROVIDER_TP";
	public static final String TRADINGGRID_COMMUNITY = "Trading Grid Community";
	public static final String COMPANY_STATUS_INPROGRESS = "In Progress";
	public static final String INV_CODE = "INV_CODE";
	public static final String INVITING_SERVICE = "INVITING_SERVICE";
	public static final String AUDIT_APP_NAME = "NEO Community Manager";
	public static final String PLATFORM_ENTITYFORM_URL = "/home/cmsneo/app/processExperience/web/perform/items/0050560122a0a1e89760c93caa8537fb.";
	public static final String PSP_VANTP_LAYOUT_URL = "/0050560122a0a1e8976601bcdf2bb7fb";
	public static final String PSP_CORE_LAYOUT_URL = "/0050560122a0a1e89cada362ce280726";
	public static final String COMPANY_STATUS_TESTING = "Testing";
	public static final String BPR_STATUS_REQUESTED = "REQUESTED";
	public static final String BPR_STATUS_TESTING = "Testing";
	public static final String BPR_STATUS_APPROVED = "APPROVED";
	public static final String FORBIDDEN_PATTERN = "[<>]";
	public static final String EMAIL_PATTERN = "^[a-zA-Z0-9!#$%+/=?^_`{|}~-]+(((?:\\.[a-zA-Z0-9!#(),:;$%+/=?^_`{|}~-]+)*|(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x20\\x5c\\x21\\x23-\\x25\\x28\\x29\\x2b-\\x2d\\x2f-\\x3b\\x3d\\x3f-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]))*)@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x25\\x28\\x29\\x2b-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	public static final String FILTER = "FILTER";
	public static final String ADDRESS = "ADDRESS";
	public static final String FACADE = "FACADE";
	public static final String BU_NAME = "BU_NAME";

	public static final String REQUESTER_TASK_STAGE = "Requester";

	public static final String CPS_INVITATION_SOURCE = "TGOCP";

	public static final String NOTIFICATION_TYPE = "onboardingInvitationNotification";
	public static final String NOTIFICATION_LANG_CODE = "en";
	public static final String FWD_SERVICE = "NotificationService";
	public static final String SERVICE_TYPE = "EMAIL";
	public static final String APPLICATION_SOURCE = "TGOCP-CM";
	public static final String INVITATION_SPOKE_NODE_ID_SELECTED = "INVITATION_SPOKE_NODE_ID_SELECTED";
	public static final String INVITING_COMPANY_EDI_ADDRESS_LIST = "INVITING_COMPANY_EDI_ADDRESS_LIST";
	public static final String CPS_ACTION = "CREATE";
	public static final String RESPONSE_STATUS_SUCCESS = "SUCCESS";
	public static final String RESPONSE_STATUS_INFO = "INFO";
	public static final String ESC_SPECIAL_CHARS = "escapeSpecialChars";
	public static final String[] SOLR_SPECTIAL_CHAR_LIST = new String[] { "%5C", "-", "%26%26%", "%7C%7C", "%21", "%28",
			"%29", "%7B", "%7D", "%5B", "%5D", "%5E", "%22", "%7E", "*", "%3F", "%3A", "%2F" };
	public static final String[] PREFERRED_DATE_FORMAT = { "MM/dd/yyyy", "EEEEE, MMMMM dd, yyyy", "MM/dd", "MM/dd/yy",
			"dd-MMM", "MMM dd, yyyy", "MMMMM dd, yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd/MM/yy", "dd-MMM-yyyy",
			"dd-MMM-yy" };

	public static final String VAN_PROVIDER = "VAN PROVIDER";
	public static final String SERVICE_PROVIDER = "SERVICE_PROVIDER";
	public static final String RESPONSE_STATUS_ERROR = "ERROR";
	public static final String REGISTRATION_TASKS_BASE_URL = "v1/onboardingservices/workflows";
	public static final String INPUT_URI = "testinputurl";

	public static final String RESPONSE_STATUS_WARNING = "WARNING";
	public static final String STATUS_ACTIVE = "ACTIVE";

	public static final String DEFAULT_CUSTOM_FIELD_CATEGORY_CM = "CM";

}
