package com.ot.cm.rest.response.entity;

public class InvitationExtendedRequestNeoRest {

    private String invitationAttrName;

    private String invitationAttrValue;

    public String getInvitationAttrName() {
		return invitationAttrName;
	}

	public void setInvitationAttrName(String invitationAttrName) {
		this.invitationAttrName = invitationAttrName;
	}

	public String getInvitationAttrValue() {
		return invitationAttrValue;
	}

	public void setInvitationAttrValue(String invitationAttrValue) {
		this.invitationAttrValue = invitationAttrValue;
	}

	public InvitationExtendedRequestNeoRest(String invitationAttrName, String invitationAttrValue) {
        this.invitationAttrName = invitationAttrName;
        this.invitationAttrValue = invitationAttrValue;
    }

    public InvitationExtendedRequestNeoRest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvitationExtendedRequestNeoRest that = (InvitationExtendedRequestNeoRest) o;

        if (!invitationAttrName.equals(that.invitationAttrName)) return false;
        return invitationAttrValue.equals(that.invitationAttrValue);
    }

    @Override
    public int hashCode() {
        int result = invitationAttrName.hashCode();
        result = 31 * result + invitationAttrValue.hashCode();
        return result;
    }
}
