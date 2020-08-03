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
package com.ot.cm.business.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class TGOCPBaseDao {

	@PersistenceContext
	private EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected Criteria createEntityCriteria(Class<?> persistentClass) {
		return getSession().createCriteria(persistentClass);
	}

	protected void refresh(Object persistentObject) {
		entityManager.refresh(persistentObject);
	}

}
