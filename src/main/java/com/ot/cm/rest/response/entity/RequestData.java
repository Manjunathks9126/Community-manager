package com.ot.cm.rest.response.entity;



public class RequestData {
    private String requestAttrName;

    private String requestAttrValue;

    private String target ;


    public RequestData() {
    }

    public RequestData(String requestAttrName, String requestAttrValue, String target) {
        this.requestAttrName = requestAttrName;
        this.requestAttrValue = requestAttrValue;
        this.target = target;

    }

  

    public String getRequestAttrName() {
		return requestAttrName;
	}

	public void setRequestAttrName(String requestAttrName) {
		this.requestAttrName = requestAttrName;
	}

	public String getRequestAttrValue() {
		return requestAttrValue;
	}

	public void setRequestAttrValue(String requestAttrValue) {
		this.requestAttrValue = requestAttrValue;
	}

	public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


}
