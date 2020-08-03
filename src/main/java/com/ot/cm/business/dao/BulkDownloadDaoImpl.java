package com.ot.cm.business.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ot.cm.jpa.entity.BulkDownload;
import com.ot.session.annotation.Loggable;

@Repository("bulkDownloadDao")
public class BulkDownloadDaoImpl extends TGOCPBaseDao implements BulkDownloadDao {

	private final static Logger logger = LoggerFactory.getLogger(BulkDownloadDaoImpl.class);

	@Override
	@Loggable
	@Transactional
	public BulkDownload saveBulkRequest(BulkDownload bulkDownload) {
		logger.info("save bulk request called");
		Session session = getSession();

		try {
			session.save(bulkDownload);
			logger.info("saved: : "+bulkDownload.getRequestId());
			
		} catch (Exception ex) {
			logger.info("exception saving "+ ex);
			throw ex;
		}
		
		return bulkDownload;
		
	}

	@Override
	public void updateBulkRequestStatus(BulkDownload bulkDownload) {
		// TODO Auto-generated method stub
		logger.info("save bulk request called");
		Session session = getSession();

		try {
			session.update(bulkDownload);
			logger.info("saved: : "+bulkDownload.getRequestId());
			
		} catch (Exception ex) {
			logger.info("exception saving "+ ex);
			throw ex;
		}
		
		
	}

}
