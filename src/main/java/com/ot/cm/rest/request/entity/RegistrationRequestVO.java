package com.ot.cm.rest.request.entity;

import java.util.List;

import com.ot.cm.rest.response.entity.InvitationRequestMapVO;
import com.ot.cm.rest.response.entity.RequestData;


public class RegistrationRequestVO {

    private String requestStatus;

    private String createdTimestamp;

    private String requestType;

    private String author;

    private String createdBy;

    private String city;

    private String companyName;

    private String buId;

    private String country;

    private List<RequestData> requestData;

    private InvitationRequestMapVO  invitationRequestMapVO;


    public RegistrationRequestVO() {
    }

    public RegistrationRequestVO(String requestStatus, String createdTimestamp, String requestType, String author, String createdBy, String city, String companyName, String buId, String country, List<RequestData> requestData, InvitationRequestMapVO invitationRequestMapVO) {
        this.requestStatus = requestStatus;
        this.createdTimestamp = createdTimestamp;
        this.requestType = requestType;
        this.author = author;
        this.createdBy = createdBy;
        this.city = city;
        this.companyName = companyName;
        this.buId = buId;
        this.country = country;
        this.requestData = requestData;
        this.invitationRequestMapVO = invitationRequestMapVO;
    }

   

    public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

  
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

  

  
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<RequestData> getRequestData() {
        return requestData;
    }

    public void setRequestData(List<RequestData> requestData) {
        this.requestData = requestData;
    }

    public InvitationRequestMapVO getInvitationRequestMapVO() {
        return invitationRequestMapVO;
    }

    public void setInvitationRequestMapVO(InvitationRequestMapVO invitationRequestMapVO) {
        this.invitationRequestMapVO = invitationRequestMapVO;
    }
}
