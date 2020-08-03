package com.ot.cm.business.dao;

import com.ot.cm.jpa.entity.BulkDownload;

public interface BulkDownloadDao {

	BulkDownload saveBulkRequest(BulkDownload bulkDownload);

	void updateBulkRequestStatus(BulkDownload bulkDownload);

}
