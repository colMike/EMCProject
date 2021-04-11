package com.compulynx.erevenue.models;

public class CompasResponse {
	
	public int respCode;
	public String respMessage;
	public CompasResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	public CompasResponse(int respCode) {
		super();
		this.respCode = respCode;
	}
	public CompasResponse() {
		// TODO Auto-generated constructor stub
	}
}
