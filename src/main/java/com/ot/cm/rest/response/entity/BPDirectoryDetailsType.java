package com.ot.cm.rest.response.entity;

import com.gxs.services.bsapi.rs.v3.entity.TPDirectoryDetailsType;

public class BPDirectoryDetailsType extends TPDirectoryDetailsType {
	private String bprStatus;

	@Override
	public String getBprStatus() {
		return bprStatus;
	}

	@Override
	public void setBprStatus(String bprStatus) {
		this.bprStatus = bprStatus;
	}
	
	

}
