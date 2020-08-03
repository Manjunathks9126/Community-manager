package com.ot.cm.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ot.cm.business.service.onboarding.bulk.BulkCustomFieldsProcessingService;
import com.ot.cm.business.service.onboarding.bulk.BulkImportProcessingService;
import com.ot.cm.business.service.onboarding.bulk.BulkInivitationProcessingService;
import com.ot.cm.constants.BulkImportType;

/**
 * 
 * @author ddharmav
 *
 */
@Component
public class BulkImportFactory {

	@Autowired
	private BulkInivitationProcessingService inivitationProcessingService;

	@Autowired
	private BulkCustomFieldsProcessingService customFieldsProcessingService;

	public BulkImportProcessingService getService(BulkImportType type) {

		switch (type) {
		case INVITE:
			return inivitationProcessingService;

		case CUSTOM_FIELD:
			return customFieldsProcessingService;

		}
		return null;
	}

}
