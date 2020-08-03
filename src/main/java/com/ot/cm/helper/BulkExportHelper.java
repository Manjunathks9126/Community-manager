package com.ot.cm.helper;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ot.cm.business.dao.BulkDownloadDao;
import com.ot.cm.business.service.BusinessPartnerService.FileType;
import com.ot.cm.business.service.UserService;
import com.ot.cm.constants.EnvironmentConstants;
import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.jpa.entity.BulkDownload;
import com.ot.cm.rest.request.entity.OnboardingWorkflowListingFilterQuery;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.config.properties.GlobalProperties;

@Component
public class BulkExportHelper {

	@Autowired
	private UserService userService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private BulkDownloadDao bulkDownloadDao;

	@Autowired
	GlobalProperties globalProperties;

	private static final String STATUS_PENDING = "PENDING";
	private static final String APPLICATION_NAME = "CM";

	private static final String EXPORT_CONTEXT_BPR = "BPR List";
	private static final String BPR_BULK_EXPORT_END_POINT = "bulkBpList";
	private static final String EXPORT_CONTEXT_OWR = "Onboarding List";
	private static final String OWR_BULK_EXPORT_END_POINT = "bulkWorkflowReport";

	public void initiateBPRBulkExport(BPRFilterSearchQuery request) throws CMApplicationException, TGOCPRestException {

		initiateBulkExport(request.getCompanyId(), request.getUserId(),
				globalProperties.getRest().getBulkExportURL() + BPR_BULK_EXPORT_END_POINT, EXPORT_CONTEXT_BPR,
				request.getLimit(), request);
	}

	public void initiateOWRBulkExport(OnboardingWorkflowListingFilterQuery request)
			throws CMApplicationException, TGOCPRestException {

		initiateBulkExport(request.getCompanyId(), request.getUserId(),
				globalProperties.getRest().getBulkExportURL() + OWR_BULK_EXPORT_END_POINT, EXPORT_CONTEXT_OWR,
				request.getLimit(), request);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void initiateBulkExport(String companyId, String userId, String bulkExportUrl, String exportContext,
			String limit, Object requestObject) throws CMApplicationException, TGOCPRestException {
		String emailId = userService.getUserInfo(userId).getContactInformation().getEmail();
		BulkDownload bulkDownload = new BulkDownload();
		bulkDownload.setCompanyId(companyId);
		bulkDownload.setUserEmailId(emailId);
		bulkDownload.setDataExtractionUrl(bulkExportUrl);
		bulkDownload.setCallerApplication(APPLICATION_NAME);
		bulkDownload.setFileType(FileType.EXCEL.toString());
		bulkDownload.setExportContext(exportContext);
		bulkDownload.setTotalRecords(Integer.valueOf(limit));
		bulkDownload.setStatus(STATUS_PENDING);
		bulkDownload.setCreatedBy(userId);
		bulkDownload.setCreatedTimeStamp(new Date());
		ObjectMapper mapper = new ObjectMapper();
		try {
			bulkDownload.setFilterString(mapper.writeValueAsString(requestObject));
			bulkDownload = bulkDownloadDao.saveBulkRequest(bulkDownload);
			jmsTemplate.convertAndSend(EnvironmentConstants.AMQ_NAME, mapper.writeValueAsString(bulkDownload));

		} catch (IOException e) {
			throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
					"SaveBulk Request : Error Saving Bulk Request", e);
		}

	}
}
