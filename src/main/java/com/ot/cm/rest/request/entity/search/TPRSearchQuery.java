package com.ot.cm.rest.request.entity.search;

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
 02/17/2017	   Madan								TPR SearchQeury
******************************************************************************/
import java.util.List;

/**
 * Class {@code TPRSearchQuery} captures search query parameters for
 * TrdingPartnerRelations data to list,export
 */
public class TPRSearchQuery extends BaseSearchQuery {

	private String partnerCompanyId;
	private List<String> ediAdderess;
	private List<String> tprIds;
	private String dateFrom;
	private String dateTo;
	private String partnerediaddress;

	public String getPartnerCompanyId() {
		return partnerCompanyId;
	}

	public void setPartnerCompanyId(String partnerCompanyId) {
		this.partnerCompanyId = partnerCompanyId;
	}

	public List<String> getEdiAdderess() {
		return ediAdderess;
	}

	public void setEdiAdderess(List<String> ediAdderess) {
		this.ediAdderess = ediAdderess;
	}

	public List<String> getTprIds() {
		return tprIds;
	}

	public void setTprIds(List<String> tprIds) {
		this.tprIds = tprIds;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getPartnerediaddress() {
		return partnerediaddress;
	}

	public void setPartnerediaddress(String partnerediaddress) {
		this.partnerediaddress = partnerediaddress;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TPRSearchQuery [partnerCompanyId=").append(partnerCompanyId).append(", ediAdderess=")
				.append(ediAdderess).append(", tprIds=").append(tprIds).append(",partnerediaddress=")
				.append(partnerediaddress).append(",dateFrom=").append(dateFrom).append(",dateTo=").append(dateTo)
				.append("]");
		return builder.toString();
	}

}
