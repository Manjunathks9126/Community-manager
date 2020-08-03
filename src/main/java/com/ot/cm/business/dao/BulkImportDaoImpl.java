package com.ot.cm.business.dao;

import java.util.Date;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ot.cm.jpa.entity.BulkImport;
import com.ot.session.annotation.Loggable;

@Repository("bulkImportDao")
public class BulkImportDaoImpl extends TGOCPBaseDao implements BulkImportDao {

	private static final Logger logger = LoggerFactory.getLogger(BulkImportDaoImpl.class);

	@Override
	@Loggable
	@Transactional
	public BulkImport uploadBulkImportData(BulkImport importData) {
		logger.info("uploading import data");
		Session session = getSession();

		try {
			session.save(importData);
			logger.info("saved bulkImport with requestID " + importData.getRequestId());

		} catch (Exception ex) {
			logger.info("exception saving " + ex);
			throw ex;
		}

		return importData;

	}

	@Override
	@Loggable
	@Transactional
	public void updateBulkImportRequest(BulkImport importData) {
		Session session = getSession();

		try {
			StringBuilder query = new StringBuilder("update BulkImport set modifiedTimeStamp=:newData ");
			if (!StringUtils.isEmpty(importData.getProcessStartTime())) {
				query.append(", processStartTime=:startTime");
			}
			if (!StringUtils.isEmpty(importData.getProcessEndTime())) {
				query.append(", processEndTime=:endTime");
			}
			
			if (!StringUtils.isEmpty(importData.getStatus())) {
				query.append(", status=:status");
			}
			if (!StringUtils.isEmpty(importData.getModifiedBy())) {
				query.append(", modifiedBy=:modifiedBy");
			}
			if (!StringUtils.isEmpty(importData.getUserEmailId())) {
				query.append(", userEmailId=:mailId");
			}
			if (!StringUtils.isEmpty(importData.getRemarks())) {
				query.append(", remarks=:remark");
			}
			if (!ObjectUtils.isEmpty(importData.getData())) {
				query.append(", data=:pData");
			}
			if (!StringUtils.isEmpty(importData.getMetaInfo())) {
				query.append(", metaInfo=:metaInfo");
			}
			
			query.append(" where requestId= :reqId");

			Query q = session.createQuery(query.toString());

			q.setParameter("newData", new Date());
			q.setParameter("reqId", importData.getRequestId());
			if (!StringUtils.isEmpty(importData.getProcessStartTime())) {
				q.setParameter("startTime", importData.getProcessStartTime());
			}
			if (!StringUtils.isEmpty(importData.getProcessEndTime())) {
				q.setParameter("endTime", importData.getProcessEndTime());
			}
			if (!StringUtils.isEmpty(importData.getMetaInfo())) {
				q.setParameter("metaInfo", importData.getMetaInfo());
			}
			if (!ObjectUtils.isEmpty(importData.getData())) {
				q.setParameter("pData", importData.getData());
			}
			if (!StringUtils.isEmpty(importData.getStatus())) {
				q.setParameter("status", importData.getStatus());
			}
			if (!StringUtils.isEmpty(importData.getModifiedBy())) {
				q.setParameter("modifiedBy", importData.getModifiedBy());
			}
			if (!StringUtils.isEmpty(importData.getUserEmailId())) {
				q.setParameter("mailId", importData.getUserEmailId());
			}
			if (!StringUtils.isEmpty(importData.getRemarks())) {
				q.setParameter("remark", importData.getRemarks());
			}

			int count = q.executeUpdate();
			logger.info("updated: : " + importData.getRequestId() + " count " + count);

		} catch (Exception ex) {
			logger.info("exception while updating " + importData.getRequestId() + "->" + ex);
			throw ex;
		}
	}

	@Override
	@Loggable
	@Transactional
	public BulkImport getBulkImportData(String requestId) {
		logger.info("get file");
		Session session = getSession();

		try {
			return session.get(BulkImport.class, Long.parseLong(requestId));

		} catch (Exception ex) {
			logger.info("exception saving " + ex);
			ex.printStackTrace();
			throw ex;
		}
	}

}
