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
 Jun 1, 2018      ssen                              Initial Creation
 ******************************************************************************/
package com.ot.cm.rest.response.entity;

/**
 * @author ssen
 *
 */
public class TPRData {
	private String tprId;
	private String companyId;
	private String companyName;
	private String displayName;
	private String ediAddress;
	private String vanProvider;
	private String yourAddress;
	private String yourVanProvider;
	private String yourRole;
	private String status;
	private String tprType;	
	private String ownerTradingAddressCompanyId;
	
	
	public String getOwnerTradingAddressCompanyId() {
		return ownerTradingAddressCompanyId;
	}

	public void setOwnerTradingAddressCompanyId(String ownerTradingAddressCompanyId) {
		this.ownerTradingAddressCompanyId = ownerTradingAddressCompanyId;
	}

	public String getTprId() {
		return tprId;
	}

	public void setTprId(String tprId) {
		this.tprId = tprId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEdiAddress() {
		return ediAddress;
	}

	public void setEdiAddress(String ediAddress) {
		this.ediAddress = ediAddress;
	}

	public String getVanProvider() {
		return vanProvider;
	}

	public void setVanProvider(String vanProvider) {
		this.vanProvider = vanProvider;
	}

	public String getYourAddress() {
		return yourAddress;
	}

	public void setYourAddress(String yourAddress) {
		this.yourAddress = yourAddress;
	}

	public String getYourVanProvider() {
		return yourVanProvider;
	}

	public void setYourVanProvider(String yourVanProvider) {
		this.yourVanProvider = yourVanProvider;
	}

	public String getYourRole() {
		return yourRole;
	}

	public void setYourRole(String yourRole) {
		this.yourRole = yourRole;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTprType() {
		return tprType;
	}

	public void setTprType(String tprType) {
		this.tprType = tprType;
	}

}
