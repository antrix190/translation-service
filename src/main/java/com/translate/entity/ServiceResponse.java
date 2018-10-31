package com.translate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author antariksh.singh
 *
 */
public class ServiceResponse {
	public int statusCode;
	@JsonProperty("Response")
	public Object responseBody;
	
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the responseBody
	 */
	public Object getResponseBody() {
		return responseBody;
	}
	/**
	 * @param responseBody the responseBody to set
	 */
	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}
	
	public ServiceResponse(int statusCode, Object responseBody) {
		super();
		this.statusCode = statusCode;
		this.responseBody = responseBody;
	}
	
	public ServiceResponse(int statusCode) {
		super();
		this.statusCode = statusCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceResponse [statusCode=" + statusCode + ", responseBody=" + responseBody + "]";
	}
	
}
