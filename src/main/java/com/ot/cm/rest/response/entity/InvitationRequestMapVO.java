package com.ot.cm.rest.response.entity;



public class InvitationRequestMapVO {

    private String invitationCode;

    private String invitationId;

    public InvitationRequestMapVO() {
    }

    public InvitationRequestMapVO(String invitationCode, String invitationId) {
        this.invitationCode = invitationCode;
        this.invitationId = invitationId;
    }

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

  
}
