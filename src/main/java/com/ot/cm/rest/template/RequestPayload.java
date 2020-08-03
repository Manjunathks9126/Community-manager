package com.ot.cm.rest.template;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.ot.cm.constants.RestActionContext;

public class RequestPayload<T> {

	private String endPoint;
	private Map<String, String> queryParams;
	private HttpMethod httpMethod;
	private HttpEntity<?> requestType;
	private ParameterizedTypeReference<T> responseType;
	private RestActionContext actionContext;
	private String msgparams[];
	
	public RequestPayload(String endPoint, HttpMethod httpMethod,
			HttpEntity<?> requestType, ParameterizedTypeReference<T> responseType) {
		this.setRequestType(requestType);
		this.setEndPoint(endPoint);
		this.setHttpMethod(httpMethod);
		this.setResponseType(responseType);
		this.setQueryParams(queryParams);
	}

	public RequestPayload(String endPoint, Map<String, String> queryParams, HttpMethod httpMethod,
			HttpEntity<?> requestType, ParameterizedTypeReference<T> responseType) {
		this.setRequestType(requestType);
		this.setEndPoint(endPoint);
		this.setHttpMethod(httpMethod);
		this.setResponseType(responseType);
		this.setQueryParams(queryParams);
	}
	
	public String getEndPoint() {
		return endPoint;
	}

	private void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	private void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public HttpEntity<?> getRequestType() {
		return requestType;
	}

	private void setRequestType(HttpEntity<?> requestType) {
		this.requestType = requestType;
	}

	public ParameterizedTypeReference<T> getResponseType() {
		return responseType;
	}

	private void setResponseType(ParameterizedTypeReference<T> responseType) {
		this.responseType = responseType;
	}

	public RestActionContext getActionContext() {
		return actionContext;
	}

	public void setActionContext(RestActionContext actionContext) {
		this.actionContext = actionContext;
	}

	public String[] getMsgparams() {
		return msgparams;
	}

	public void setMsgparams(String[] msgparams) {
		this.msgparams = msgparams;
	}

}
