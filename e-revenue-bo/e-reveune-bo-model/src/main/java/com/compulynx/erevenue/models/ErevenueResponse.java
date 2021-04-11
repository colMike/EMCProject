package com.compulynx.erevenue.models;

public class ErevenueResponse {
	
	public int respCode;
	public String respMessage;
	
	public ErevenueResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	public ErevenueResponse(int respCode) {
		super();
		this.respCode = respCode;
	}
}
